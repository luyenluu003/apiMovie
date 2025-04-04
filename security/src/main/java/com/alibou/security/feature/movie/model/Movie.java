package com.alibou.security.feature.movie.model;

import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.episode.model.Episode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie implements Serializable {
    private static final long serialVersionUID = 6242063620653687126L;

    private Long id;
    private String movieCode;
    private String movieName;
    private String userphone;
    private String description;
    private String createdAt;
    private String updatedAt;
    private Date releaseDate;
    private Double duration;
    private String categoryId;
    private Boolean type;
    private String imageUrl;
    private String videoUrl;
    private Boolean status;
    private String movieGenre;
    private Integer censorship;
    private String language;
    private Integer isHot;
    private Integer isVip;
    private String thumbnail;
    private List<Episode> episodes;
    private List<Actor> actors;
}
