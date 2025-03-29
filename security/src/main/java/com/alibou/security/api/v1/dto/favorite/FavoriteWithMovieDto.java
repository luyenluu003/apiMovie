package com.alibou.security.api.v1.dto.favorite;

import com.alibou.security.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteWithMovieDto implements Serializable {
    private static final long serialVersionUID = 6242063625211187126L;
    private Boolean active;
    private String email;
    private String movieCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date favoriteDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date unfavoriteDate;

    // Thông tin Movie
    private String movieName;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date releaseDate;
    private String movieGenre;
    private Integer isHot;
    private String imageUrl;
}
