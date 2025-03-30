package com.alibou.security.feature.notification.service;

import com.alibou.security.feature.notification.model.Notification;
import lombok.extern.log4j.Log4j2;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotificationsForUser(String userId);

    List<Notification> getNotificationsAll(String userId);

    void scheduleNotification(Notification notification);
}
