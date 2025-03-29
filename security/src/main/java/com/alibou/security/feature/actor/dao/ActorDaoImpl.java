package com.alibou.security.feature.actor.dao;


import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.movie.model.Movie;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Repository
public class ActorDaoImpl implements ActorDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Actor> getAllActorByMovieCode(String movieCode){
        try{
            String sql = String.format("SELECT a.actor_code, a.avatar, a.name, a.status " +
                    "FROM actor a " +
                    "JOIN movie_actor ma ON a.actor_code = ma.actor_code " +
                    "WHERE ma.movie_code = :movieCode"
            );
            SqlParameterSource params = new MapSqlParameterSource("movieCode", movieCode);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Actor.builder()
                    .actorCode(rs.getString("actor_code"))
                    .avatar(rs.getString("avatar"))
                    .name(rs.getString("name"))
                    .status(rs.getBoolean("status"))
                    .build());
        }catch (Exception e){
            log.error("Error while getting actor list", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Actor getActorByActorId(String actorId){
        try{
            String sql = "SELECT actor_code, avatar, bio, date_of_birth, name, status " +
                    "FROM actor " +
                    "WHERE actor_code = :actorId";

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("actorId", actorId);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    Actor.builder()
                            .actorCode(rs.getString("actor_code"))
                            .avatar(rs.getString("avatar"))
                            .bio(rs.getString("bio"))
                            .dateOfBirth(rs.getDate("date_of_birth"))
                            .name(rs.getString("name"))
                            .status(rs.getBoolean("status"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            log.warn("Actor not found with actorId: {}", actorId);
        } catch (Exception e) {
            log.error("Error while getting actor: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Actor> getActorByListId(List<String> listId){
        try{
            List<Actor> listActor = new ArrayList<>();
            for(String id : listId){
                Actor actor = getActorByActorId(id);
                if(actor !=null ){
                    listActor.add(actor);
                }
            }
            return listActor;
        } catch (Exception e){
            log.error("Error retrieving actor: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Actor> getActorAll(){
        String sql = "SELECT * FROM actor WHERE active = 1";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> Actor.builder()
                    .id(rs.getLong("id")) // Thêm id nếu có
                    .actorCode(rs.getString("actor_code"))
                    .avatar(rs.getString("avatar"))
                    .bio(rs.getString("bio"))
                    .dateOfBirth(rs.getDate("date_of_birth"))
                    .name(rs.getString("name"))
                    .status(rs.getBoolean("status"))
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving actor: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
