package com.alibou.security.feature.authen.dao;

import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
@Log4j2
public class AuthenDaoImpl implements AuthenDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public User checkUserByLoginId(String loginId) {
        String sql = "SELECT user_id, login_id, base_on, phone_number, email, password, otp, otp_expired, " +
                    " username, last_avatar, active, vip_level, vip_start_date, vip_end_date, avatar " +
                    "FROM users " +
                    "WHERE login_id = :loginId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("loginId", loginId);
        return jdbcTemplate.query(sql, params,
                rs ->{
                    if(rs.next()) {
                        return User.builder()
                                .userId(rs.getString("user_id"))
                                .loginId(rs.getString("login_id"))
                                .baseOn(rs.getString("base_on"))
                                .phoneNumber(rs.getString("phone_number"))
                                .email(rs.getString("email"))
                                .password(rs.getString("password"))
                                .otp(rs.getString("otp"))
                                .otpExpired(rs.getTimestamp("otp_expired"))
                                .userName(rs.getString("username"))
                                .lastAvatar(rs.getLong("last_avatar"))
                                .active(rs.getInt("active"))
                                .vipLevel(rs.getString("vip_level"))
                                .vipStartDate(rs.getDate("vip_start_date"))
                                .vipEndDate(rs.getDate("vip_end_date"))
                                .avatar(rs.getString("avatar"))
                                .build();
                    }
                    return null;
                });
    }

    @Override
    public User checkUserByPhone(String phoneNumber) {
        String sql = "SELECT user_id, login_id, base_on, phone_number, email, password, otp, otp_expired, " +
                    "username, last_avatar, active " +
                    "FROM users " +
                    "WHERE phone_number = :phoneNumber";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("phoneNumber", phoneNumber);
        return jdbcTemplate.query(sql,params,
                rs -> {
                    if(rs.next()) {
                        return User.builder()
                                .userId(rs.getString("user_id"))
                                .loginId(rs.getString("login_id"))
                                .baseOn(rs.getString("base_on"))
                                .phoneNumber(rs.getString("phone_number"))
                                .email(rs.getString("email"))
                                .password(rs.getString("password"))
                                .otp(rs.getString("otp"))
                                .otpExpired(rs.getTimestamp("otp_expired"))
                                .userName(rs.getString("username"))
                                .lastAvatar(rs.getLong("last_avatar"))
                                .active(rs.getInt("active"))
                                .build();
                    }
                    return null;
                });
    }

    @Override
    public User getUserByUserId(String userId) {
        try{
            String sql = "SELECT user_id, base_on, token, email, phone_number, password, username, last_avatar, active, vip_level, vip_Start_date, vip_end_date, " +
                        "FROM users " +
                        "WHERE user_id = :userId";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);
            return jdbcTemplate.query(sql, params,
                    rs -> {
                        if(rs.next()) {
                            return User.builder()
                                    .userId(rs.getString("user_id"))
                                    .baseOn(rs.getString("base_on"))
                                    .token(rs.getString("token"))
                                    .email(rs.getString("email"))
                                    .phoneNumber(rs.getString("phone_number"))
                                    .userName(rs.getString("user_name"))
                                    .lastAvatar(rs.getLong("last_avatar"))
                                    .password(rs.getString("password"))
                                    .active(rs.getInt("active"))
                                    .vipLevel(rs.getString("vip_level"))
                                    .vipEndDate(rs.getDate("vip_end_date"))
                                    .vipStartDate(rs.getDate("vip_start_date"))
                                    .build();
                        }
                        return null;
                    });
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public Integer saveUserInfo(User user){
        try{
            String sql = "INSERT INTO users(user_id, login_id, base_on, " +
                        "password, token, username, last_avatar, avatar, " +
                        "active, token_expired, created_at, updated_at, uuid, " +
                        "regid, v_regid, last_login, otp, otp_expired, email, phone_number) " +
                        "VALUES (:user_id, :login_id, :base_on, " +
                        ":password, :token, :username, :last_avatar, :avatar, " +
                        ":active, :token_expired, :created_at, :updated_at, :uuid, " +
                        ":regid, :v_regid, :last_login, :otp, :otp_expired, :email, :phone_number) ";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("user_id", user.getUserId())
                    .addValue("login_id", user.getLoginId())
                    .addValue("base_on", user.getBaseOn())
                    .addValue("password", user.getPassword())
                    .addValue("token", user.getToken())
                    .addValue("username", user.getUserName())
                    .addValue("last_avatar", user.getLastAvatar())
                    .addValue("avatar", user.getAvatar())
                    .addValue("active", user.getActive())
                    .addValue("token_expired", user.getTokenExpired())
                    .addValue("created_at", new Date())
                    .addValue("updated_at", user.getUpdatedAt())
                    .addValue("uuid", user.getUuid())
                    .addValue("regid", user.getRegid())
                    .addValue("v_regid", user.getVRegid())
                    .addValue("last_login", new Date())
                    .addValue("otp", user.getOtp())
                    .addValue("otp_expired", user.getOtpExpired())
                    .addValue("email", user.getEmail())
                    .addValue("phone_number", user.getPhoneNumber());

            return jdbcTemplate.update(sql, params);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public Integer updateUserInfo(User user){
        try{
            String sql = "UPDATE users " +
                    "SET token = :token, " +
                    "username = :username, " +
                    "avatar = :avatar, " +
                    "active = :active, " +
                    "email = :email, " +
                    "phone_number = :phone_number, " +
                    "WHERE user_id = :user_id";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("token", user.getToken())
                    .addValue("username",user.getUserName())
                    .addValue("avatar",user.getAvatar())
                    .addValue("active",user.getActive())
                    .addValue("email",user.getEmail())
                    .addValue("phone_number",user.getPhoneNumber())
                    .addValue("user_id",user.getUserId());
            return jdbcTemplate.update(sql, params);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public Integer updateOtp(String userId, String otp, Date otpExpired) {
        try{
            String sql = "UPDATE users " +
                    "SET otp = :otp, otp_expired = :expired " +
                    "WHERE user_id = :user_id";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("user_id", userId)
                    .addValue("otp", otp)
                    .addValue("expired", otpExpired);
            return jdbcTemplate.update(sql, params);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public User getOtpByUserId(String userId) {
        try{
            String sql = "SELECT user_id, phone_number, email, username, last_avatar, otp, otp_expired " +
                        "FROM users " +
                        "WHERE user_id = :user_id";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("user_id", userId);
            return jdbcTemplate.query(sql, namedParameters,
                    rs ->{
                        if(rs.next()){
                            return User.builder()
                                    .userId(rs.getString("user_id"))
                                    .email(rs.getString("email"))
                                    .phoneNumber(rs.getString("phone_number"))
                                    .userName(rs.getString("username"))
                                    .lastAvatar(rs.getLong("last_avatar"))
                                    .otp(rs.getString("otp"))
                                    .otpExpired(new Date(rs.getTimestamp("otp_expired").getTime()))
                                    .build();
                        }
                        return null;
                    });
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public Integer updateToken(User user){
        try{
            String sql = "UPDATE users " +
                    "SET token = :token, token_expired = :tokenExpired, active = :active " +
                    "WHERE user_id = :user_id";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("token", user.getToken())
                    .addValue("tokenExpired", user.getTokenExpired())
                    .addValue("active", user.getActive())
                    .addValue("user_id", user.getUserId());
            return jdbcTemplate.update(sql, namedParameters);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return 0;
    }

    @Override
    public Integer updateTokenForgotPassword(String email, String link, Date date){
        try{
            String sql = "UPDATE users SET link_reset_password =:link, " +
                    "token_reset_password_expired = :date_expired " +
                    "WHERE email = :email";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("link", link)
                    .addValue("date_expired", date)
                    .addValue("email", email);
            return jdbcTemplate.update(sql, namedParameters);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return 0;
    }

    @Override
    public Map<String, Object> getTokenForgotPassword(String email){
        try{
            String sql = "SELECT link_reset_password, token_reset_password_expired " +
                        "FROM users " +
                    "WHERE (email = :email)";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("email", email);
            return jdbcTemplate.query(sql,namedParameters,rs->{
                rs.next();
                Map<String,Object> map = new HashMap<>();
                map.put("link",rs.getString("link_reset_password"));
                map.put("date_expired",rs.getString("token_reset_password_expired"));
                return map;
            });
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public Integer updatePassword(String email, String password) {
        try{
            String sql = "UPDATE users SET PASSWORD = :password " +
                        "WHERE (email = :email)";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("password", password)
                    .addValue("email", email);
            return jdbcTemplate.update(sql, namedParameters);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public Integer updateTimeOtp(String phoneNumber, Date date) {
        try{
            String sql = "UPDATE users SET otp_expired = :expired " +
                    "WHERE (phone_number = :phone_number)";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("expired", date)
                    .addValue("phone_number", phoneNumber);
            return jdbcTemplate.update(sql, namedParameters);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public Integer updateTimeTokenResetPass(String email, Date date) {
        try{
            String sql = "UPDATE users SET token_reset_password_expired = :expired "+
                    "WHERE (email = :email)";
            SqlParameterSource namedParameters = new MapSqlParameterSource()
                    .addValue("expired", date)
                    .addValue("email", email);
            return jdbcTemplate.update(sql, namedParameters);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public User getPassword(String email) {
        try {
            String sql = "SELECT password FROM users where email = :email;";
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("email", email);
            return jdbcTemplate.query(sql, params, rs -> {
                if (rs.next()) {
                    return User.builder()
                            .password(rs.getString("password"))
                            .build();
                }
                return null;
            });
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

}
