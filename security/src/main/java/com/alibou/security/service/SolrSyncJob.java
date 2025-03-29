package com.alibou.security.service;

import com.alibou.security.exrepos.solr.MovieAllActorRepository;
import com.alibou.security.exrepos.solr.MovieAllCategoryRepository;
import com.alibou.security.exrepos.solr.MovieSeriesRepository;
import com.alibou.security.exrepos.solr.entity.MovieActor;
import com.alibou.security.exrepos.solr.entity.MovieCategory;
import com.alibou.security.exrepos.solr.entity.MovieSeries;
import com.alibou.security.feature.actor.dao.ActorDao;
import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.category.dao.CategoryDao;
import com.alibou.security.feature.category.model.Category;
import com.alibou.security.feature.movie.dao.MovieDao;
import com.alibou.security.feature.movie.model.Movie;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Log4j2
public class SolrSyncJob {

    @Autowired
    private MovieSeriesRepository movieSeriesRepository;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private MovieAllActorRepository movieAllActorRepository;

    @Autowired
    private ActorDao actorDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private MovieAllCategoryRepository movieAllCategoryRepository;


    public SolrSyncJob(MovieDao movieDao, ActorDao actorDao, MovieAllActorRepository movieAllActorRepository,
                       MovieSeriesRepository movieSeriesRepository, CategoryDao categoryDao, MovieAllCategoryRepository movieAllCategoryRepository) {
        this.movieDao = movieDao;
        this.actorDao = actorDao;
        this.categoryDao = categoryDao;
        this.movieAllCategoryRepository = movieAllCategoryRepository;
        this.movieAllActorRepository = movieAllActorRepository;
        this.movieSeriesRepository = movieSeriesRepository;
    }

    @Scheduled(fixedRate = 3600000) // Chạy mỗi 1 tiếng
    public void syncMoviesActorToSolr() {
        log.info("🔄 Bắt đầu đồng bộ dữ liệu lên Solr...");

        List<Movie> movies = movieDao.getMovieAll();
        log.info("movies class: " + movies.getClass().getName());

        List<Actor> actors = actorDao.getActorAll();
        log.info("actors class: " + actors.getClass().getName());

        List<Category> categories = categoryDao.getCategoryAll();
        log.info("categories class: " + categories.getClass().getName());

        Page<MovieActor> solrActorsPage = (Page<MovieActor>) movieAllActorRepository.findAll();
        List<MovieActor> solrActors = solrActorsPage.getContent(); // Lấy content từ Page

        Set<String> solrActorIds = solrActors.stream().map(MovieActor::getId).collect(Collectors.toSet());
        List<MovieActor> missingActor = actors.stream()
                .filter(actor -> !solrActorIds.contains(actor.getId().toString())) // Lọc actor chưa có trên Solr
                .map(actor -> {
                    MovieActor movieActor = new MovieActor();
                    movieActor.setId(actor.getId().toString());
                    movieActor.setContentId(actor.getActorCode());
                    movieActor.setAvatar(actor.getAvatar());
                    movieActor.setContentName(actor.getBio());
                    movieActor.setName(actor.getName());
                    return movieActor;
                }).collect(Collectors.toList());

        if (!missingActor.isEmpty()) {
            movieAllActorRepository.saveAll(missingActor);
            log.info("✅ Đã đồng bộ " + missingActor.size() + " actor vào Solr!");
        } else {
            log.info("✅ Không có dữ liệu Actor mới cần đồng bộ.");
        }

        Page<MovieCategory> solrCategoryPage = (Page<MovieCategory>) movieAllCategoryRepository.findAll();
        List<MovieCategory> solrCategorys = solrCategoryPage.getContent(); // Lấy content từ Page


        Set<String> solrCategoryIds = solrCategorys.stream().map(MovieCategory::getId).collect(Collectors.toSet());
        List<MovieCategory> missingCategory = categories.stream()
                .filter(category -> !solrCategoryIds.contains(category.getId().toString())) // Lọc category chưa có trên Solr
                .map(category -> {
                    MovieCategory movieCategory = new MovieCategory();
                    movieCategory.setId(category.getId().toString());
                    movieCategory.setContentId(category.getCategoryCode());
                    movieCategory.setAvatar("");
                    movieCategory.setContentName(category.getDescription());
                    movieCategory.setName(category.getName());
                    return movieCategory;
                }).collect(Collectors.toList());

        if (!missingCategory.isEmpty()) {
            movieAllCategoryRepository.saveAll(missingCategory);
            log.info("✅ Đã đồng bộ " + missingCategory.size() + " category vào Solr!");
        } else {
            log.info("✅ Không có dữ liệu Category mới cần đồng bộ.");
        }


        // Lấy dữ liệu từ Solr
        Page<MovieSeries> solrMoviesPage = (Page<MovieSeries>) movieSeriesRepository.findAll();
        List<MovieSeries> solrMovies = solrMoviesPage.getContent(); // Lấy content từ Page

        Set<String> solrMovieIds = solrMovies.stream()
                .map(MovieSeries::getId)
                .collect(Collectors.toSet());

        List<MovieSeries> missingMovies = movies.stream()
                .filter(movie -> !solrMovieIds.contains(movie.getId().toString())) // Lọc phim chưa có trên Solr
                .map(movie -> {
                    MovieSeries movieSeries = new MovieSeries();
                    movieSeries.setId(movie.getId().toString());
                    movieSeries.setContentId(movie.getMovieCode());
                    movieSeries.setAvatar(movie.getImageUrl());
                    movieSeries.setContentName(movie.getDescription());
                    movieSeries.setName(movie.getMovieName());
                    return movieSeries;
                }).collect(Collectors.toList());

        if (!missingMovies.isEmpty()) {
            movieSeriesRepository.saveAll(missingMovies);
            log.info("✅ Đã đồng bộ " + missingMovies.size() + " phim vào Solr!");
        } else {
            log.info("✅ Không có dữ liệu Movie mới cần đồng bộ.");
        }
    }


}
