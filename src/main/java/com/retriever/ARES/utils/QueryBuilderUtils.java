package com.retriever.ARES.utils;

import com.retriever.ARES.models.SearchQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class QueryBuilderUtils {
	@Autowired
	private Client client;

	public Optional<SearchRequestBuilder> getRequestBuilderByQuery(SearchQuery query) {
		SearchRequestBuilder builder = getBuilder();
		builder.setQuery(query.toQueryBuilder());
		return Optional.of(builder);
	}

	private SearchRequestBuilder getBuilder() {
		return getBuilder(0,100);
	}

	private SearchRequestBuilder getBuilder(int from, int size) {
		return client.prepareSearch().setFrom(from).setSize(size).setIndices(Globals
				.INDEX);
	}
}
