package com.alibou.security.feature.actor.service;

import com.alibou.security.feature.actor.dao.ActorDao;
import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.episode.model.Episode;
import com.alibou.security.feature.movie.model.Movie;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Log4j2
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorDao actorDao;

    @Override
    public List<Actor> getActorsByListId(String userId, List<String> listId, long timestamp, String id) {
        List<Actor> listActor = actorDao.getActorByListId(listId);

        return listActor != null ? listActor : Collections.emptyList();
    }

    @Override
    public Actor findActorByActorId(String userId, String actorId) {
        log.info("Finding actor with actorId={}", actorId);

        try {
            Actor actor = actorDao.getActorByActorId(actorId);

            if (actor == null) {
                log.warn("No actor found with actorId={}", actorId);
                return null;
            }

            return actor;

        } catch (Exception e) {
            log.error("Error retrieving actor details for actorId={}: {}", actorId, e.getMessage(), e);
            return null;
        }
    }

}
