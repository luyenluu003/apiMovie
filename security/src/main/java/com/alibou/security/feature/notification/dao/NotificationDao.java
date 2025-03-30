package com.alibou.security.feature.notification.dao;

import com.alibou.security.feature.notification.model.Notification;

import java.util.List;

public interface NotificationDao {
    List<Notification> findByStatus(Integer status);
    List<Notification> findByUserIdOrUserIdIsNull(String userId);

    Notification findById(Long id);

}
