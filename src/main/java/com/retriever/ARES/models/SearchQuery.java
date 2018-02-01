package com.retriever.ARES.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Optional;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchQuery {
    private Integer maxHits= 20;
    private String queryString;

    private JsonNode sort;

    private static ObjectMapper mapper = new ObjectMapper();

    public SearchQuery(String query) {
        this.queryString = query;
    }

    public void setSort(JsonNode sort) {
        this.sort = sort;
    }
    public JsonNode getSort() {
        return Optional.ofNullable(sort).orElse(mapper.createObjectNode().nullNode());
    }
    public Integer getMaxHits() {
        return maxHits;
    }

    public void setMaxHits(Integer maxHits) {
        this.maxHits = maxHits;
    }



    public QueryBuilder toQueryBuilder() {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
// TODO: 2018-02-01 se till att strängen är skriven såsom kibana
        return builder;
    }

}
