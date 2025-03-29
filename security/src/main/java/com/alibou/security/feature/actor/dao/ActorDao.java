package com.alibou.security.feature.actor.dao;

import com.alibou.security.feature.actor.model.Actor;

import java.util.List;

public interface ActorDao {
    List<Actor> getAllActorByMovieCode(String movieCode);

    List<Actor> getActorByListId(List<String> listId);

    Actor getActorByActorId(String actorId);

    List<Actor> getActorAll();

}
