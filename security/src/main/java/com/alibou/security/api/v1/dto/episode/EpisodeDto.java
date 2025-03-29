package com.alibou.security.api.v1.dto.episode;

import com.alibou.security.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EpisodeDto implements Serializable {
    private static final long serialVersionUID = 6242063620651117121L;

    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Double duration;
    private String description;
    private Integer episodeNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date releaseDate;
    private Boolean status;
    private String videoUrl;
}
