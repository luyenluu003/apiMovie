package com.alibou.security.feature.rating.dao;

import com.alibou.security.feature.rating.model.MovieRating;
import com.alibou.security.feature.rating.model.RatingSummary;

import java.util.Optional;

public interface MovieRatingDao {
    Optional<MovieRating> getRatingByUserIdAndMovieCode(String userId, String movieCode);
    MovieRating submitRating(String userId, String movieCode, Integer rating);
    RatingSummary getAverageRatingByMovieCode(String movieCode);

}
