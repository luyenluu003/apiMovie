package com.alibou.security.api.v1.dto.banner;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BannerDto implements Serializable {
    private static final long serialVersionUID = 3681163620012687991L;

    private Long id;
    private String bannerImage;
    private Date createdAt;
    private Long createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date endDate;
    private String movieBannerCode;
    private Integer position;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date startDate;
    private Boolean status;
    private String title;
    private Date updatedAt;
    private Long updatedBy;
}
