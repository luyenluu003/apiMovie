package com.alibou.security.api.v1.dto.authen;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserDto {
    private static final long serialVersionUID = -1114155469057944671L;
    private String userId;
    private String userName;
    private String phoneNumber;
    private String avatar;

}
