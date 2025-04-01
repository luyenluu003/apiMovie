package com.alibou.security.feature.mongo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReply {
    private String replyId;

    private String userId;

    private String username;

    private String content;

    @Nullable
    private String avatar;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date replyAt;

    private List<String> likes = new ArrayList<>(); // Likes cho reply
    private Map<String, List<String>> reactions = new HashMap<>();
}
