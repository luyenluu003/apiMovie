package com.alibou.security.feature.authen.service;

import com.alibou.security.api.v1.dto.authen.UserDto;
import com.alibou.security.feature.user.model.User;

import java.util.Map;

public interface AuthenService {
    UserDto requestOTPByPhone(String phoneNumber, String lang);

    UserDto loginByPhone(String phoneNumber, String otp);

    UserDto loginByEmail(String email, String password);

    Boolean registrationUser(User user);

    Boolean sendTokenResetPassword(String email, String lang);

    Map<String, Object> resetPassword(String email, String password, Integer tokenResetPassword);

    Map<String, Object> changePassword(String email, String oldPassword, String newPassword);
}
