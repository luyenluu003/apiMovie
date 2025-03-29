package com.alibou.security.feature.SearchKeyword.model;

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
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Searchkeyword implements Serializable {

    private static final long serialVersionUID = 1242063620653611126L;

    private Long id;
    private String keyword;
    private Date createdAt;
    private String description;
    private Boolean status;
    private Date updatedAt;
}
