package com.alibou.security.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessResDto {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Builder.Default
    private Date serverTime = new Date();
    private String message;
    private Object data;
}
