package com.alibou.security.api.v1.comment;

import com.alibou.security.feature.mongo.model.Comment;
import com.alibou.security.feature.mongo.service.CommentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add-comment")
    public ResponseEntity<Comment> addComment(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("movieCode") String movieCode,
            @RequestParam("userId") String userId,
            @RequestParam("content") String content,
            HttpServletRequest request) {
        log.info("Received addComment request for movieCode: {}, userId: {}, lang: {}",
                movieCode, userId, lang);

        try {
            log.debug("Calling CommentService.addComment with movieCode: {}, userId: {}, content: {}",
                    movieCode, userId, content);
            Comment comment = commentService.addComment(movieCode, userId, content);
            log.info("Comment added successfully for movieCode: {}, commentId: {}",
                    movieCode, comment.getCommentId());
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            log.error("Failed to add comment for movieCode: {}, userId: {}. Error: {}",
                    movieCode, userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/add-reply")
    public ResponseEntity<Comment> addReply(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("commentId") String commentId,
            @RequestParam("userId") String userId,
            @RequestParam("content") String content,
            HttpServletRequest request) {
        log.info("Received addReply request for commentId: {}, userId: {}, lang: {}",
                commentId, userId, lang);

        try {
            log.debug("Calling CommentService.addReply with commentId: {}, userId: {}, content: {}",
                    commentId, userId, content);
            Comment comment = commentService.addReply(commentId, userId, content);
            log.info("Reply added successfully for commentId: {}, replyId: {}",
                    commentId, comment.getReplies().get(comment.getReplies().size() - 1).getReplyId());
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            log.error("Failed to add reply for commentId: {}, userId: {}. Error: {}",
                    commentId, userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/all-comments")
    public ResponseEntity<List<Comment>> getComments(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("movieCode") String movieCode,
            HttpServletRequest request) {
        log.info("Received getComments request for movieCode: {}, lang: {}", movieCode, lang);

        try {
            log.debug("Calling CommentService.getCommentsByMovieCode with movieCode: {}", movieCode);
            List<Comment> comments = commentService.getCommentsByMovieCode(movieCode);
            log.info("Retrieved {} comments for movieCode: {}", comments.size(), movieCode);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            log.error("Failed to get comments for movieCode: {}. Error: {}",
                    movieCode, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("Accept-language") String lang,
            @RequestParam String commentId,
            @RequestParam String userId ,
            HttpServletRequest request) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/delete-reply")
    public ResponseEntity<Void> deleteReply(
            @RequestHeader("Accept-language") String lang,

            @RequestParam String commentId,
            @RequestParam String replyId,
            @RequestParam String userId,
            HttpServletRequest request) {
        commentService.deleteReply(commentId, replyId, userId);
        return ResponseEntity.noContent().build();
    }
}