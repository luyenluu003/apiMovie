package com.alibou.security.feature.authen.service;

import com.alibou.security.api.v1.dto.authen.UserDto;
import com.alibou.security.common.UploadFile;
import com.alibou.security.exception.NonHandleException;
import com.alibou.security.feature.authen.dao.AuthenDao;
import com.alibou.security.feature.authen.model.MessageOTP;
import com.alibou.security.feature.authen.model.MessageSendPassword;
import com.alibou.security.feature.common.service.MediaService;
import com.alibou.security.feature.user.dao.UserDao;
import com.alibou.security.feature.user.model.User;
import com.alibou.security.utils.AuthenticationUtil;
import com.alibou.security.utils.ReengUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Log4j2
public class AuthenServiceImpl implements AuthenService {
    public static final int TIME_EXPIRED_TOKEN_RESET_PASSWORD = 60 * 60;
    private static final int TIME_EXPIRED_OTP = 30;

    @Value("${google.clientId}")
    private String googleClientId;

    @Autowired
    private AuthenDao authenDao;

    @Autowired
    private MediaService mediaService;

    @Value("${superapp.jwt.secret.key}")
    private String jwtSecret;

    @Value("${superapp.jwt.expire.duration1}")
    private long jwtExpiration;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UploadFile uploadFile;


    @Override
    public UserDto requestOTPByPhone(String phoneNumber, String lang) {
        User user = authenDao.checkUserByPhone(phoneNumber);
        String otp = "1111";
        String encryptOtp = encryptBcrypt(otp);
        Date otpExpiredAt = DateUtils.addSeconds(new Date(), TIME_EXPIRED_OTP);

        if (user == null) {
            throw new InvalidParameterException("Số điện thoại chưa được đăng ký");
        } else {
            user.setOtp(encryptOtp);
            user.setOtpExpired(otpExpiredAt);
            authenDao.updateOtp(user.getUserId(), user.getOtp(), user.getOtpExpired());
        }

        User userDb = authenDao.checkUserByPhone(phoneNumber);

        MessageOTP messageOTP = MessageOTP.builder()
                .otp(otp)
                .sendTo(userDb.getPhoneNumber())
                .lang(lang)
                .build();

        log.info("VÀO");
        return UserDto.builder()
                .phoneNumber(userDb.getPhoneNumber())
                .build();
    }

    @Override
    public UserDto loginByPhone(String phoneNumber, String otp) {
        User user = authenDao.checkUserByPhone(phoneNumber);
        if (user == null) {
            throw new InvalidParameterException("Số điện thoại chưa được đăng ký");
        }

        long timestamp = new Date().getTime();
        long timeExpired = timestamp + AuthenticationUtil.TOKEN_TIME_EXPIRED_SCALE;

        if (isValidOtp(otp, timestamp, user)) {
            authenDao.updateTimeOtp(user.getPhoneNumber(), new Date(timestamp));
            return sendTokenLoginSuccess(user, timeExpired);
        } else {
            throw new InvalidParameterException("Invalid OTP");
        }
    }

    @Override
    public UserDto loginByEmail(String email, String password) {
        User user = authenDao.checkUserByLoginId(email);
        if (user == null) {
            throw new InvalidParameterException("Account doesn't exist");
        }

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        Boolean check = bCrypt.matches(password, user.getPassword());
        if (!check) {
            throw new InvalidParameterException("Invalid password");
        }

        long timestamp = new Date().getTime();
        long timeExpired = timestamp + AuthenticationUtil.TOKEN_TIME_EXPIRED_SCALE;
        return sendTokenLoginSuccess(user, timeExpired);
    }

    @Override
    public Boolean registrationUser(User userInfo) {
        log.info("CHECK");
        User userByEmail = authenDao.checkUserByLoginId(userInfo.getEmail());
        if (userByEmail != null) {
            throw new InvalidParameterException("Email is already used for another account!");
        }

        User userByPhone = authenDao.checkUserByPhone(userInfo.getPhoneNumber());
        if (userByPhone != null) {
            throw new InvalidParameterException("Phone number is already used for another account!");
        }
        userInfo.setUserId(String.valueOf(System.currentTimeMillis()));
        userInfo.setLoginId(userInfo.getEmail());
        userInfo.setBaseOn("email");
        String password = encryptBcrypt(userInfo.getPassword());
        userInfo.setPassword(password);
        userInfo.setActive(1);
        return authenDao.saveUserInfo(userInfo) == 1;
    }

    @Override
    public Boolean sendTokenResetPassword(String email, String lang) {
        User userByEmail = authenDao.checkUserByLoginId(email);
        if (userByEmail == null) {
            throw new InvalidParameterException("Email not exist");
        }

        String tokenResetPassword = "111111";
        Date expiredForgotPassword = DateUtils.addSeconds(new Date(), TIME_EXPIRED_TOKEN_RESET_PASSWORD);
        String linkResetPassword = email + tokenResetPassword;
        Integer updateToken = authenDao.updateTokenForgotPassword(email, encryptBcrypt(linkResetPassword), expiredForgotPassword);

        if (updateToken == 1) {
            MessageSendPassword message = MessageSendPassword.builder()
                    .token(tokenResetPassword)
                    .address(email)
                    .lang(lang)
                    .build();

            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> resetPassword(String email, String password, Integer tokenResetPassword) {
        if (!validateTokenResetPassword(email, tokenResetPassword)) {
            throw new InvalidParameterException("Token not valid");
        }

        Integer check = authenDao.updatePassword(email, encryptBcrypt(password));

        if (check == 1) {
            authenDao.updateTimeTokenResetPass(email, new Date());
            Map<String, Object> map = new HashMap<>();
            map.put("email", email);
            return map;
        }
        return null;
    }

    @Override
    public Map<String, Object> changePassword(String email, String oldPassword, String newPassword) {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        Boolean checkOldPass = bCrypt.matches(oldPassword, authenDao.getPassword(email).getPassword());
        if (checkOldPass) {
            Integer check = authenDao.updatePassword(email, encryptBcrypt(newPassword));
            if (check == 1) {
                authenDao.updateTimeTokenResetPass(email, new Date());
                Map<String, Object> map = new HashMap<>();
                map.put("email", email);
                return map;
            }
        } else {
            throw new InvalidParameterException("Wrong old password");
        }
        return null;
    }

    public Boolean isValidOtp(String otp, Long timestamp, User user) {
        try {
            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            String otpDb = user.getOtp();
            long otpExpiredTimeDb = user.getOtpExpired().getTime();
            if (otpDb != null && otpExpiredTimeDb != 0) {
                long otpResetTime = (otpExpiredTimeDb - timestamp) / 1000;
                if (0 <= otpResetTime && otpResetTime <= TIME_EXPIRED_OTP) {
                    return bCrypt.matches(otp, otpDb);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Boolean.FALSE;
    }

    private UserDto sendTokenLoginSuccess(User user, long timeExpired) {
        String token = AuthenticationUtil.generateToken(user.getUserId(), timeExpired, AuthenticationUtil.SECRET_CODE);
        if (token != null) {
            user.setToken(token);
            user.setTokenExpired(new Date(timeExpired));
            user.setActive(1);
            int rsCount = authenDao.updateToken(user);
            if (rsCount > 0) {
                String avatar = user.getAvatar();
                return UserDto.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .userName(user.getUserName())
                        .avatar(avatar)
                        .token(token)
                        .vipLevel(user.getVipLevel())
                        .vipEndDate(user.getVipEndDate())
                        .vipStartDate(user.getVipStartDate())
                        .tokenExpired(new Date(timeExpired))
                        .build();
            } else {
                throw new NonHandleException("Update's not successful.");
            }
        } else {
            throw new NonHandleException("Token can not be null.");
        }
    }

    private boolean validateTokenResetPassword(String email, Integer token) {
        if (email == null || token == null) {
            throw new InvalidParameterException("Email or token cannot be null");
        }
        User userByEmail = authenDao.checkUserByLoginId(email);
        if (userByEmail == null) {
            throw new InvalidParameterException("Email not exist");
        }

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        String link = email + token;
        Map<String, Object> map = authenDao.getTokenForgotPassword(email);

        Date date = (Date) map.get("date_expired");
        String linkDb = (String) map.get("link");

        boolean checkToken = bCrypt.matches(link, linkDb);
        int checkTimeExpired = (int) (date.getTime() - new Date().getTime()) / 1000;
        if (checkTimeExpired < 0) {
            throw new InvalidParameterException("Time not valid");
        }
        return checkTimeExpired <= TIME_EXPIRED_TOKEN_RESET_PASSWORD && checkToken;
    }


    public String encryptBcrypt(String oldString) {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        return bCrypt.encode(oldString);
    }

    @Override
    public User googleLogin(String credential) {
        try {
            // Decode token bằng jjwt (không verify chữ ký)
            JwtParser parser = Jwts.parser();
            String[] tokenParts = credential.split("\\.");
            if (tokenParts.length != 3) {
                throw new InvalidParameterException("Invalid Google token format");
            }

            // Decode payload (phần giữa của JWT)
            String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(tokenParts[1]));
            com.google.gson.JsonObject payload = new com.google.gson.JsonParser().parse(payloadJson).getAsJsonObject();

            // Lấy thông tin từ payload
            String email = payload.get("email").getAsString();
            String name = payload.has("name") ? payload.get("name").getAsString() : "Unknown";
            String picture = payload.has("picture") ? payload.get("picture").getAsString() : null;

            // Kiểm tra audience để đảm bảo token dành cho ứng dụng của bạn
            String aud = payload.get("aud").getAsString();
            if (!googleClientId.equals(aud)) {
                throw new InvalidParameterException("Invalid audience in Google token");
            }

            // Tìm hoặc tạo user
            User user = authenDao.checkUserByLoginId(email);
            if (user == null) {
                user = new User();
                user.setUserId(String.valueOf(System.currentTimeMillis()));
                user.setLoginId(email);
                user.setEmail(email);
                user.setUserName(name);
                user.setAvatar(picture);
                user.setPhoneNumber(null);
                user.setPassword(encryptBcrypt(String.valueOf(System.currentTimeMillis())));
                user.setBaseOn("google");
                user.setActive(1);
                authenDao.saveUserInfo(user);
            }

            // Cập nhật token và trả về User
            long timestamp = new Date().getTime();
            long timeExpired = timestamp + AuthenticationUtil.TOKEN_TIME_EXPIRED_SCALE;
            String token = AuthenticationUtil.generateToken(user.getUserId(), timeExpired, AuthenticationUtil.SECRET_CODE);
            if (token != null) {
                user.setToken(token);
                user.setTokenExpired(new Date(timeExpired));
                user.setActive(1);
                int rsCount = authenDao.updateToken(user);
                if (rsCount <= 0) {
                    throw new NonHandleException("Update's not successful.");
                }
            } else {
                throw new NonHandleException("Token can not be null.");
            }
            return user; // Trả về User thay vì UserDto
        } catch (Exception e) {
            log.error("Google login failed: " + e.getMessage(), e);
            throw new InvalidParameterException("Google login failed: " + e.getMessage());
        }
    }

    @Override
    public User updateUser(String userId, String userName, String phoneNumber, String avatar) {
        // Tìm user theo userId
        User user = userDao.userFindByuserId(userId);
        if (user == null) {
            log.error("USER NOT FOUND: {}", userId);
            return null;
        }

        // Chỉ cập nhật các trường nếu có giá trị mới và không rỗng
        if (userName != null && !userName.isEmpty()) {
            user.setUserName(userName);
        }
        if (phoneNumber != null) {
            // Kiểm tra định dạng số điện thoại nếu không rỗng
            if (!phoneNumber.isEmpty() && !ReengUtils.isPhoneNumber(phoneNumber)) {
                throw new InvalidParameterException("phoneNumber " + phoneNumber + " wrong format <CountryCode><Phone>");
            }
            user.setPhoneNumber(phoneNumber);
        }
        if (avatar != null && !avatar.isEmpty()) {
            String fileNameAvatarUser;
            // Kiểm tra xem avatar có phải là chuỗi base64 không
            if (isBase64(avatar)) {
                // Nếu là base64, xử lý upload file như trước
                fileNameAvatarUser = uploadFile.createImageFile(avatar, "avatar");
                log.info("FILENAMEAVATARUSER ==> {}" ,fileNameAvatarUser);
            } else {
                // Nếu không phải base64, sử dụng giá trị trực tiếp (có thể là URL hoặc tên file)
                fileNameAvatarUser = avatar;
                log.info("AVATAR GOC ==> {}" ,fileNameAvatarUser);

            }
            log.info("fileNameHeaderAvatar ------> {}", fileNameAvatarUser);
            user.setAvatar(fileNameAvatarUser);
        }
        // Nếu avatar là null hoặc rỗng, không làm gì cả -> giữ nguyên giá trị cũ

        // Cập nhật thời gian cập nhật mới
        user.setUpdatedAt(new Date());

        // Cập nhật user trong DB
        Integer updatedRows = userDao.updateUser(user);
        if (updatedRows > 0) {
            // Trả về thông tin người dùng đã được cập nhật
            return userDao.userFindByuserId(userId);
        } else {
            log.error("Lỗi khi cập nhật thông tin người dùng với userId: {}", userId);
            return null;
        }
    }

    // Hàm helper để kiểm tra chuỗi base64
    private boolean isBase64(String str) {
        try {
            // Kiểm tra xem chuỗi có hợp lệ với base64 không
            java.util.Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}