package com.alibou.security.feature.mongo.service;

import com.alibou.security.feature.mongo.dao.CommentDao;
import com.alibou.security.feature.mongo.model.Comment;
import com.alibou.security.feature.mongo.model.CommentReply;
import com.alibou.security.feature.user.dao.UserDao;
import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Comment addComment(String movieCode, String userId, String content) {
        log.info("Starting addComment for movieCode: {}, userId: {}", movieCode, userId);

        try {
            log.debug("Fetching user with userId: {}", userId);
            User user = userDao.userFindByuserId(userId);
            if (user == null) {
                log.error("User not found for userId: {}", userId);
                throw new RuntimeException("User not found");
            }
            log.debug("User found: {}", user.getUserName());

            log.info("Building new comment for movieCode: {}", movieCode);
            Comment comment = Comment.builder()
                    .id(UUID.randomUUID().toString())
                    .commentId(UUID.randomUUID().toString())
                    .movieCode(movieCode)
                    .userId(userId)
                    .username(user.getUserName())
                    .content(content)
                    .avatar(user.getAvatar())
                    .commentAt(new Date())
                    .build();
            log.debug("Comment built: {}", comment);

            log.info("Saving comment to database");
            Comment savedComment = commentDao.save(comment);
            log.info("Comment saved successfully with id: {}", savedComment.getId());

            return savedComment;
        } catch (Exception e) {
            log.error("Error while adding comment for movieCode: {}, userId: {}. Error: {}",
                    movieCode, userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Comment addReply(String commentId, String userId, String content) {
        log.info("Starting addReply for commentId: {}, userId: {}", commentId, userId);

        try {
            log.debug("Fetching user with userId: {}", userId);
            User user = userDao.userFindByuserId(userId);
            if (user == null) {
                log.error("User not found for userId: {}", userId);
                throw new RuntimeException("User not found");
            }
            log.debug("User found: {}", user.getUserName());

            log.debug("Fetching comment with commentId: {}", commentId);
            Comment comment = commentDao.findById(commentId)
                    .orElseThrow(() -> {
                        log.error("Comment not found for commentId: {}", commentId);
                        return new RuntimeException("Comment not found");
                    });
            log.debug("Comment found: {}", comment.getContent());

            log.info("Building new reply for commentId: {}", commentId);
            CommentReply reply = CommentReply.builder()
                    .replyId(UUID.randomUUID().toString())
                    .userId(userId)
                    .username(user.getUserName())
                    .content(content)
                    .avatar(user.getAvatar())
                    .replyAt(new Date())
                    .build();
            log.debug("Reply built: {}", reply);

            log.info("Adding reply to comment with id: {}", commentId);
            comment.getReplies().add(reply);

            log.info("Saving updated comment to database");
            Comment updatedComment = commentDao.save(comment);
            log.info("Comment updated successfully with new reply, id: {}", updatedComment.getId());

            return updatedComment;
        } catch (Exception e) {
            log.error("Error while adding reply for commentId: {}, userId: {}. Error: {}",
                    commentId, userId, e.getMessage(), e);
            throw e; // Ném lại exception để controller xử lý
        }
    }

    @Override
    public List<Comment> getCommentsByMovieCode(String movieCode) {
        log.info("Starting getCommentsByMovieCode for movieCode: {}", movieCode);

        try {
            log.debug("Fetching comments from database for movieCode: {}", movieCode);
            List<Comment> comments = commentDao.findByMovieCode(movieCode, Sort.by(Sort.Direction.DESC, "commentAt"));

            log.info("Found {} comments for movieCode: {}", comments.size(), movieCode);
            log.debug("Comments retrieved: {}", comments);

            return comments;
        } catch (Exception e) {
            log.error("Error while fetching comments for movieCode: {}. Error: {}",
                    movieCode, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteComment(String commentId, String userId) {
        log.info("Starting deleteComment for commentId: {}, userId: {}", commentId, userId);

        try {
            log.debug("Fetching comment with commentId: {}", commentId);
            Comment comment = commentDao.findById(commentId)
                    .orElseThrow(() -> {
                        log.error("Comment not found for commentId: {}", commentId);
                        return new RuntimeException("Comment not found");
                    });

            // Kiểm tra quyền xóa: chỉ người tạo bình luận mới được xóa
            if (!comment.getUserId().equals(userId)) {
                log.error("User {} is not authorized to delete comment {}", userId, commentId);
                throw new RuntimeException("Unauthorized to delete this comment");
            }

            log.info("Deleting comment with id: {}", commentId);
            commentDao.delete(comment);
            log.info("Comment deleted successfully for commentId: {}", commentId);
        } catch (Exception e) {
            log.error("Error while deleting comment for commentId: {}, userId: {}. Error: {}",
                    commentId, userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteReply(String commentId, String replyId, String userId) {
        log.info("Starting deleteReply for commentId: {}, replyId: {}, userId: {}", commentId, replyId, userId);

        try {
            log.debug("Fetching comment with commentId: {}", commentId);
            Comment comment = commentDao.findById(commentId)
                    .orElseThrow(() -> {
                        log.error("Comment not found for commentId: {}", commentId);
                        return new RuntimeException("Comment not found");
                    });

            // Tìm reply trong danh sách replies
            CommentReply reply = comment.getReplies().stream()
                    .filter(r -> r.getReplyId().equals(replyId))
                    .findFirst()
                    .orElseThrow(() -> {
                        log.error("Reply not found for replyId: {} in commentId: {}", replyId, commentId);
                        return new RuntimeException("Reply not found");
                    });

            // Kiểm tra quyền xóa: chỉ người tạo reply mới được xóa
            if (!reply.getUserId().equals(userId)) {
                log.error("User {} is not authorized to delete reply {} in comment {}", userId, replyId, commentId);
                throw new RuntimeException("Unauthorized to delete this reply");
            }

            log.info("Removing reply with replyId: {} from commentId: {}", replyId, commentId);
            comment.getReplies().remove(reply);

            log.info("Saving updated comment to database");
            commentDao.save(comment);
            log.info("Reply deleted successfully for replyId: {} in commentId: {}", replyId, commentId);
        } catch (Exception e) {
            log.error("Error while deleting reply for commentId: {}, replyId: {}, userId: {}. Error: {}",
                    commentId, replyId, userId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public  Integer totalCommentCount(String movieCode){
        List<Comment> comments = commentDao.findByMovieCode(movieCode, Sort.by(Sort.Direction.DESC, "commentAt"));
        Integer total = comments.size();
        for(Comment comment : comments){
            total += comment.getReplies().size();
        }

        return total;
    }
}