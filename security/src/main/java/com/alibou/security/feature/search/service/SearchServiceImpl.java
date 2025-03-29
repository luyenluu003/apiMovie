package com.alibou.security.feature.search.service;

import com.alibou.security.exrepos.solr.entity.MovieActor;
import com.alibou.security.exrepos.solr.entity.MovieAll;
import com.alibou.security.exrepos.solr.entity.MovieCategory;
import com.alibou.security.exrepos.solr.entity.MovieSeries;
import com.alibou.security.feature.actor.model.Actor;
import com.alibou.security.feature.actor.service.ActorService;
import com.alibou.security.feature.category.model.Category;
import com.alibou.security.feature.category.service.CategoryService;
import com.alibou.security.feature.movie.model.Movie;
import com.alibou.security.feature.movie.service.MovieService;
import com.alibou.security.feature.search.model.ResultGroup;
import com.alibou.security.feature.search.model.ResultItem;
import com.alibou.security.service.SolrService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrService solrService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public List<ResultGroup> searchAllContent(String userId, String qrTxt, String lang, long timestamp) {
        List<ResultGroup> listGroup = new ArrayList<>();
        qrTxt = qrTxt.toLowerCase();
//
//        // Tìm kiếm Phim Top
//        List<MovieAll> listT = solrService.searchMovieAll(userId, qrTxt, 0, 1);
//        if (CollectionUtils.isEmpty(listT)) {
//            return new ArrayList<>();
//        }
//
//        // Thêm Phim vào kết quả
//        ResultGroup movieGroup = new ResultGroup();
//        movieGroup.setGroupName("Movies");
//        movieGroup.setItems(listT.stream()
//                .map(movieAll -> ResultItem.builder()
//                        .id(movieAll.getContentId().toString())
//                        .avatar(movieAll.getAvatar())
//                        .itemName(movieAll.getContentName())
//                        .itemType("movie")
//                        .movieInfo(movieAll)
//                        .build())
//                .collect(Collectors.toList()));
//        listGroup.add(movieGroup);
//
//        // Tìm kiếm Diễn viên
//        List<MovieActor> actorList = solrService.searchMovieActor(userId, qrTxt, 0, 5);
//        if (!CollectionUtils.isEmpty(actorList)) {
//            ResultGroup actorGroup = new ResultGroup();
//            actorGroup.setGroupName("Actors");
//            actorGroup.setItems(actorList.stream()
//                    .map(actor -> ResultItem.builder()
//                            .id(actor.getContentId().toString())
//                            .avatar(actor.getAvatar())
//                            .itemName(actor.getName())
//                            .itemType("actor")
//                            .actorInfo(actor)
//                            .build())
//                    .collect(Collectors.toList()));
//            listGroup.add(actorGroup);
//        }
//
//        // Tìm kiếm Thể loại
//        List<MovieCategory> categoryList = solrService.searchMovieCategory(userId, qrTxt, 0, 5);
//        if (!CollectionUtils.isEmpty(categoryList)) {
//            ResultGroup categoryGroup = new ResultGroup();
//            categoryGroup.setGroupName("Categories");
//            categoryGroup.setItems(categoryList.stream()
//                    .map(category -> ResultItem.builder()
//                            .id(category.getContentId().toString())
//                            .avatar("")
//                            .itemName(category.getName())
//                            .itemType("category")
//                            .categoryInfo(category)
//                            .build())
//                    .collect(Collectors.toList()));
//            listGroup.add(categoryGroup);
//        }

        return listGroup;
    }

    @Override
    public List<ResultItem> searchByMovie(String userId, String qrTxt, int page, int size, long timestamp) {
        if (qrTxt == null || qrTxt.trim().isEmpty()) {
            return new ArrayList<>();
        }

        qrTxt = qrTxt.toLowerCase();

        // Tìm kiếm MovieSeries dựa trên qrTxt
        List<MovieSeries> list = solrService.searchMovieSeries(userId, qrTxt, page, size);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        // Lấy danh sách ID từ list MovieSeries
        List<String> listId = list.stream()
                .map(i -> String.valueOf(i.getContentId()))
                .collect(Collectors.toList());

        List<Movie> listMovie = movieService.getMovieByListId(userId, listId, timestamp, qrTxt);

        if (CollectionUtils.isEmpty(listMovie)) {
            return new ArrayList<>();
        }

        return listMovie.stream()
                .map(movie -> ResultItem.builder()
                        .id(movie.getId())
                        .avatar(movie.getImageUrl())
                        .itemName(movie.getMovieName())
                        .itemType("movie")
                        .movieInfo(movie)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultItem> searchByActor(String userId, String qrTxt, int page, int size, long timestamp) {
        if (qrTxt == null || qrTxt.trim().isEmpty()) {
            return new ArrayList<>();
        }

        qrTxt = qrTxt.toLowerCase();

        // Tìm kiếm MovieSeries dựa trên qrTxt
        List<MovieActor> list = solrService.searchMovieActor(userId, qrTxt, page, size);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        // Lấy danh sách ID từ list MovieSeries
        List<String> listId = list.stream()
                .map(i -> String.valueOf(i.getContentId()))
                .collect(Collectors.toList());

        List<Actor> listActor = actorService.getActorsByListId(userId, listId, timestamp, qrTxt);

        if (CollectionUtils.isEmpty(listActor)) {
            return new ArrayList<>();
        }

        return listActor.stream()
                .map(actor -> ResultItem.builder()
                        .id(actor.getId())
                        .avatar(actor.getAvatar())
                        .itemName(actor.getName())
                        .itemType("actor")
                        .actorInfo(actor)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultItem> searchByCategory(String userId, String qrTxt, int page, int size, long timestamp) {
        if (qrTxt == null || qrTxt.trim().isEmpty()) {
            return new ArrayList<>();
        }

        qrTxt = qrTxt.toLowerCase();

        List<MovieCategory> list = solrService.searchMovieCategory(userId, qrTxt, page, size);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        // Lấy danh sách ID từ list MovieSeries
        List<String> listId = list.stream()
                .map(i -> String.valueOf(i.getContentId()))
                .collect(Collectors.toList());

        List<Category> listCategory = categoryService.getCategoryByListId(userId, listId, timestamp, qrTxt);

        if (CollectionUtils.isEmpty(listCategory)) {
            return new ArrayList<>();
        }

        return listCategory.stream()
                .map(category -> ResultItem.builder()
                        .id(category.getId())
                        .avatar("")
                        .itemName(category.getName())
                        .itemType("category")
                        .categoryInfo(category)
                        .build())
                .collect(Collectors.toList());
    }
}
