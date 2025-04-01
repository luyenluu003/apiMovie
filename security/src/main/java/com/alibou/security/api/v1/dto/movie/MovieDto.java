package com.alibou.security.api.v1.dto.movie;

import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.episode.model.Episode;
import com.alibou.security.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDto implements Serializable {
    private static final long serialVersionUID = 6242063620653687126L;

    private Long id;
    private String movieCode;
    private String movieName;
    private String userphone;
    private String description;
    private String createdAt;
    private String updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
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
    private List<Episode> episodes;
    private List<Actor> actors;
}
