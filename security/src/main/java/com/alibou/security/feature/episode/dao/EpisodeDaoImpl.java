package com.alibou.security.feature.episode.dao;


import com.alibou.security.feature.episode.model.Episode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Log4j2
@Repository
public class EpisodeDaoImpl implements EpisodeDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Episode> getAllEpisodesByMovieCode(String movieCode){
        try{
            String sql = String.format(
                    "SELECT id, description, episode_number, duration, video_url, release_date " +
                            "FROM episode " +
                            "WHERE movie_code =:movieCode"
            );

            SqlParameterSource params = new MapSqlParameterSource("movieCode", movieCode);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Episode.builder()
                    .id(rs.getLong("id"))
                    .description(rs.getString("description"))
                    .episodeNumber(rs.getInt("episode_number"))
                    .duration(rs.getDouble("duration"))
                    .videoUrl(rs.getString("video_url"))
                    .releaseDate(rs.getDate("release_date"))
                    .build());
        }catch (Exception e){
            log.error("Error while getting episode list", e);
        }
        return Collections.emptyList();

    }

}
