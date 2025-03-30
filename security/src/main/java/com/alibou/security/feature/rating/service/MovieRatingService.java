package com.alibou.security.feature.rating.service;

import com.alibou.security.feature.rating.model.MovieRating;
import com.alibou.security.feature.rating.model.RatingSummary;

import java.util.Optional;

public interface MovieRatingService {
    Optional<MovieRating> findRatingByUserIdAndMovieCode(String userId, String movieCode);

    MovieRating submitRatingByUserIdAndMovieCode(String userId, String movieCode, Integer rating);

    RatingSummary findAverageRatingByMovieCode(String movieCode);
}
