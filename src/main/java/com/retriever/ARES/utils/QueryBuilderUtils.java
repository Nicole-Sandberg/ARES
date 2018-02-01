package com.retriever.ARES.utils;

import com.retriever.ARES.models.SearchQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class QueryBuilderUtils {
	@Autowired
	private Client client;

	public static QueryBuilder getMultiNestedQuery(String query) {
		return QueryBuilders.nestedQuery(Globals.PATH_TWO,matchQuery(query),ScoreMode.None);
	}

	private static QueryBuilder matchQuery(String query) {
		return QueryBuilders.matchQuery(Globals.MATCH_FIELD,query);
	}

	public Optional<SearchRequestBuilder> getRequestBuilderByQuery(SearchQuery query) {
		SearchRequestBuilder builder = getBuilder();
		builder.setFetchSource(Globals.FIELDS,new String[0]);
		query.toQueryBuilder().ifPresent(builder::setQuery);
		return Optional.of(builder);
	}

	private SearchRequestBuilder getBuilder() {
		return getBuilder(0,100);
	}

	private SearchRequestBuilder getBuilder(int from, int size) {
		return client.prepareSearch().setFrom(from).setSize(size).setIndices(Globals
				.INDEX);
	}

	public static QueryBuilder getNestedQuery(String query) {
		return QueryBuilders.nestedQuery(Globals.PATH_ONE,getMultiNestedQuery(query), ScoreMode.None);

	}
}
