package com.alibou.security.feature.authen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageOTP implements Serializable {
    private static final long serialVersionUID = 8450046700726290047L;
    private String sendTo;
    private String otp;
    private String lang;
}
