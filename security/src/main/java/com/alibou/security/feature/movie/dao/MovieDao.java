package com.alibou.security.feature.movie.dao;

import com.alibou.security.feature.movie.model.Movie;

import java.util.List;

public interface MovieDao {
    List<Movie> getMovies(Boolean type, Integer page, Integer pageSize);

//    List<Movie> getAllEpisodes(Boolean type, Integer page, Integer pageSize);

    List<Movie> getMovieHots(Integer isHot, Integer page, Integer pageSize);

    List<Movie> getMovieVips(Integer isVip, Integer page, Integer pageSize);

    Movie getMovieByMovieCode(String movieCode);

    Movie getMovieByMovieId(String movieId);

    List<Movie> getMovieByListId(List<String> movieId);

    List<Movie> getMovieAll();

    List<Movie> getMovieByCategoryId(String categoryId, Boolean type, Integer page, Integer pageSize);

    List<Movie> getMovieByCategoryIdHot(String categoryId, Integer isHot, Integer page, Integer pageSize);

    List<Movie> getMovieByCategoryIdVip(String categoryId, Integer isVip, Integer page, Integer pageSize);

    List<Movie> findMoviesByFilters(List<String> genres, List<String> actors, Integer yearFrom, Integer yearTo);

}
