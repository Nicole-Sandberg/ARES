package com.retriever.ARES.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.retriever.ARES.utils.QueryBuilderUtils;
import org.assertj.core.util.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchQuery {
	private Integer maxHits = 20;
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


	public Optional<QueryBuilder> toQueryBuilder() {
		if (Strings.isNullOrEmpty(queryString))
			return Optional.empty();
		String query = queryString;
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		builder.must(QueryBuilderUtils.getNestedQuery(query));

		return Optional.of(builder);
	}
}
