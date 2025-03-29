package com.alibou.security.service;

import com.alibou.security.exrepos.solr.MovieAllActorRepository;
import com.alibou.security.exrepos.solr.MovieAllCategoryRepository;
import com.alibou.security.exrepos.solr.MovieAllRepository;
import com.alibou.security.exrepos.solr.MovieSeriesRepository;
import com.alibou.security.exrepos.solr.entity.MovieActor;
import com.alibou.security.exrepos.solr.entity.MovieAll;
import com.alibou.security.exrepos.solr.entity.MovieCategory;
import com.alibou.security.exrepos.solr.entity.MovieSeries;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SolrService {

    @Autowired
    private MovieAllRepository movieAllRepository;

    @Autowired
    private MovieAllCategoryRepository movieAllCategoryRepository;

    @Autowired
    private MovieAllActorRepository movieAllActorRepository;

    @Autowired
    private MovieSeriesRepository movieSeriesRepository;

    public List<MovieAll> searchMovieAll(String userId, String q, Integer page, Integer size) {
        log.info("searchMovieAll userId|{}|{}|{}|{}", userId, q, page, size);
        try{
            Page<MovieAll> results = movieAllRepository.findContentByQuery(q, PageRequest.of(page, size));

            return results.get().collect(Collectors.toList());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<MovieSeries> searchMovieSeries(String userId, String q, Integer page, Integer size) {
        log.info("SearchMovieSeries userId|{}|{}|{}", userId, q, page, size);

        try{
            Page<MovieSeries> result = movieSeriesRepository.findContentByQuery(q, PageRequest.of(page, size));

            return result.get().collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<MovieActor> searchMovieActor(String userId, String q, Integer page, Integer size) {
        log.info("SearchMovieActor userId|{}|{}|{}", userId, q, page, size);

        try{
            Page<MovieActor> result = movieAllActorRepository.findContentByQuery(q, PageRequest.of(page, size));
            return result.get().collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<MovieCategory> searchMovieCategory(String userId, String q, Integer page, Integer size) {
        log.info("SearchMovieCategory userId|{}|{}|{}", userId, q, page, size);
        try{
            Page<MovieCategory> result = movieAllCategoryRepository.findContentByQuery(q, PageRequest.of(page, size));
            return result.get().collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
