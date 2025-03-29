package com.alibou.security.utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

@Log4j2
public class ReengUtils {
    public static final ThreadLocal<SimpleDateFormat> getFormatYyyyMMddHHmmss = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static String encryptSHA256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for(int i =0 ; i < hash.length ; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    };

    public static String encryptMD5(String str) {
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());

            byte[] byteData = digest.digest();
            StringBuffer sb = new StringBuffer();
            for(int i =0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (EnumConstantNotPresentException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static boolean isPhoneNumber (String phoneNumber) {
        try{
            phoneNumber = phoneNumber.replace(" ", "_");
            if(!phoneNumber.startsWith("\\+")){
                phoneNumber = "+" + phoneNumber;
            }
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(phoneNumber, "VN");
            return phoneUtil.isValidNumber(swissNumberProto);
        } catch (Exception e) {
            return false;
        }
    }
}
