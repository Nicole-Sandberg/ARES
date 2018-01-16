package com.retriever.ARES.utils;

import com.retriever.ARES.models.QueryObject;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QueryBuilderUtils {
	public Optional<SearchRequestBuilder> getRequestBuilderByQuery(QueryObject query) {
		return Optional.of(null);
	}
}
