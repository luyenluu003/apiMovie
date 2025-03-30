package com.alibou.security.feature.notification.service;

import com.alibou.security.feature.notification.dao.NotificationDao;
import com.alibou.security.feature.notification.model.Notification;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
@CacheConfig(cacheManager = "cacheManager3Hours")
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private Scheduler scheduler;


    public List<Notification> getNotificationsAll(String userId){
        List<Notification> notifications = notificationDao.findByStatus(1);

        if (notifications == null || notifications.isEmpty()) {
            log.warn("No notifications found: {}", userId);
            return Collections.emptyList();
        }

        return notifications.stream()
                .filter(Objects::nonNull) // ✅ Loại bỏ phần tử null nếu có
                .map(notification -> {
                    if (notification.getRelatedId() == null) {
                        log.warn("Notification ID {} has null relatedId", notification.getId());
                    }
                    return notification;
                })
                .collect(Collectors.toList());
    }

    public List<Notification> getNotificationsForUser(String userId) {
        List<Notification> notifications = notificationDao.findByUserIdOrUserIdIsNull(userId);

        if (notifications == null || notifications.isEmpty()) {
            log.warn("No notifications found for user: {}", userId);
            return Collections.emptyList();
        }

        return notifications.stream()
                .filter(Objects::nonNull) // ✅ Loại bỏ phần tử null nếu có
                .map(notification -> {
                    if (notification.getRelatedId() == null) {
                        log.warn("Notification ID {} has null relatedId", notification.getId());
                    }
                    return notification;
                })
                .collect(Collectors.toList());
    }


    public void scheduleNotification(Notification notification) {
        try {
            if (notification.getStatus() == 1 && notification.getStartAt() != null && notification.getStartAt().after(new Date())) {
                JobKey jobKey = new JobKey("notificationJob-" + notification.getId(), "notificationGroup");
                if (!scheduler.checkExists(jobKey)) {
                    JobDetail job = JobBuilder.newJob(SendNotificationJob.class)
                            .withIdentity(jobKey)
                            .usingJobData("notificationId", notification.getId())
                            .build();
                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("notificationTrigger-" + notification.getId(), "notificationGroup")
                            .startAt(notification.getStartAt())
                            .build();
                    scheduler.scheduleJob(job, trigger);
                } else {
                    log.info("Notification job {} is already scheduled.", jobKey);
                }
            }
        } catch (SchedulerException e) {
            log.error("Error scheduling notification: {}", e.getMessage(), e);
        }
    }

}
