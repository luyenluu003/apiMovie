package com.alibou.security.feature.favorite.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Favorite implements Serializable {
    private static final long serialVersionUID = 6242063625293687126L;

    private Long id;
    private String movieCode;
    private String email;
    private Boolean active;
    private Date favoriteDate;
    private Date unfavoriteDate;


}
