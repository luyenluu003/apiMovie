package com.alibou.security.feature.movie.service;

import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.movie.model.Movie;

import java.util.List;

public interface MovieService {

    List<MovieDto> findAllMovies(Integer page, Integer pageSize, String userId, Boolean type);

    List<MovieDto> findAllMoviesIsHot(Integer page, Integer pageSize, String userId, Integer isHot);

    List<MovieDto> findAllMoviesIsVip(Integer page, Integer pageSize, String userId, Integer isVip);

    Movie findMovieByMovieCode(String userId, String movieCode);

    List<Movie> getMovieByListId(String userId, List<String> listId, long timestamp, String id);

    void clearAllMoviesCache();

    List<MovieDto> findAllMovieByCategoryId(String userId, String categoryId, Boolean type, Integer page, Integer pageSize);

    List<MovieDto> findAllMovieByCategoryIdHot(String userId, String categoryId, Integer isHot, Integer page, Integer pageSize);

    List<MovieDto> findAllMovieByCategoryIdVip(String userId, String categoryId, Integer isVip, Integer page, Integer pageSize);

}
