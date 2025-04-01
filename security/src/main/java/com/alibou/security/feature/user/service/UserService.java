package com.alibou.security.feature.user.service;

import com.alibou.security.feature.user.model.User;

public interface UserService {

    void activateVip(String userId, Integer days, String vip);

    Boolean isVipActive(String userId);


}
