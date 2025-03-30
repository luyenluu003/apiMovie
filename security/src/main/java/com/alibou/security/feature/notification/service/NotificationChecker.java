package com.alibou.security.feature.notification.service;

import com.alibou.security.feature.notification.dao.NotificationDao;
import com.alibou.security.feature.notification.model.Notification;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationChecker {
    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private NotificationService notificationService;

    private Set<Long> scheduledNotificationIds = new HashSet<>();

    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi phút
    public void checkNewNotifications() throws SchedulerException {
        List<Notification> activeNotifications = notificationDao.findByStatus(1);
        for (Notification notification : activeNotifications) {
            if (!scheduledNotificationIds.contains(notification.getId())) {
                notificationService.scheduleNotification(notification);
                scheduledNotificationIds.add(notification.getId());
            }
        }
    }

    // Khôi phục lịch khi khởi động
    @PostConstruct
    public void initialize() throws SchedulerException {
        List<Notification> activeNotifications = notificationDao.findByStatus(1);
        for (Notification notification : activeNotifications) {
            if (!scheduledNotificationIds.contains(notification.getId())) {
                notificationService.scheduleNotification(notification);
                scheduledNotificationIds.add(notification.getId());
            }
        }
    }
}
