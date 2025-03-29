package com.alibou.security.config;


import lombok.extern.log4j.Log4j2;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Log4j2
@Configuration
@EnableSolrRepositories(
        basePackages = "com.alibou.security.exrepos.solr",
        namedQueriesLocation = "classpath:solr-named-queries.properties")
@ComponentScan
public class SolrConfig {
    @Value("${url.solr.service}")
    private String solrBaseUrl;

    @Bean
    public SolrClient solrClient() {
        log.info("solrBaseUrl|{}", solrBaseUrl);
        return new HttpSolrClient.Builder(solrBaseUrl).build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        if (client == null) {
            log.error("SolrClient is NULL");
            throw new RuntimeException("SolrClient is not initialized");
        }
        log.info("Creating SolrTemplate with SolrClient: {}", client);
        return new SolrTemplate(client);
    }
}
