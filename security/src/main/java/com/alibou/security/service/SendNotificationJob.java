package com.alibou.security.service;

import com.alibou.security.feature.notification.dao.NotificationDao;
import com.alibou.security.feature.notification.model.Notification;
import com.alibou.security.feature.user.dao.UserDao;
import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//@Log4j2
//public class SendNotificationJob implements Job {
//
//    @Autowired
//    private NotificationDao notificationDao;
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    private UserDao userDao;
//
//    @Override
//    public void execute(JobExecutionContext context) {
//        Long notificationId = context.getJobDetail().getJobDataMap().getLong("notificationId");
//        log.info("Executing job for notificationId: {}", notificationId);
//        Notification notification = notificationDao.findById(notificationId);
//        if (notification == null) {
//            log.error("Notification not found for ID: {}", notificationId);
//            return;
//        }
//
//        Date now = new Date();
//        log.info("Current time: {}, StartAt: {}, EndAt: {}", now, notification.getStartAt(), notification.getEndAt());
//        if (notification.getStartAt().before(now) && (notification.getEndAt() == null || notification.getEndAt().after(now))) {
//            if (notification.getUserId() == null) {
//                List<String> allUsers = userDao.findAllUser().stream()
//                        .map(User::getEmail)
//                        .collect(Collectors.toList());
//                log.info("Sending to all users: {}", allUsers);
//                for (String userId : allUsers) {
//                    messagingTemplate.convertAndSendToUser(userId, "/topic/notifications", notification);
//                    log.info("Sent notification to user: {}", userId);
//                }
//            } else {
//                messagingTemplate.convertAndSendToUser(notification.getUserId(), "/topic/notifications", notification);
//                log.info("Sent notification to user: {}", notification.getUserId());
//            }
//        } else {
//            log.warn("Notification not sent - out of time range");
//        }
//    }
//}
