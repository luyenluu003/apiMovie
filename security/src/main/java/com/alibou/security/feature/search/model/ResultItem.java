package com.alibou.security.feature.search.model;

import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.category.model.Category;
import com.alibou.security.feature.movie.model.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultItem implements Serializable {
    private static final long serialVersionUID = -8065641162620821547L;

    private Long id;
    private String itemName;
    private String itemType;
    private String avatar;

//   extend
    private Movie movieInfo;
    private Actor actorInfo;
    private Category categoryInfo;
}
