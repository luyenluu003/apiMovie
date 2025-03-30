package com.alibou.security.feature.rating.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingSummary implements Serializable {
    private static final long serialVersionUID = 1142063625211187527L;
    private Double averageRating;
    private Long totalRatings;
}
