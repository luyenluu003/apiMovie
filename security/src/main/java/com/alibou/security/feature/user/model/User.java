package com.alibou.security.feature.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User  implements Serializable {
    private static final long serialVersionUID = 1231239774020L;
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
    private Date otpExpired;
}
