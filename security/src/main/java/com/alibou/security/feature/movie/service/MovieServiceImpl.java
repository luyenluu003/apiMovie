package com.alibou.security.feature.movie.service;

import com.alibou.security.api.v1.dto.episode.EpisodeDto;
import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.actor.dao.ActorDao;
import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.category.dao.CategoryDao;
import com.alibou.security.feature.category.model.Category;
import com.alibou.security.feature.episode.dao.EpisodeDao;
import com.alibou.security.feature.episode.model.Episode;
import com.alibou.security.feature.movie.dao.MovieDao;
import com.alibou.security.feature.movie.model.Movie;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@CacheConfig(cacheManager = "cacheManager3Hours")
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private EpisodeDao episodeDao;

    @Autowired
    private ActorDao actorDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    @Cacheable(value = "movies", key = "'list-movies:'.concat(#userId).concat(#type).concat(#page).concat(#pageSize)")
    public List<MovieDto> findAllMovies(Integer page, Integer pageSize, String userId, Boolean type) {
        log.info("FindAllMovie | HIT | userId={} | type={} | page={} | pageSize={}", userId, type, page, pageSize);
        String cacheKey = "list-movies:".concat(userId).concat(type.toString()).concat(page.toString()).concat(pageSize.toString());
        log.info("Cache Key: {}", cacheKey);

        List<Movie> movies = movieDao.getMovies(type, page, pageSize);

        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }

        return movies.stream().map(movie -> {
            MovieDto.MovieDtoBuilder builder = MovieDto.builder()
                    .categoryId(movie.getCategoryId())
                    .censorship(movie.getCensorship())
                    .description(movie.getDescription())
                    .duration(movie.getDuration())
                    .imageUrl(movie.getImageUrl())
                    .language(movie.getLanguage())
                    .movieCode(movie.getMovieCode())
                    .movieGenre(movie.getMovieGenre())
                    .movieName(movie.getMovieName())
                    .releaseDate(movie.getReleaseDate())
                    .status(movie.getStatus())
                    .type(movie.getType())
                    .userphone(movie.getUserphone())
                    .videoUrl(movie.getVideoUrl())
                    .isHot(movie.getIsHot());

            if (Boolean.TRUE.equals(movie.getType())) {
                log.info("Fetching episodes for movieCode: {}", movie.getMovieCode());
                List<Episode> episodes = episodeDao.getAllEpisodesByMovieCode(movie.getMovieCode());
                log.info("Episodes found: {}", episodes.size());
                builder.episodes(episodes);
            }

            return builder.build();
        }).collect(Collectors.toList());
    }



    @Override
    @Cacheable(value = "movies", key = "'list-movies-hot:'.concat(#userId).concat(#isHot).concat(#page).concat(#pageSize)")
    public List<MovieDto> findAllMoviesIsHot(Integer page, Integer pageSize, String userId, Integer isHot) {
        log.info("FindAllMovieIsHot | HIT | userId={} | isHot={} | page={} | pageSize={}", userId, isHot, page, pageSize);

        List<Movie> movies = movieDao.getMovieHots(isHot, page, pageSize);

        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }

        return movies.stream().map(movie -> {
            MovieDto.MovieDtoBuilder builder = MovieDto.builder()
                    .categoryId(movie.getCategoryId())
                    .censorship(movie.getCensorship())
                    .description(movie.getDescription())
                    .duration(movie.getDuration())
                    .imageUrl(movie.getImageUrl())
                    .language(movie.getLanguage())
                    .movieCode(movie.getMovieCode())
                    .movieGenre(movie.getMovieGenre())
                    .movieName(movie.getMovieName())
                    .releaseDate(movie.getReleaseDate())
                    .status(movie.getStatus())
                    .type(movie.getType())
                    .userphone(movie.getUserphone())
                    .videoUrl(movie.getVideoUrl())
                    .isHot(movie.getIsHot());

            if (Boolean.TRUE.equals(movie.getType())) {
                log.info("Fetching episodes for movieCode: {}", movie.getMovieCode());
                List<Episode> episodes = episodeDao.getAllEpisodesByMovieCode(movie.getMovieCode());
                log.info("Episodes found: {}", episodes.size());
                builder.episodes(episodes);
            }

            return builder.build();
        }).collect(Collectors.toList());
    }



    @Override
    public Movie findMovieByMovieCode(String userId, String movieCode) {
        log.info("Finding movie for userId={} with movieCode={}", userId, movieCode);

        Movie movie = movieDao.getMovieByMovieCode(movieCode);
        if (movie == null) {
            log.info("No movie found with movieCode={}", movieCode);
            return null;
        }

        try {
            if (Boolean.TRUE.equals(movie.getType())) {
                log.info("Retrieving episodes for series with movieCode={}", movieCode);
                List<Episode> episodes = episodeDao.getAllEpisodesByMovieCode(movieCode);
                movie.setEpisodes(episodes != null ? episodes : Collections.emptyList());
            } else {
                log.info("Processing single movie with movieCode={}", movieCode);
            }



            log.info("Retrieving actors for movieCode={}", movieCode);
            List<Actor> actors = actorDao.getAllActorByMovieCode(movieCode);
            movie.setActors(actors != null ? actors : Collections.emptyList());

//            log.info("Retrieving category for series with categoryId={}", movie.getCategoryId());
//            List<Category> categories = categoryDao.getAllCategoryByCategoryId(movie.getCategoryId());
//            Map<String, Object> movieData = new HashMap<>();
//            movieData.put("movie", movie);
//            movieData.put("categories", categories != null ? categories : Collections.emptyList());

            return movie;
        } catch (Exception e) {
            log.error("Error retrieving movie details for movieCode={}: {}", movieCode, e.getMessage());
            return null;
        }
    }

    @Override
    public List<Movie> getMovieByListId(String userId, List<String> listId, long timestamp, String id) {
        List<Movie> listMovie = movieDao.getMovieByListId(listId);

        return listMovie != null ? listMovie : Collections.emptyList();
    }

    @CacheEvict(value = "movies", allEntries = true)
    public void clearAllMoviesCache() {
        log.info("Clearing all movies cache for all users...");
    }

    @Override
    @Cacheable(value = "movies", key = "'list-movies-category:'.concat(#userId).concat(#categoryId).concat(#type).concat(#page).concat(#pageSize)")
    public List<MovieDto> findAllMovieByCategoryId(String userId, String categoryId, Boolean type, Integer page, Integer pageSize)  {
        log.info("FindAllMovieIsHot | HIT | userId={} | categoryId={} | page={} | pageSize={}", userId, categoryId, page, pageSize);

        List<Movie> movies = movieDao.getMovieByCategoryId(categoryId, type, page, pageSize);

        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }

        return movies.stream().map(movie ->
                MovieDto.builder()
                        .categoryId(movie.getCategoryId())
                        .censorship(movie.getCensorship())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .imageUrl(movie.getImageUrl())
                        .language(movie.getLanguage())
                        .movieCode(movie.getMovieCode())
                        .movieGenre(movie.getMovieGenre())
                        .movieName(movie.getMovieName())
                        .releaseDate(movie.getReleaseDate())
                        .status(movie.getStatus())
                        .type(movie.getType())
                        .userphone(movie.getUserphone())
                        .videoUrl(movie.getVideoUrl())
                        .isHot(movie.getIsHot())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "movies", key = "'list-movieHot-category:'.concat(#userId).concat(#categoryId).concat(#isHot).concat(#page).concat(#pageSize)")
    public List<MovieDto> findAllMovieByCategoryIdHot(String userId, String categoryId, Integer isHot, Integer page, Integer pageSize)  {
        log.info("FindAllMovieIsHot | HIT | userId={} | categoryId={} | page={} | pageSize={}", userId, categoryId, page, pageSize);

        List<Movie> movies = movieDao.getMovieByCategoryIdHot(categoryId, isHot, page, pageSize);

        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }

        return movies.stream().map(movie ->
                MovieDto.builder()
                        .categoryId(movie.getCategoryId())
                        .censorship(movie.getCensorship())
                        .description(movie.getDescription())
                        .duration(movie.getDuration())
                        .imageUrl(movie.getImageUrl())
                        .language(movie.getLanguage())
                        .movieCode(movie.getMovieCode())
                        .movieGenre(movie.getMovieGenre())
                        .movieName(movie.getMovieName())
                        .releaseDate(movie.getReleaseDate())
                        .status(movie.getStatus())
                        .type(movie.getType())
                        .userphone(movie.getUserphone())
                        .videoUrl(movie.getVideoUrl())
                        .isHot(movie.getIsHot())
                        .build()
        ).collect(Collectors.toList());
    }


}
