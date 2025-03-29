package com.alibou.security.api.v1.movie;

import com.alibou.security.api.v1.dto.SuccessResDto;
import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.movie.model.Movie;
import com.alibou.security.feature.movie.service.MovieService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/v1/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping(value = "/allMovie")
    @ResponseBody
    public ResponseEntity<List<MovieDto>> getAllMovie(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("type") Boolean type,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            HttpServletRequest request
    ) {
        Long start = System.currentTimeMillis();
        log.info("[MOVIE]:" + "userId=" + userId + " |type=" + type);

        List<MovieDto> movies = movieService.findAllMovies(page, pageSize, userId, type);

        // Kiểm tra nếu không có dữ liệu
        if (movies == null || movies.isEmpty()) {
            log.info("[MOVIE]:" + "No movies found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/moviehot")
    @ResponseBody
    public ResponseEntity<?> getMovieHot(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("isHot") Integer isHot,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();
        log.info("[MOVIE]:" + "userId=" + userId + " |isHot=" + isHot);

        List<MovieDto> movies = movieService.findAllMoviesIsHot(page, pageSize, userId, isHot);

        // Kiểm tra nếu không có dữ liệu
        if (movies == null || movies.isEmpty()) {
            log.info("[MOVIE]:" + "No movies found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(movies);
    }



    @GetMapping("/detailMovie")
    @ResponseBody
    public ResponseEntity<?> getMovieDetail(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("movieCode") String movieCode,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();

        Movie movie = movieService.findMovieByMovieCode(userId, movieCode);
        if (movie == null ) {
            log.info("[MOVIE]:" + "No movie found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        SuccessResDto data = SuccessResDto.builder()
                .message("Detail Movie Success")
                .data(movie)
                .build();
        return ResponseEntity.ok().body(data);
    }

    @DeleteMapping("/clearAllCacheMovies")
    public ResponseEntity<String> clearAllCacheMovies() {
        try {
            // Xóa cache cho tất cả các user
            movieService.clearAllMoviesCache();
            return ResponseEntity.ok("All movie cache cleared!");
        } catch (Exception e) {
            log.error("Error clearing cache: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear movie cache!");
        }
    }

    @GetMapping("/category")
    @ResponseBody
    public ResponseEntity<?> getMovieByCategory(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("categoryId") String categoryId,
            @RequestParam("type") Boolean type,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();
        log.info("[MOVIE]:" + "userId=" + userId + " |categoryId=" + categoryId);

        List<MovieDto> movies = movieService.findAllMovieByCategoryId(userId, categoryId, type, page, pageSize);

        // Kiểm tra nếu không có dữ liệu
        if (movies == null || movies.isEmpty()) {
            log.info("[MOVIE]:" + "No movies found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movieHot/category")
    @ResponseBody
    public ResponseEntity<?> getMovieByCategoryHot(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("categoryId") String categoryId,
            @RequestParam("isHot") Integer isHot,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();
        log.info("[MOVIE]:" + "userId=" + userId + " |categoryId=" + categoryId);

        List<MovieDto> movies = movieService.findAllMovieByCategoryIdHot(userId, categoryId, isHot, page, pageSize);

        // Kiểm tra nếu không có dữ liệu
        if (movies == null || movies.isEmpty()) {
            log.info("[MOVIE]:" + "No movies found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(movies);
    }
}
