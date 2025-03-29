package com.alibou.security.exrepos.solr;

import com.alibou.security.exrepos.solr.entity.MovieAll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

public interface MovieAllRepository extends SolrCrudRepository<MovieAll, String> {

    @Query(name = "MovieAllSolr.findContentByQuery")
    Page<MovieAll> findContentByQuery(String searchTerm, Pageable pageable);
}
