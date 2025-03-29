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
public class MessageSendPassword implements Serializable {
    private static final long serialVersionUID = 7429057236462414553L;
    private String address;

    private String token;

    private String lang;
}
