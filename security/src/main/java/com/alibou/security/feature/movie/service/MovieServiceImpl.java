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
                    .isHot(movie.getIsHot())
                    .isVip(movie.getIsVip())
                    .thumbnail(movie.getThumbnail());

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
                    .isHot(movie.getIsHot())
                    .isVip(movie.getIsVip())
                    .thumbnail(movie.getThumbnail());

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
    @Cacheable(value = "movies", key = "'list-movies-vip:'.concat(#userId).concat(#isVip).concat(#page).concat(#pageSize)")
    public List<MovieDto> findAllMoviesIsVip(Integer page, Integer pageSize, String userId, Integer isVip) {
        log.info("FindAllMovieIsHot | HIT | userId={} | isHot={} | page={} | pageSize={}", userId, isVip, page, pageSize);

        List<Movie> movies = movieDao.getMovieVips(isVip, page, pageSize);

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
                    .isHot(movie.getIsHot())
                    .isVip(movie.getIsVip())
                    .thumbnail(movie.getThumbnail());

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
    @Cacheable(value = "movies", key = "'list-movie-detail:'.concat(#userId).concat(#movieCode)")
    public Movie findMovieByMovieCode(String userId, String movieCode) {
        log.info("Tìm kiếm phim cho userId={} với movieCode={}", userId, movieCode);

        Movie movie = movieDao.getMovieByMovieCode(movieCode);
        if (movie == null) {
            log.info("Không tìm thấy phim với movieCode={}", movieCode);
            return null;
        }

        try {

            if (Boolean.TRUE.equals(movie.getType())) {
                log.info("Đang lấy danh sách tập cho series với movieCode={}", movieCode);
                List<Episode> episodes = episodeDao.getAllEpisodesByMovieCode(movieCode);
                movie.setEpisodes(episodes != null ? episodes : Collections.emptyList());
                log.info("Số tập tìm thấy: {}", movie.getEpisodes().size());
            } else {
                log.info("Xử lý phim lẻ với movieCode={}", movieCode);
            }

            log.info("Đang lấy danh sách diễn viên cho movieCode={}", movieCode);
            List<Actor> actors = actorDao.getAllActorByMovieCode(movieCode);
            movie.setActors(actors != null ? actors : Collections.emptyList());
            log.info("Số diễn viên tìm thấy: {}", movie.getActors().size());

            return movie;
        } catch (Exception e) {
            log.error("Lỗi khi lấy thông tin chi tiết cho movieCode={}: {}", movieCode, e.getMessage());
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
                        .isVip(movie.getIsVip())
                        .thumbnail(movie.getThumbnail())
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
                        .isVip(movie.getIsVip())
                        .thumbnail(movie.getThumbnail())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "movies", key = "'list-movieVip-category:'.concat(#userId).concat(#categoryId).concat(#isVip).concat(#page).concat(#pageSize)")
    public List<MovieDto> findAllMovieByCategoryIdVip(String userId, String categoryId, Integer isVip, Integer page, Integer pageSize)  {
        log.info("FindAllMovieIsHot | HIT | userId={} | categoryId={} | page={} | pageSize={}", userId, categoryId, page, pageSize);

        List<Movie> movies = movieDao.getMovieByCategoryIdVip(categoryId, isVip, page, pageSize);

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
                        .isVip(movie.getIsVip())
                        .thumbnail(movie.getThumbnail())
                        .build()
        ).collect(Collectors.toList());
    }


}
