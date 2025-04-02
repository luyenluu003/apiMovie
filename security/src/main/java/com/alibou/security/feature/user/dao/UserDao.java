package com.alibou.security.feature.user.dao;

import com.alibou.security.feature.user.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAllUser();

    User userFindByuserId(String userId);

    Integer updateUserVip(User user);

    Integer updateUser(User user);


}
