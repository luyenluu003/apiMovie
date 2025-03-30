package com.alibou.security.feature.rating.model;

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
public class MovieRating implements Serializable {

    private static final long serialVersionUID = 8744173862653686921L;
    private Long id;
    private String userId;
    private String movieCode;
    private Integer rating;
    private Date createdAt;
    private Date updatedAt;
}
