package com.alibou.security.feature.episode.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Episode implements Serializable {
    private static final long serialVersionUID = 6242063620653123126L;

    private Long id;
    private Integer episodeNumber;
    private String description;
    private Date releaseDate;
    private Double duration;
    private String videoUrl;
    private Boolean status;
    private Date createAt;
    private Date updateAt;
}
