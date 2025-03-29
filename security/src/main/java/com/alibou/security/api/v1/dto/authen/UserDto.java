package com.alibou.security.api.v1.dto.authen;

import com.alibou.security.feature.user.model.Subscription;
import com.alibou.security.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto implements Serializable {

    private static final long serialVersionUID = -5724155469057944671L;
    private String userId;
    private String loginId;
    private String baseOn; // phone or email
    private String phoneNumber;
    private String email;

    private String password;
    private String token;

    private String userName;
    private String avatar;
    private Long lastAvatar;
    private Integer active;

    private Date tokenExpired;
    private Date createdAt;
    private Date updatedAt;
    private String uuid;
    private String regid;
    private String vRegid;

    private String otp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.ISO_DATE_TIME,timezone = DateUtil.TIMEZONE_VN)
    private Date otpExpired;
    private List<Subscription> listSub;


}
