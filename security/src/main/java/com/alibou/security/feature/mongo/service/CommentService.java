package com.alibou.security.feature.mongo.service;

import com.alibou.security.feature.mongo.model.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(String movieCode, String userId, String content);
    Comment addReply(String commentId, String userId, String content);
    List<Comment> getCommentsByMovieCode(String movieCode);
    void deleteComment(String commentId, String userId);
    void deleteReply(String commentId, String replyId, String userId);
    Integer totalCommentCount(String movieCode);

    Comment likeComment(String commentId, String userId);
    Comment unlikeComment(String commentId, String userId);
    Comment addReactionToComment(String commentId, String userId, String reactionType);
    Comment removeReactionFromComment(String commentId, String userId, String reactionType);
    Comment likeReply(String commentId, String replyId, String userId);
    Comment unlikeReply(String commentId, String replyId, String userId);
    Comment addReactionToReply(String commentId, String replyId, String userId, String reactionType);
    Comment removeReactionFromReply(String commentId, String replyId, String userId, String reactionType);
}
