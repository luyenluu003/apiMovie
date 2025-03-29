package com.alibou.security.api.v1.dto.favorite;

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
public class FavoriteDto implements Serializable {

    private static final long serialVersionUID = 6242063625293687126L;

    private Long id;
    private String movieCode;
    private String email;
    private Boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date favoriteDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date unfavoriteDate;

}
