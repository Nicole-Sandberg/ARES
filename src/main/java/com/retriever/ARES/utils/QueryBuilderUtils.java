package com.retriever.ARES.utils;

import com.retriever.ARES.models.SearchQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;

@Component
public class QueryBuilderUtils {
	@Autowired
	private Client client;

	private static Logger log = org.slf4j.LoggerFactory.
			getLogger(QueryBuilderUtils.class);

	public static QueryBuilder getMultiNestedQuery(String query) {

		return QueryBuilders.nestedQuery(
				Globals.PATH_TWO, matchQuery(query), ScoreMode.None);
	}

	private static QueryBuilder matchQuery(String query) {
		return QueryBuilders.matchQuery(Globals.MATCH_FIELD, query);
	}
	public Optional<SearchRequestBuilder> umea(String query) {
		SearchRequestBuilder builder = getBuilderWithMaxHits(10);
		builder.setFetchSource(Globals.getCompanynameandorgnr(), new String[0]);
		getNestedQueryWithInnerHit(query).ifPresent(builder::setQuery);
		log.info(builder.toString());
		return Optional.of(builder);
	}

	public Optional<SearchRequestBuilder> getRequestBuilderByQuery(SearchQuery query) {
		SearchRequestBuilder builder = getBuilderWithMaxHits(query.getMaxHits());
		builder.setFetchSource(Globals.getFIELDS(query.isIncludeStory()), new String[0]);
		builder.setFrom(query.getOffset());
		parseQuery(query.getQuery()).ifPresent(builder::setQuery);
		log.info(builder.toString());
		return Optional.of(builder);
	}

	public Optional<SearchRequestBuilder> test(SearchQuery searchQuery) {
		SearchRequestBuilder builder = getBuilderWithMaxHits(10);
		builder.setFetchSource(Globals.getFIELDS(searchQuery.isIncludeStory()),
				new String[0]);
		builder.setFrom(searchQuery.getOffset());
		parseQueryTest(searchQuery.getQuery()).ifPresent(builder::setQuery);
		return Optional.of(builder);
	}

	private Optional<QueryBuilder> parseQueryTest(String query) {

		//hasparent type, query,score
		BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
		BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
		TermQueryBuilder termQueryOne = QueryBuilders.termQuery("year", 2018)
				.queryName("termOne");
		TermQueryBuilder termQueryTwo = QueryBuilders.termQuery("year", 2019)
				.queryName("termTwo");
		mustQuery.must(QueryBuilders.hasChildQuery("properties",
				shouldQuery.should(termQueryOne)
				.should(termQueryTwo).minimumShouldMatch(1), ScoreMode.None)
				.queryName("parent").innerHit(new InnerHitBuilder(), true)
		);
		log.debug(mustQuery.toString());
		return Optional.of(mustQuery);
		/*return QueryBuilders.boolQuery().must(
				QueryBuilders.hasParentQuery("report",
						QueryBuilders.boolQuery()
								.should(QueryBuilders.termQuery("year",
								"2018").queryName("term")).
								queryName("innerBool")).queryName("parent")
		).queryName("outerBool")); */
	}

	private Optional<QueryStringQueryBuilder> parseQuery(String query) {
		if (query.equals("")) {
			return Optional.empty();
		}
		QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query);
		queryBuilder.lenient(true);
		queryBuilder.defaultOperator(Operator.OR);
		return Optional.of(queryBuilder);
	}
	private SearchRequestBuilder getBuilderWithMaxHits(int maxHits) {
		return getBuilder(0, maxHits);
	}

	private SearchRequestBuilder getBuilder(int from, int size) {
		return client.prepareSearch().setFrom(from).setSize(size).setIndices(Globals
				.INDEX);
	}
	private Optional<QueryBuilder> getNestedQueryWithInnerHit(String query) {
		NestedQueryBuilder nestedQueryBuilder =  QueryBuilders.nestedQuery(
				Globals.PATH_ONE, getMultiNestedQuery(query), ScoreMode.Avg);
		nestedQueryBuilder.innerHit(new InnerHitBuilder(), true);
		nestedQueryBuilder.innerHit()
				.addDocValueField("report.year")
				.addDocValueField("report.from_month");
		return Optional.of(nestedQueryBuilder);
	}

	public static QueryBuilder getNestedQuery(String query) {
		return   QueryBuilders.nestedQuery(
				Globals.PATH_ONE, getMultiNestedQuery(query), ScoreMode.Avg);
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

	private static QueryBuilder getMustQuery(String section) {
		BoolQueryBuilder mustQueries = QueryBuilders.boolQuery();
		Arrays.stream(section.split("AND")).forEach(phrase -> {
			mustQueries.must(getNestedQuery(phrase.trim()));
		});
		return mustQueries;
	}
}
