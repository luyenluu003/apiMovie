package com.alibou.security.feature.mongo.service;

import com.alibou.security.feature.mongo.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(String movieCode, String userId, String content);
    Comment addReply(String commentId, String userId, String content);
    List<Comment> getCommentsByMovieCode(String movieCode);
    void deleteComment(String commentId, String userId);
    void deleteReply(String commentId, String replyId, String userId);
}
