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
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
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
                    "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
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
                            .isVip(rs.getInt("is_vip"))
                            .thumbnail(rs.getString("thumbnail"))
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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
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
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public List<Movie> getMovieVips(Integer isVip, Integer page, Integer pageSize) {
        try {
            int offset = Math.max((page - 1) * pageSize, 0);


            String sql = String.format(
                    "SELECT category_id, censorship, description, duration, image_url, language, " +
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
                            "FROM movie " +
                            "WHERE is_vip = :isVip AND status = 1 " +
                            "LIMIT %d OFFSET %d", pageSize, offset // Truyền giá trị trực tiếp vào SQL
            );

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("isVip", isVip);

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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
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
                    "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
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
                            .isVip(rs.getInt("is_vip"))
                            .thumbnail(rs.getString("thumbnail"))
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
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
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
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Movie> getMovieByCategoryIdVip(String categoryId, Integer isVip, Integer page, Integer pageSize) {
        try {
            int offset = Math.max((page - 1) * pageSize, 0);


            String sql = String.format(
                    "SELECT category_id, censorship, description, duration, image_url, language, " +
                            "movie_code, movie_genre, movie_name, release_date, status, type, user_phone, video_url, is_hot, is_vip, thumbnail " +
                            "FROM movie " +
                            "WHERE category_id = :categoryId AND status = 1 " +
                            "AND is_vip =:isVip " +
                            "LIMIT %d OFFSET %d", pageSize, offset // Truyền giá trị trực tiếp vào SQL
            );

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("categoryId", categoryId)
                    .addValue("isVip", isVip);

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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Movie> findMoviesByFilters(List<String> genres, List<String> actors, Integer yearFrom, Integer yearTo) {
        try {
            // Xây dựng câu truy vấn SQL động
            StringBuilder sql = new StringBuilder(
                    "SELECT DISTINCT m.category_id, m.censorship, m.description, m.duration, m.image_url, m.language, " +
                            "m.movie_code, m.movie_genre, m.movie_name, m.release_date, m.status, m.type, m.user_phone, m.video_url, m.is_hot, m.is_vip, m.thumbnail " +
                            "FROM movie m "
            );

            MapSqlParameterSource params = new MapSqlParameterSource();
            List<String> conditions = new ArrayList<>();

            // Điều kiện cơ bản: chỉ lấy phim đang hoạt động
            conditions.add("m.status = 1");

            // Lọc theo thể loại (movie_genre)
            if (genres != null && !genres.isEmpty()) {
                List<String> likeConditions = new ArrayList<>();
                for (int i = 0; i < genres.size(); i++) {
                    String paramName = "genre" + i;
                    likeConditions.add("LOWER(m.movie_genre) LIKE LOWER(CONCAT('%', :" + paramName + ", '%'))");
                    params.addValue(paramName, genres.get(i));
                }
                conditions.add("(" + String.join(" OR ", likeConditions) + ")");
            }

            // Lọc theo diễn viên (giả sử có bảng movie_actor)
            if (actors != null && !actors.isEmpty()) {
                sql.append("JOIN movie_actor ma ON m.movie_code = ma.movie_code ");
                sql.append("JOIN actor a ON ma.actor_code = a.actor_code ");
                List<String> actorConditions = new ArrayList<>();
                for (int i = 0; i < actors.size(); i++) {
                    String paramName = "actor" + i;
                    actorConditions.add("LOWER(a.name) = LOWER(:" + paramName + ")");
                    params.addValue(paramName, actors.get(i));
                }
                conditions.add("(" + String.join(" OR ", actorConditions) + ")");
            }

            // Lọc theo năm phát hành
            if (yearFrom != null) {
                conditions.add("YEAR(m.release_date) >= :yearFrom");
                params.addValue("yearFrom", yearFrom);
            }
            if (yearTo != null) {
                conditions.add("YEAR(m.release_date) <= :yearTo");
                params.addValue("yearTo", yearTo);
            }

            // Gộp các điều kiện vào SQL
            if (!conditions.isEmpty()) {
                sql.append("WHERE ").append(String.join(" AND ", conditions));
            }

            // Thêm phân trang (tùy chọn, mặc định lấy 10 phim)
            sql.append(" LIMIT 10 OFFSET 0");

            log.info("Generated SQL: {}", sql.toString());

            return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> Movie.builder()
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
                    .isVip(rs.getInt("is_vip"))
                    .thumbnail(rs.getString("thumbnail"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving movies by filters: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
