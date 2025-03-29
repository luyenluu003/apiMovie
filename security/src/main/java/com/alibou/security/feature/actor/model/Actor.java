package com.alibou.security.feature.actor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Actor implements Serializable {
    private static final long serialVersionUID = 112312345621864L;

    private Long id;
    private String actorCode;
    private String name;
    private String bio;
    private Date dateOfBirth;
    private String avatar;
    private Boolean active;
    private Boolean status;
    private Date createdAt;
    private Date updatedAt;
    private Long createdBy;
    private Long updatedBy;


}
