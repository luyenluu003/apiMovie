package com.alibou.security.api.v1.episode;

import com.alibou.security.api.v1.dto.episode.EpisodeDto;
import com.alibou.security.feature.episode.service.EpisodeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/v1/episode")
public class EpisodeController {

    @Autowired
    private EpisodeService episodeService;

    @GetMapping(value = "/{movieCode}/allEpisode")
    @ResponseBody
    public ResponseEntity<List<EpisodeDto>> getAllEpisodeByMovieCode(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @PathVariable("movieCode") String movieCode,
            HttpServletRequest request
    ) {
        Long start = System.currentTimeMillis();
        log.info("[EPISODE]:" + "userId=" + userId + " |movieCode=" + movieCode);

        List<EpisodeDto> episodeDtos = episodeService.findAllEpisodesByMovieCode( userId, movieCode);

        // Kiểm tra nếu không có dữ liệu
        if (episodeDtos == null || episodeDtos.isEmpty()) {
            log.info("[EPISODE]:" + "No episodes found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(episodeDtos);
    }
}
