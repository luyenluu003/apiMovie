package com.alibou.security.api.v1;

import com.alibou.security.api.v1.dto.SuccessResDto;
import com.alibou.security.api.v1.dto.authen.UpdateUserDto;
import com.alibou.security.api.v1.dto.authen.UserDto;
import com.alibou.security.feature.authen.service.AuthenService;
import com.alibou.security.feature.user.model.User;
import com.alibou.security.utils.ReengUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;


@Log4j2
@RestController
@RequestMapping("/v1/authen")
@Validated
public class AuthenController {
    @Autowired
    private AuthenService authenService;


    @GetMapping("/test")
    public String test() {
        return "Test OK";
    }

    @PostMapping("/login/request-otp")
    public ResponseEntity<?> requestOTP(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("timestamp") long timestamp,
            @RequestParam("phoneNumber") @NotBlank String phoneNumber
    ){
        log.info("Request OTP by phone number: " + phoneNumber);
        if (!ReengUtils.isPhoneNumber(phoneNumber)) {
            throw new InvalidParameterException("phoneNumber " + phoneNumber + " wrong format <CountryCode><Phone> ");
        }

        authenService.requestOTPByPhone(phoneNumber,lang);

        SuccessResDto data = SuccessResDto.builder()
                .message("Request OTP Success")
                .build();
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/login/otp")
    public ResponseEntity<?> loginByPhone(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("timestamp") long timestamp,
            @RequestParam("phoneNumber") @NotBlank String phoneNumber,
            @RequestParam("otp") @NotBlank @Size(min = 4, message = "OTP = 4 digit") String otp
    ){
        log.info("Request loginByPhone: phoneNumber:{}, {}", phoneNumber, otp);
        if(!ReengUtils.isPhoneNumber(phoneNumber)) {
            throw new InvalidParameterException("phoneNumber " + phoneNumber + " wrong format <CountryCode><Phone> ");
        }

        UserDto data = authenService.loginByPhone(phoneNumber, otp);
        return ResponseEntity.ok().body(SuccessResDto.builder()
                .data(data)
                .build());
    }

    @PostMapping("/login/email")
    public ResponseEntity<?> loginByEmail(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("email") @Email String email,
            @RequestParam("password") @NotBlank @Size(min = 8, message = "Password > 8 digit") String password
            ){
        log.info("Request loginByEmail: email:{}", email);
        UserDto userDto = authenService.loginByEmail(email, password);
        SuccessResDto data = SuccessResDto.builder()
                .message("Login By Email Success")
                .data(userDto)
                .build();
        return ResponseEntity.ok().body(data);
    }


    @PostMapping("/regis")
    public ResponseEntity<?> registrationUser(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("timestamp") long timestamp,
            @RequestParam("username") @NotBlank String username,
            @RequestParam("email") @Email String email,
            @RequestParam("phoneNumber") @NotBlank String phoneNumber,
            @RequestParam("password") @NotBlank @Size(min = 8, message = "Password > 8 digit") String password
    ){
        log.info("Request registration phoneNumber={}, email={}", phoneNumber, email);
//        if(!ReengUtils.isPhoneNumber(phoneNumber)) {
//            throw new InvalidParameterException("phoneNumber " + phoneNumber + " wrong format <CountryCode><Phone> ");
//        }
        User user = new User();
        user.setUserName(username);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPassword(password);

        log.info("Request registration User: {}", user);

        SuccessResDto data = SuccessResDto.builder()
                .message("Registration Success")
                .data(authenService.registrationUser(user))
                .build();

        return ResponseEntity.ok().body(data);
    }



    @PostMapping("/forgot")
    public ResponseEntity<?> sendTokenResetPassword(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("timestamp") long timestamp,
            @RequestParam("email") @Email String email
    ){
        log.info("Request Send Token Reset Password: email:{}", email);

        Boolean check = authenService.sendTokenResetPassword(email, lang);
        Map<String, Object> data = new HashMap<>();
        data.put("check", check);

        return ResponseEntity.ok().body(SuccessResDto.builder()
                .message("Send Token Reset Password")
                .data(data)
                .build());
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("timestamp") long timestamp,
            @RequestParam("email") @Email String email,
            @RequestParam("password") @NotBlank(message = "password not blank")
                @Size(min = 8, message = "password > 8 digit") String password,
            @RequestParam("token") @NotNull Integer tokenReset
    ){
        log.info("Request Reset Password: email:{}", email);
        Map<String, Object> data = authenService.resetPassword(email, password, tokenReset);

        return ResponseEntity.ok().body(SuccessResDto.builder()
                .message("Reset Password Success")
                .data(data)
                .build());
    }

    @PostMapping("/change-pass")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("timestamp") long timestamp,
            @RequestParam("email") @Email String email,
            @RequestParam("oldpass") @NotBlank(message = "password not blank")
                @Size(min = 8, message = "password > 8 digit") String oldPassword,
            @RequestParam("newpass") @NotBlank(message = "password not blank")
                @Size(min = 8, message = "password > 8 digit") String newPassword
    ){
        log.info("Request change password: email:{}", email);
        Map<String, Object> data = authenService.changePassword(email, oldPassword, newPassword);
        return ResponseEntity.ok().body(SuccessResDto.builder()
                .message("Change Password Success")
                .data(data)
                .build());
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestBody Map<String, String> request) {
        log.info("Request Google Login");

        String credential = request.get("credential");
        if (credential == null || credential.isEmpty()) {
            throw new InvalidParameterException("Credential is required");
        }

        User user = authenService.googleLogin(credential);
        SuccessResDto data = SuccessResDto.builder()
                .message("Google Login Success")
                .data(user)
                .build();

        return ResponseEntity.ok().body(data);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestBody UpdateUserDto updateUserRequest  // Sử dụng DTO để nhận dữ liệu
    ) {
        log.info("Request update user: userId={}, userName={}, phoneNumber={}, avatar={}",
                updateUserRequest.getUserId(),
                updateUserRequest.getUserName(),
                updateUserRequest.getPhoneNumber(),
                updateUserRequest.getAvatar());

        // Kiểm tra định dạng phoneNumber nếu được gửi
//        if (updateUserRequest.getPhoneNumber() != null && !updateUserRequest.getPhoneNumber().isEmpty()
//                && !ReengUtils.isPhoneNumber(updateUserRequest.getPhoneNumber())) {
//            throw new InvalidParameterException("phoneNumber " + updateUserRequest.getPhoneNumber() + " wrong format <CountryCode><Phone>");
//        }

        // Gọi service để cập nhật thông tin
        User updatedUser = authenService.updateUser(
                updateUserRequest.getUserId(),
                updateUserRequest.getUserName(),
                updateUserRequest.getPhoneNumber(),
                updateUserRequest.getAvatar()
        );

        SuccessResDto data = SuccessResDto.builder()
                .message("Cập nhật thông tin thành công!")
                .data(updatedUser) // Trả về thông tin user đã cập nhật
                .build();

        return ResponseEntity.ok().body(data);
    }

}
