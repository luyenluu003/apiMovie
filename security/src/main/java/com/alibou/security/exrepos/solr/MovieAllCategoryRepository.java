package com.alibou.security.exrepos.solr;

import com.alibou.security.exrepos.solr.entity.MovieCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

public interface MovieAllCategoryRepository extends SolrCrudRepository<MovieCategory, String> {
    @Query(name = "MovieAllCategorySolr.findContentByQuery")
    Page<MovieCategory> findContentByQuery(String searchTerm, Pageable pageable);
}
