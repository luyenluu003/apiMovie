package com.alibou.security.api.v1.rating;

import com.alibou.security.feature.notification.model.Notification;
import com.alibou.security.feature.rating.model.MovieRating;
import com.alibou.security.feature.rating.model.RatingSummary;
import com.alibou.security.feature.rating.service.MovieRatingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@RequestMapping("/v1/rating")
public class RatingController {

    @Autowired
    private MovieRatingService movieRatingService;

    @GetMapping("/getRating")
    @ResponseBody
    public ResponseEntity<MovieRating> getRating(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("movieCode") String movieCode) {
        Long start = System.currentTimeMillis();
        log.info("[RATING] Get rating for userId: {}", userId);

        Optional<MovieRating> movieRatingOpt = movieRatingService.findRatingByUserIdAndMovieCode(userId, movieCode);

        if (!movieRatingOpt.isPresent()) {
            log.info("[RATING] No rating found for userId: {} and movieCode: {}", userId, movieCode);
            return ResponseEntity.noContent().build();
        }

        MovieRating movieRating = movieRatingOpt.get();
        Long executionTime = System.currentTimeMillis() - start;
        log.info("[RATING] userId: {} | END | ExecutionTime: {}ms", userId, executionTime);
        return ResponseEntity.ok(movieRating);
    }

    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<MovieRating> submitRating(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("movieCode") String movieCode,
            @RequestParam("rating") Integer rating) {
        Long start = System.currentTimeMillis();
        log.info("[RATING] Submit rating for userId: {}, movieCode: {}, rating: {}", userId, movieCode, rating);

        MovieRating movieRating = movieRatingService.submitRatingByUserIdAndMovieCode(userId, movieCode, rating);

        Long executionTime = System.currentTimeMillis() - start;
        log.info("[RATING] userId: {} | END | ExecutionTime: {}ms", userId, executionTime);
        return ResponseEntity.ok(movieRating);
    }

    @GetMapping("/average")
    @ResponseBody
    public ResponseEntity<RatingSummary> getAverageRating(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("movieCode") String movieCode) {
        Long start = System.currentTimeMillis();
        log.info("[RATING] Get average rating for movieCode: {}", movieCode);

        RatingSummary ratingSummary = movieRatingService.findAverageRatingByMovieCode(movieCode);

        Long executionTime = System.currentTimeMillis() - start;
        log.info("[RATING] movieCode: {} | END | ExecutionTime: {}ms", movieCode, executionTime);
        return ResponseEntity.ok(ratingSummary);
    }
}
