package com.alibou.security.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MovieConfiguration {

    @Value("${queue.movie.like}")
    private String queueMovieLike;

    @Value("${queue.movie.share")
    private String queueMovieShare;

    @Value("${queue.movie.comment")
    private String queueMovieComment;

    @Value("${queue.movie.like.comment}")
    private String queueMovieLikeComment;

    @Value("${queue.movie.download}")
    private String queueMovieDownload;

    @Value("${queue.movie.watch")
    private String queueMovieWatch;

    @Value("${queue.movie.delete.comment")
    private String queueMovieDeleteComment;

    @Value("${queue.movie.delete.comment}")
    private String jwtSecretKey;

}
