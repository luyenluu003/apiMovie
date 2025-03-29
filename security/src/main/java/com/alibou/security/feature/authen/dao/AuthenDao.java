package com.alibou.security.feature.authen.dao;

import com.alibou.security.feature.user.model.User;

import java.util.Date;
import java.util.Map;

public interface AuthenDao {
    User checkUserByLoginId(String loginId);

    User checkUserByPhone(String phoneNumber);

    User getUserByUserId(String userId);

    Integer saveUserInfo(User user);

    Integer updateOtp(String userId, String otp, Date otpExpired);

    Integer updateUserInfo(User user);

    User getOtpByUserId(String userId);

    Integer updateToken(User user);

    Integer updateTokenForgotPassword(String email, String link, Date date);

    Map<String, Object> getTokenForgotPassword(String email);

    Integer updatePassword(String email, String password);

    Integer updateTimeOtp(String phoneNumber, Date date);

    Integer updateTimeTokenResetPass(String email, Date date);

    User getPassword(String email);
}
