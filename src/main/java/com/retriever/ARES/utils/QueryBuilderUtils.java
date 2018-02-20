package com.retriever.ARES.utils;

import com.retriever.ARES.models.SearchQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class QueryBuilderUtils {
	@Autowired
	private Client client;

	public static QueryBuilder getMultiNestedQuery(String query) {

		return QueryBuilders.nestedQuery(
				Globals.PATH_TWO, matchQuery(query), ScoreMode.None);
	}

	private static QueryBuilder matchQuery(String query) {
		return QueryBuilders.matchQuery(Globals.MATCH_FIELD, query);
	}

	public Optional<SearchRequestBuilder> getRequestBuilderByQuery(SearchQuery query) {
		SearchRequestBuilder builder = getBuilderWithMaxHits(query.getMaxHits());
		builder.setFetchSource(Globals.getFIELDS(query.isIncludeStory()), new String[0]);
		getRootQuery(query.getQuery()).ifPresent(builder::setQuery);
		return Optional.of(builder);
	}
	private Optional<QueryBuilder> parseQuery(String rawInput) {
		if (rawInput.contains(Arrays.toString(Globals.getKeys()))) {
			return Optional.of(getMustQuery(rawInput));
		}
		return parseQuery(rawInput);
	}
	private void parse(){
		String string =  "håkan AND hellström";
		QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery("q");
		queryBuilder.defaultField("df");
		queryBuilder.analyzer("analyser");
		queryBuilder.analyzeWildcard(false);
		if ("OR".equals(string)){
			queryBuilder.defaultOperator(QueryStringQueryBuilder.DEFAULT_OPERATOR); //OR
		}
	}

	private SearchRequestBuilder getBuilderWithMaxHits(int maxHits) {
		return getBuilder(0, maxHits);
	}

	private SearchRequestBuilder getBuilder(int from, int size) {
		return client.prepareSearch().setFrom(from).setSize(size).setIndices(Globals
				.INDEX);
	}

	public static QueryBuilder getNestedQuery(String query) {
		return QueryBuilders.nestedQuery(
				Globals.PATH_ONE, getMultiNestedQuery(query), ScoreMode.None);

	}

	public static Optional<BoolQueryBuilder> getRootQuery(String rawInputString) {
		if (rawInputString.equals(""))
			return Optional.empty();

			BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
			Arrays.stream(rawInputString.split("OR")).forEach(section -> {
				shouldQuery.should(getMustQuery(section.trim()));
			});
			shouldQuery.minimumShouldMatch(1);
			return Optional.of(shouldQuery);
	}

	private static QueryBuilder getMustNotQuery(String raw) {
		BoolQueryBuilder mustNotQueries = QueryBuilders.boolQuery();
		Arrays.stream(raw.split("ANDNOT")).forEach(phrase ->{
			mustNotQueries.mustNot(getNestedQuery(phrase.trim()));
		});
		return mustNotQueries;
	}

	private static QueryBuilder getMustQuery(String section) {
		BoolQueryBuilder mustQueries = QueryBuilders.boolQuery();
		Arrays.stream(section.split("AND")).forEach(phrase -> {
			mustQueries.must(getNestedQuery(phrase.trim()));
		});
		return mustQueries;
	}
}
