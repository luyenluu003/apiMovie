package com.alibou.security.feature.movie.dao;

import com.alibou.security.feature.movie.model.Movie;
import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Log4j2
public class MovieDaoImpl implements MovieDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Movie> getMovies(Boolean type, Integer page, Integer pageSize) {
        try {
            int offset = Math.max((page - 1) * pageSize, 0);


            String sql = String.format(
                    "SELECT category_id, censorship, description, duration, image_url, language, " +
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot " +
                            "FROM movie " +
                            "WHERE type = :type AND status = 1 " +
                            "LIMIT %d OFFSET %d", pageSize, offset // Truyền giá trị trực tiếp vào SQL
            );

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("type", type);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Movie.builder()
                    .categoryId(rs.getString("category_id"))
                    .censorship(rs.getInt("censorship"))
                    .description(rs.getString("description"))
                    .duration(rs.getDouble("duration"))
                    .imageUrl(rs.getString("image_url"))
                    .language(rs.getString("language"))
                    .movieCode(rs.getString("movie_code"))
                    .movieGenre(rs.getString("movie_genre"))
                    .movieName(rs.getString("movie_name"))
                    .releaseDate(rs.getDate("release_date"))
                    .status(rs.getBoolean("status"))
                    .type(rs.getBoolean("type"))
                    .userphone(rs.getString("user_phone"))
                    .videoUrl(rs.getString("video_url"))
                    .isHot(rs.getInt("is_hot"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public Movie getMovieByMovieId(String movieId) {
        try {
            String sql = "SELECT category_id, censorship, description, duration, image_url, language, " +
                    "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot " +
                    "FROM movie " +
                    "WHERE movie_code = :movieId AND status = 1 ";

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("movieId", movieId);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    Movie.builder()
                            .categoryId(rs.getString("category_id"))
                            .censorship(rs.getInt("censorship"))
                            .description(rs.getString("description"))
                            .duration(rs.getDouble("duration"))
                            .imageUrl(rs.getString("image_url"))
                            .language(rs.getString("language"))
                            .movieCode(rs.getString("movie_code"))
                            .movieGenre(rs.getString("movie_genre"))
                            .movieName(rs.getString("movie_name"))
                            .releaseDate(rs.getDate("release_date"))
                            .status(rs.getBoolean("status"))
                            .type(rs.getBoolean("type"))
                            .userphone(rs.getString("user_phone"))
                            .videoUrl(rs.getString("video_url"))
                            .isHot(rs.getInt("is_hot"))
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            log.warn("Movie not found with movieId: {}", movieId);
        } catch (Exception e) {
            log.error("Error retrieving movie: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Movie> getMovieAll() {
        String sql = "SELECT * FROM movie WHERE status = 1 ";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> Movie.builder()
                    .id(rs.getLong("id")) // Thêm id nếu có
                    .categoryId(rs.getString("category_id"))
                    .censorship(rs.getInt("censorship"))
                    .description(rs.getString("description"))
                    .duration(rs.getDouble("duration"))
                    .imageUrl(rs.getString("image_url"))
                    .language(rs.getString("language"))
                    .movieCode(rs.getString("movie_code"))
                    .movieGenre(rs.getString("movie_genre"))
                    .movieName(rs.getString("movie_name"))
                    .releaseDate(rs.getDate("release_date"))
                    .status(rs.getBoolean("status"))
                    .type(rs.getBoolean("type"))
                    .userphone(rs.getString("user_phone"))
                    .videoUrl(rs.getString("video_url"))
                    .isHot(rs.getInt("is_hot"))
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<Movie> getMovieHots(Integer isHot, Integer page, Integer pageSize) {
        try {
            int offset = Math.max((page - 1) * pageSize, 0);


            String sql = String.format(
                    "SELECT category_id, censorship, description, duration, image_url, language, " +
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot " +
                            "FROM movie " +
                            "WHERE is_hot = :isHot AND status = 1 " +
                            "LIMIT %d OFFSET %d", pageSize, offset // Truyền giá trị trực tiếp vào SQL
            );

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("isHot", isHot);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Movie.builder()
                    .categoryId(rs.getString("category_id"))
                    .censorship(rs.getInt("censorship"))
                    .description(rs.getString("description"))
                    .duration(rs.getDouble("duration"))
                    .imageUrl(rs.getString("image_url"))
                    .language(rs.getString("language"))
                    .movieCode(rs.getString("movie_code"))
                    .movieGenre(rs.getString("movie_genre"))
                    .movieName(rs.getString("movie_name"))
                    .releaseDate(rs.getDate("release_date"))
                    .status(rs.getBoolean("status"))
                    .type(rs.getBoolean("type"))
                    .userphone(rs.getString("user_phone"))
                    .videoUrl(rs.getString("video_url"))
                    .isHot(rs.getInt("is_hot"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public Movie getMovieByMovieCode(String movieCode) {
        try {
            String sql = "SELECT category_id, censorship, description, duration, image_url, language, " +
                    "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot " +
                    "FROM movie " +
                    "WHERE movie_code = :movieCode and status = 1 ";

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("movieCode", movieCode);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    Movie.builder()
                            .categoryId(rs.getString("category_id"))
                            .censorship(rs.getInt("censorship"))
                            .description(rs.getString("description"))
                            .duration(rs.getDouble("duration"))
                            .imageUrl(rs.getString("image_url"))
                            .language(rs.getString("language"))
                            .movieCode(rs.getString("movie_code"))
                            .movieGenre(rs.getString("movie_genre"))
                            .movieName(rs.getString("movie_name"))
                            .releaseDate(rs.getDate("release_date"))
                            .status(rs.getBoolean("status"))
                            .type(rs.getBoolean("type"))
                            .userphone(rs.getString("user_phone"))
                            .videoUrl(rs.getString("video_url"))
                            .isHot(rs.getInt("is_hot"))
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            log.warn("Movie not found with movieCode: {}", movieCode);
        } catch (Exception e) {
            log.error("Error retrieving movie: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Movie> getMovieByListId(List<String> movieId) {
        try{
            List<Movie> listMovie = new ArrayList<>();
            for(String i : movieId){
                Movie movie = getMovieByMovieId(i);
                if(movie != null){
                    listMovie.add(movie);
                }
            }
            return listMovie;
        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Movie> getMovieByCategoryId(String categoryId, Boolean type, Integer page, Integer pageSize) {
        try {
            int offset = Math.max((page - 1) * pageSize, 0);


            String sql = String.format(
                    "SELECT category_id, censorship, description, duration, image_url, language, " +
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot " +
                            "FROM movie " +
                            "WHERE category_id = :categoryId AND status = 1 " +
                            "AND type =:type " +
                            "LIMIT %d OFFSET %d", pageSize, offset // Truyền giá trị trực tiếp vào SQL
            );

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("categoryId", categoryId)
                    .addValue("type", type);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Movie.builder()
                    .categoryId(rs.getString("category_id"))
                    .censorship(rs.getInt("censorship"))
                    .description(rs.getString("description"))
                    .duration(rs.getDouble("duration"))
                    .imageUrl(rs.getString("image_url"))
                    .language(rs.getString("language"))
                    .movieCode(rs.getString("movie_code"))
                    .movieGenre(rs.getString("movie_genre"))
                    .movieName(rs.getString("movie_name"))
                    .releaseDate(rs.getDate("release_date"))
                    .status(rs.getBoolean("status"))
                    .type(rs.getBoolean("type"))
                    .userphone(rs.getString("user_phone"))
                    .videoUrl(rs.getString("video_url"))
                    .isHot(rs.getInt("is_hot"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Movie> getMovieByCategoryIdHot(String categoryId, Integer isHot, Integer page, Integer pageSize) {
        try {
            int offset = Math.max((page - 1) * pageSize, 0);


            String sql = String.format(
                    "SELECT category_id, censorship, description, duration, image_url, language, " +
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot " +
                            "FROM movie " +
                            "WHERE category_id = :categoryId AND status = 1 " +
                            "AND is_hot =:isHot " +
                            "LIMIT %d OFFSET %d", pageSize, offset // Truyền giá trị trực tiếp vào SQL
            );

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("categoryId", categoryId)
                    .addValue("isHot", isHot);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Movie.builder()
                    .categoryId(rs.getString("category_id"))
                    .censorship(rs.getInt("censorship"))
                    .description(rs.getString("description"))
                    .duration(rs.getDouble("duration"))
                    .imageUrl(rs.getString("image_url"))
                    .language(rs.getString("language"))
                    .movieCode(rs.getString("movie_code"))
                    .movieGenre(rs.getString("movie_genre"))
                    .movieName(rs.getString("movie_name"))
                    .releaseDate(rs.getDate("release_date"))
                    .status(rs.getBoolean("status"))
                    .type(rs.getBoolean("type"))
                    .userphone(rs.getString("user_phone"))
                    .videoUrl(rs.getString("video_url"))
                    .isHot(rs.getInt("is_hot"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

}
