package com.alibou.security.utils;

import lombok.extern.log4j.Log4j2;
import org.xxtea.XXTEA;
import java.util.concurrent.TimeUnit;

@Log4j2
public class AuthenticationUtil {
    public static final String TOKEN_KEY = ReengUtils.encryptSHA256("movieTokenKey");
    public static final String SECRET_CODE = "movieSecretKey";
    public static final long TOKEN_TIME_EXPIRED_SCALE = TimeUnit.SECONDS.toMillis(20);
    public static final long TOKEN_TIME_RENEW = TimeUnit.SECONDS.toMillis(20);
    public static final String SPLIT_SYMBOL = "@";

    public static Boolean isValidToken(String userId, String token, long timestamp){
        try{
            String plainText = decryptToken(token);
            String[] list = plainText.split(SPLIT_SYMBOL);
            String tokenUserId = tokenUserId(list);
            String tokenTimeExpiredStr = tokenTimeExpired(list);
            String secretCode = tokenSecretCode(list);
            if(isValidTokenUser(userId,tokenUserId) && isTokenValidAndRenewable(tokenTimeExpiredStr,timestamp)
             && isValidTokenSecretCode(secretCode)){
                return Boolean.TRUE;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Boolean.FALSE;
    }

    public static Boolean isTokenValidToRenew(String userId, String oldToken, long timestamp){
        try{
            String plainText = decryptToken(oldToken);
            String[] list = plainText.split(SPLIT_SYMBOL);
            String tokenUserId = tokenUserId(list);
            long tokenTimeExpired = Long.parseLong(tokenTimeExpired(list));

            if(isValidTokenUser(userId,tokenUserId) && isTokenTimeValidToRenew(tokenTimeExpired,timestamp)){
                return Boolean.TRUE;
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Boolean.FALSE;
    }

    public static Boolean isTokenTimeValidToRenew(long tokenTimeExpired, long timestamp){
        try{
            long tokenRestTime = timestamp - tokenTimeExpired;
            if(0 < tokenRestTime && tokenRestTime <= TOKEN_TIME_RENEW){
                return Boolean.TRUE;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Boolean.FALSE;
    }

    public static Boolean isValidTokenUser(String userId, String tokenUserId){
        try{
            if(userId.equals(tokenUserId))
                return Boolean.TRUE;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Boolean.FALSE;

    }

    public static boolean isTokenValidAndRenewable(String tokenTimeExpiredStr, long timestamp) {
        if (tokenTimeExpiredStr == null || tokenTimeExpiredStr.trim().isEmpty()) {
            log.error("Token time expired string is invalid");
            return false;
        }
        try {
            long tokenTimeExpired = Long.parseLong(tokenTimeExpiredStr);
            long tokenRestTime = tokenTimeExpired - timestamp;
            //check time: the time client request to server out of the token time expired
            // -x-----------------x------------------------x-----------------x-----------
            //  |                 |                        |                 |
            //  tokenTimeGen    timestamp            tokenExpiredTime          tokenTimeRenew
            return timestamp <= tokenTimeExpired && tokenRestTime <= TOKEN_TIME_RENEW;
        } catch (NumberFormatException ex) {
            log.error("Invalid token time format: " + ex.getMessage(), ex);
            return false;
        }
    }

    public static Boolean isValidTokenSecretCode(String secretCode){
        try{
            if(secretCode.equals(SECRET_CODE))
                return Boolean.TRUE;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return Boolean.FALSE;
    }

    public static String tokenTimeExpired(String[] list){
        try{
            return list[1];
        }catch(ArrayIndexOutOfBoundsException e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static String decryptToken(String token){
        try{
            return XXTEA.decryptBase64StringToString(token, TOKEN_KEY);
        }catch (ArrayIndexOutOfBoundsException aex){
            log.error(aex.getMessage(),aex);
        }
        return null;
    }

    public static String generateToken(String userId, long timeExpired, String secretCode){
        try{
            String plainText = String.join(SPLIT_SYMBOL, userId, String.valueOf(timeExpired), secretCode);
            return XXTEA.encryptToBase64String(plainText,TOKEN_KEY);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static String tokenUserId(String[] list){
        try{
            return list[0];
        }catch (ArrayIndexOutOfBoundsException aex){
            log.error(aex.getMessage(),aex);
        }
        return null;
    }

    public static String tokenSecretCode(String[] list){
        try{
            return list[2];
        }catch (ArrayIndexOutOfBoundsException aex){
            log.error(aex.getMessage(),aex);
        }
        return null;
    }

}
