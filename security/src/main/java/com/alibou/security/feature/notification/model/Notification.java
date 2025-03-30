package com.alibou.security.feature.notification.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification implements Serializable {
    private static final long serialVersionUID = 8744263620653686921L;

    private Long id;
    private String userId; // Có thể null nếu gửi đến tất cả
    private Integer status; // 0 là chờ duyệt , -1 là xóa, 1 là hoạt động
    private String message;
    private String title;
    private String imageUrl;
    private String relatedId;
    private Date createdAt;
    private Date updatedAt;
    private Date startAt;
    private Date endAt;
    private Long createdBy;
    private Long updatedBy;
    private Date approvedAt;
    private Long approvedBy;

}
