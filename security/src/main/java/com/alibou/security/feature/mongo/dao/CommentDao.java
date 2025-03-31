package com.alibou.security.feature.mongo.dao;

import com.alibou.security.feature.mongo.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentDao extends MongoRepository<Comment, String> {
    List<Comment> findByMovieCode(String movieCode, Sort sort);
}
