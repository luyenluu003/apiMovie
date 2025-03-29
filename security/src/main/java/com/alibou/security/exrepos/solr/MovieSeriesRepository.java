package com.alibou.security.exrepos.solr;

import com.alibou.security.exrepos.solr.entity.MovieSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

public interface MovieSeriesRepository  extends SolrCrudRepository<MovieSeries, String> {

    @Query(name = "MovieSeriesSolr.findContentByQuery")
    Page<MovieSeries> findContentByQuery(String searchTerm, Pageable pageable);
}
