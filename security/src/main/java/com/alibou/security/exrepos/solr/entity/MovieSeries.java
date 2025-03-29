package com.alibou.security.exrepos.solr.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;


@Data
@SolrDocument(collection = "movie_series")
public class MovieSeries {
    private static final long serialVersionUID = 6629669541200831022L;
    @Id
    @Indexed(name = "id", type = "string")
    private String id;

    @Indexed(name = "content_id", type = "string")
    private String contentId;

    @Indexed(name = "avatar", type = "string")
    private String avatar;

    @Indexed(name = "content_name", type = "string")
    private String contentName;

    @Indexed(name = "name", type = "string")
    private String name;
}
