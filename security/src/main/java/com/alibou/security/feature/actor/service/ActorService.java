package com.alibou.security.feature.actor.service;

import com.alibou.security.feature.actor.model.Actor;

import java.util.List;

public interface ActorService {

    List<Actor> getActorsByListId(String userId, List<String> listId, long timestamp, String id);

    Actor findActorByActorId(String userId, String actorId);
}
