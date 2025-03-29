package com.alibou.security.api.v1.actor;

import com.alibou.security.api.v1.dto.SuccessResDto;
import com.alibou.security.feature.actor.dao.ActorDao;
import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.actor.service.ActorService;
import com.alibou.security.feature.movie.model.Movie;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RestController
@Validated
@RequestMapping("/v1/actor")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @GetMapping("/detailActor")
    @ResponseBody
    public ResponseEntity<?> getMovieDetail(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("actorId") String actorId,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();

        Actor actor = actorService.findActorByActorId(userId, actorId);
        if (actor == null ) {
            log.info("[actor]:" + "No actor found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[actor]:userId=" + userId + "|END|Executime=" + t);
        SuccessResDto data = SuccessResDto.builder()
                .message("Actor Movie Success")
                .data(actor)
                .build();
        return ResponseEntity.ok().body(data);
    }
}
