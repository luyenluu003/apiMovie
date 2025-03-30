package com.alibou.security.feature.rating.dao;

import com.alibou.security.feature.rating.model.MovieRating;
import com.alibou.security.feature.rating.model.RatingSummary;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
@Log4j2
public class MovieRatingDaoImpl implements MovieRatingDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<MovieRating> getRatingByUserIdAndMovieCode(String userId, String movieCode) {
        try {
            String checkSql = "SELECT * FROM movie_rating WHERE user_id = :userId AND movie_code = :movieCode";
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("movieCode", movieCode);
            MovieRating rating = jdbcTemplate.queryForObject(checkSql, params, (rs, rowNum) -> MovieRating.builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getString("user_id"))
                    .movieCode(rs.getString("movie_code"))
                    .rating(rs.getInt("rating"))
                    .createdAt(rs.getDate("created_at"))
                    .updatedAt(rs.getDate("updated_at"))
                    .build());
            return Optional.of(rating);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Rating not found with movieCode: {} and UserId: {}", movieCode, userId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error retrieving Rating: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve rating", e);
        }
    }

    @Override
    public MovieRating submitRating(String userId, String movieCode, Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        try {
            // Kiểm tra xem đã có đánh giá chưa
            Optional<MovieRating> existingRating = getRatingByUserIdAndMovieCode(userId, movieCode);
            String sql;
            SqlParameterSource params;

            if (existingRating.isPresent()) {
                // Cập nhật đánh giá
                sql = "UPDATE movie_rating SET rating = :rating, updated_at = CURRENT_TIMESTAMP " +
                        "WHERE user_id = :userId AND movie_code = :movieCode";
                params = new MapSqlParameterSource()
                        .addValue("userId", userId)
                        .addValue("movieCode", movieCode)
                        .addValue("rating", rating);
            } else {
                // Thêm mới đánh giá, bao gồm cả created_at và updated_at
                sql = "INSERT INTO movie_rating (user_id, movie_code, rating, created_at, updated_at) " +
                        "VALUES (:userId, :movieCode, :rating, :createdAt, :updatedAt)";
                params = new MapSqlParameterSource()
                        .addValue("userId", userId)
                        .addValue("movieCode", movieCode)
                        .addValue("rating", rating)
                        .addValue("createdAt", new Timestamp(System.currentTimeMillis()))
                        .addValue("updatedAt", new Timestamp(System.currentTimeMillis())); // Thêm updated_at
            }

            jdbcTemplate.update(sql, params);
            return getRatingByUserIdAndMovieCode(userId, movieCode)
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve saved rating"));
        } catch (Exception e) {
            log.error("Error submitting rating: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to submit rating", e);
        }
    }

    @Override
    public RatingSummary getAverageRatingByMovieCode(String movieCode) {
        try {
            String sql = "SELECT AVG(rating) as average_rating, COUNT(*) as total_ratings " +
                    "FROM movie_rating WHERE movie_code = :movieCode";
            SqlParameterSource params = new MapSqlParameterSource().addValue("movieCode", movieCode);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    new RatingSummary(rs.getDouble("average_rating"), rs.getLong("total_ratings"))
            );
        } catch (EmptyResultDataAccessException e) {
            log.warn("No ratings found for movieCode: {}", movieCode);
            return new RatingSummary(0.0, 0L); // Trả về mặc định nếu không có đánh giá
        } catch (Exception e) {
            log.error("Error calculating average rating for movieCode: {}. Error: {}", movieCode, e.getMessage(), e);
            throw new RuntimeException("Failed to calculate average rating", e);
        }
    }
}