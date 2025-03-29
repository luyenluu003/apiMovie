package com.alibou.security.feature.search.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultGroup implements Serializable {
    private static final long serialVersionUID = -5877571568446472152L;

    private String groupName;
    private String groupType;

    private List<ResultItem> items;

}
