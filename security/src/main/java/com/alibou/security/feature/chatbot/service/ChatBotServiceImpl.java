package com.alibou.security.feature.chatbot.service;

import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.actor.dao.ActorDao;
import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.category.model.Category;
import com.alibou.security.feature.category.service.CategoryService;
import com.alibou.security.feature.chatbot.model.ChatMessage;
import com.alibou.security.feature.chatbot.model.ChatbotResponse;
import com.alibou.security.feature.movie.dao.MovieDao;
import com.alibou.security.feature.movie.model.Movie;
import com.alibou.security.feature.movie.service.MovieService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ActorDao actorDao;

    public ChatbotResponse processChatRequest(String userMessage, List<ChatMessage> chatHistory) {
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                log.error("API key không hợp lệ hoặc chưa được cấu hình!");
                return new ChatbotResponse("Lỗi: Chưa cấu hình API key!", new ArrayList<>());
            }

            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-002:generateContent?key=" + apiKey;

            String systemPrompt = "Bạn là trợ lý AI cho một trang web xem phim. " +
                    "Nhiệm vụ của bạn là đề xuất phim phù hợp với yêu cầu của người dùng. " +
                    "Luôn đề cập rõ ràng các thể loại phim (như hành động, tình cảm, kinh dị, anime, v.v.), " +
                    "tên diễn viên, đạo diễn hoặc năm phát hành trong câu trả lời của bạn. " +
                    "Nói chuyện bằng tiếng Việt một cách tự nhiên và thân thiện. ";

            Map<String, Object> requestBody = new HashMap<>();
            List<Map<String, Object>> contents = new ArrayList<>();

            for (ChatMessage msg : chatHistory) {
                if ("user".equals(msg.getRole()) || "assistant".equals(msg.getRole())) {
                    Map<String, Object> message = new HashMap<>();
                    message.put("role", msg.getRole());
                    Map<String, Object> textMap = new HashMap<>();
                    textMap.put("text", msg.getContent());
                    List<Map<String, Object>> partsList = new ArrayList<>();
                    partsList.add(textMap);
                    message.put("parts", partsList);
                    contents.add(message);
                }
            }

            String combinedUserMessage = systemPrompt + "\n" + userMessage;
            Map<String, Object> userMessageMap = new HashMap<>();
            userMessageMap.put("role", "user");
            Map<String, Object> userText = new HashMap<>();
            userText.put("text", combinedUserMessage);
            List<Map<String, Object>> userParts = new ArrayList<>();
            userParts.add(userText);
            userMessageMap.put("parts", userParts);
            contents.add(userMessageMap);

            requestBody.put("contents", contents);

            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.7);
            generationConfig.put("topK", 40);
            generationConfig.put("topP", 0.95);
            generationConfig.put("maxOutputTokens", 1024);
            requestBody.put("generationConfig", generationConfig);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            log.info("Gemini API Response: {}", response.getBody());

            String aiResponse = extractGeminiResponse(response.getBody());

            // Phân tích nội dung để tìm từ khóa về phim
            List<String> genres = extractGenres(aiResponse);
            List<String> actors = extractActors(aiResponse);
            Integer yearFrom = extractYearFrom(aiResponse);
            Integer yearTo = extractYearTo(aiResponse);

            // Lấy danh sách phim từ MovieService dựa trên các tiêu chí
            List<Movie> recommendedMovies = movieDao.findMoviesByFilters(genres, actors, yearFrom, yearTo);
            return new ChatbotResponse(aiResponse, recommendedMovies);

        } catch (Exception e) {
            log.error("Lỗi khi xử lý yêu cầu chat", e);
            return new ChatbotResponse("Xin lỗi, tôi không thể xử lý yêu cầu của bạn lúc này.", new ArrayList<>());
        }
    }

    private String extractGeminiResponse(Map responseBody) {
        try {
            log.info("Gemini raw response: {}", responseBody);
            if (responseBody == null || !responseBody.containsKey("candidates")) {
                return "Lỗi: Không có phản hồi từ AI.";
            }
            List<Map> candidates = (List<Map>) responseBody.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return "Không có phản hồi từ AI.";
            }
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            log.error("Lỗi khi trích xuất phản hồi từ Gemini", e);
            return "Xin lỗi, tôi không thể tạo phản hồi lúc này.";
        }
    }

    private List<String> extractGenres(String text) {
        List<Category> allGenres = categoryService.findALlCategory("123123"); // Lấy danh sách thể loại từ DB
        List<String> foundGenres = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (Category genre : allGenres) {
            if (lowerText.contains(genre.getName().toLowerCase())) {
                foundGenres.add(genre.getName());
            }
        }
        return foundGenres;
    }

    private List<String> extractActors(String text) {
        // Lấy danh sách diễn viên từ ActorDao
        List<Actor> knownActors = actorDao.getActorAll(); // Giả sử ActorDao trả về List<Actor>
        List<String> foundActors = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (Actor actor : knownActors) {
            String actorName = actor.getName().toLowerCase(); // Lấy tên diễn viên và chuyển thành chữ thường
            if (lowerText.contains(actorName)) {
                foundActors.add(actor.getName()); // Thêm tên diễn viên vào kết quả
            }
        }
        return foundActors;
    }

    private Integer extractYearFrom(String text) {
        Pattern pattern = Pattern.compile("\\b(19\\d{2}|20\\d{2})\\b");
        Matcher matcher = pattern.matcher(text);
        Integer yearFrom = null;
        while (matcher.find()) {
            int year = Integer.parseInt(matcher.group());
            if (yearFrom == null || year < yearFrom) {
                yearFrom = year;
            }
        }
        return yearFrom;
    }

    private Integer extractYearTo(String text) {
        Pattern pattern = Pattern.compile("\\b(19\\d{2}|20\\d{2})\\b");
        Matcher matcher = pattern.matcher(text);
        Integer yearTo = null;
        while (matcher.find()) {
            int year = Integer.parseInt(matcher.group());
            if (yearTo == null || year > yearTo) {
                yearTo = year;
            }
        }
        return yearTo;
    }
}