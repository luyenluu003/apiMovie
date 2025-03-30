package com.alibou.security.api.v1.notification;

import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.notification.model.Notification;
import com.alibou.security.feature.notification.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/v1/noti")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification")
    @ResponseBody
    public ResponseEntity<?> getNotiUser(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();
        log.info("[MOVIE]:" + "userId=" + userId );

        List<Notification> notifications = notificationService.getNotificationsForUser(userId);

        // Kiểm tra nếu không có dữ liệu
        if (notifications == null || notifications.isEmpty()) {
            log.info("[Notifications]:" + "No movies found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/allnotification")
    @ResponseBody
    public ResponseEntity<?> getAllNuti(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();
        log.info("[MOVIE]:" + "userId=" + userId );

        List<Notification> notifications = notificationService.getNotificationsAll(userId);

        // Kiểm tra nếu không có dữ liệu
        if (notifications == null || notifications.isEmpty()) {
            log.info("[Notifications]:" + "No movies found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[getVideoInfo]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(notifications);
    }
}
