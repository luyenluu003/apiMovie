package com.alibou.security.feature.mongo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movie_comment")
public class Comment {
    @Id
    public String id;

    @Field(value = "comment_id")
    private String commentId;

    @Field(value = "movie_code")
    private String movieCode;

    @Field(value = "user_id")
    private String userId;

    @Field(value = "username")
    private String username;

    @Field(value = "content")
    private String content;

    @Field(value = "avatar")
    @Nullable
    private String avatar;

    @Field(value = "commentAt")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date commentAt;

    @Field(value = "commentReplies")
    private List<CommentReply> replies = new ArrayList<>();

}
