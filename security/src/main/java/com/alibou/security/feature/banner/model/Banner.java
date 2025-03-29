package com.alibou.security.feature.banner.model;

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
public class Banner implements Serializable {

    private static final long serialVersionUID = 3681163620012687991L;

    private Long id;
    private String bannerImage;
    private Date createdAt;
    private Long createdBy;
    private Date endDate;
    private String movieBannerCode;
    private Integer position;
    private Date startDate;
    private Boolean status;
    private String title;
    private Date updatedAt;
    private Long updatedBy;
}
