package com.alibou.security.feature.user.model;

import com.alibou.security.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription implements Serializable {
    private static final long serialVersionUID = 1947156833932400840L;

    @JsonIgnore
    private Long id;
    private String phoneNumber;
    private String subPackage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME)
    private Date registeredAt;
    private Date expiredAt;
    private String apiDomain;
}
