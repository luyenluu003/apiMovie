package com.alibou.security.feature.notification.service;

import com.alibou.security.feature.notification.dao.NotificationDao;
import com.alibou.security.feature.notification.model.Notification;
import com.alibou.security.feature.user.dao.UserDao;
import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SendNotificationJob implements Job {
    private NotificationDao notificationDao;
    private SimpMessagingTemplate messagingTemplate;
    private UserDao userDao;

    @Autowired
    public void setNotificationDao(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    @Autowired
    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void execute(JobExecutionContext context) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this); // Inject dependencies
        Long notificationId = context.getJobDetail().getJobDataMap().getLong("notificationId");
        log.info("Executing job for notificationId: {}", notificationId);
        Notification notification = notificationDao.findById(notificationId);
        if (notification == null) {
            log.error("Notification not found for ID: {}", notificationId);
            return;
        }

        Date now = new Date();
        log.info("Current time: {}, StartAt: {}, EndAt: {}", now, notification.getStartAt(), notification.getEndAt());
        if (notification.getStartAt().before(now) && (notification.getEndAt() == null || notification.getEndAt().after(now))) {
            if (notification.getUserId() == null) {
                List<String> allUsers = userDao.findAllUser().stream()
                        .map(User::getUserId)
                        .collect(Collectors.toList());
                log.info("Sending to all users: {}", allUsers);
                for (String userId : allUsers) {
                    messagingTemplate.convertAndSendToUser(userId, "/topic/notifications", notification);
                    log.info("Sent to: {}", userId);
                }
            } else {
                messagingTemplate.convertAndSendToUser(notification.getUserId(), "/topic/notifications", notification);
                log.info("Sent to: {}", notification.getUserId());
            }
        } else {
            log.warn("Notification not in valid time range");
        }
    }
}
