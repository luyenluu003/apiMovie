package com.alibou.security.feature.rating.service;

import com.alibou.security.feature.rating.dao.MovieRatingDao;
import com.alibou.security.feature.rating.model.MovieRating;
import com.alibou.security.feature.rating.model.RatingSummary;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@CacheConfig(cacheManager = "cacheManager3Hours")
public class MovieRatingServiceImpl implements MovieRatingService {

    @Autowired
    private MovieRatingDao movieRatingDao;

    @Override
//    @Cacheable(value = "ratings", key = "'list-rating:'.concat(#userId).concat(':').concat(#movieCode)")
    public Optional<MovieRating> findRatingByUserIdAndMovieCode(String userId, String movieCode) {
        log.info("Retrieving rating for userId: {} and movieCode: {}", userId, movieCode);

        try {
            Optional<MovieRating> movieRating = movieRatingDao.getRatingByUserIdAndMovieCode(userId, movieCode);
            if (!movieRating.isPresent()) {
                log.warn("No rating found for userId: {} and movieCode: {}", userId, movieCode);
            }
            return movieRating;
        } catch (Exception e) {
            log.error("Error retrieving rating for userId: {} and movieCode: {}. Error: {}",
                    userId, movieCode, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
//    @CacheEvict(value = {"ratings", "averageRatings"},
//            key = "{'list-rating:'.concat(#userId).concat(':').concat(#movieCode), #movieCode}",
//            allEntries = false)
    public MovieRating submitRatingByUserIdAndMovieCode(String userId, String movieCode, Integer rating) {
        log.info("Submitting rating for userId: {}, movieCode: {}, rating: {}", userId, movieCode, rating);

        // Kiểm tra rating hợp lệ
        if (rating == null || rating < 1 || rating > 5) {
            log.error("Invalid rating value: {}. Rating must be between 1 and 5.", rating);
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        try {
            MovieRating movieRating = movieRatingDao.submitRating(userId, movieCode, rating);
            if (movieRating == null) {
                log.warn("Failed to submit rating for userId: {} and movieCode: {}", userId, movieCode);
                throw new RuntimeException("Rating submission failed");
            }
            return movieRating;
        } catch (Exception e) {
            log.error("Error submitting rating for userId: {}, movieCode: {}. Error: {}",
                    userId, movieCode, e.getMessage(), e);
            throw new RuntimeException("Failed to submit rating", e);
        }
    }

    @Override
//    @Cacheable(value = "averageRatings", key = "#movieCode")
    public RatingSummary findAverageRatingByMovieCode(String movieCode) {
        log.info("Calculating average rating for movieCode: {}", movieCode);

        // Kiểm tra đầu vào
        if (movieCode == null || movieCode.trim().isEmpty()) {
            log.error("Invalid movieCode: null or empty");
            throw new IllegalArgumentException("Movie code must not be null or empty");
        }

        try {
            RatingSummary ratingSummary = movieRatingDao.getAverageRatingByMovieCode(movieCode);
            if (ratingSummary == null) {
                log.info("No ratings found for movieCode: {}", movieCode);
                return new RatingSummary(0.0, 0L);
            }
            return ratingSummary;
        } catch (Exception e) {
            log.error("Failed to calculate average rating for movieCode: {}. Error: {}",
                    movieCode, e.getMessage(), e);
            throw new RuntimeException("Failed to calculate average rating", e);
        }
    }
}