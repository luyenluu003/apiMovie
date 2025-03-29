package com.alibou.security.exrepos.solr;

import com.alibou.security.exrepos.solr.entity.MovieActor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;



public interface MovieAllActorRepository extends SolrCrudRepository<MovieActor, String> {
    @Query(name = "MovieAllActorSolr.findContentByQuery")
    Page<MovieActor> findContentByQuery(String searchTerm, Pageable pageable);
}
