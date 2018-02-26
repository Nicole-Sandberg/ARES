package com.retriever.ARES.utils;

import com.retriever.ARES.models.SearchQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		// TODO: 2018-02-22 if input sista ordet. stanna och skicka iväg query
		if (rawInput.indexOf('"')>=0) {
			String sub = rawInput.substring(0,rawInput.lastIndexOf('"'));
			String restSub = rawInput.substring(rawInput.lastIndexOf('"'),10);

		}
		String[]input = rawInput.split(" ");
		String save = "";

		for (int i = 0; i <input.length ; i++) {
			if (input[i].substring(0).equals('"')){
				String matchQuery = input[i].replace('"',' ').trim();
				save = matchQuery;
				parseQuery(String.valueOf(Arrays.copyOfRange(input,i+1,input.length)));
			}
			if (input[i].equals("OR")){
				BoolQueryBuilder shouldQuery = QueryBuilders.boolQuery();
				if(save != ""){
					//start should clause
					shouldQuery.should(getMultiNestedQuery(String.valueOf(save)));
				}
				parseQuery(String.valueOf(Arrays.copyOfRange(input,i+1,input.length)));
			}
			if (input[i].equals("AND")){
				BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
				mustQuery.must(getNestedQuery(save));
				mustQuery.must(getNestedQuery(input[i+1]));
				parseQuery(String.valueOf(Arrays.copyOfRange(input,i+2,input.length)));
			}
			if (input[i].equals("NOT")){
				BoolQueryBuilder mustNot = QueryBuilders.boolQuery();
				mustNot.mustNot(getNestedQuery(input[i+1]));
				parseQuery(String.valueOf(Arrays.copyOfRange(input,i+1,input.length)));
			}
			else{
				save = input[i];
			}
		}
		return parseQuery(rawInput);
	}
	public static QueryStringQueryBuilder parse(String input){
		QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(input);
		//queryBuilder.defaultField("df");
		queryBuilder.field(Globals.MATCH_FIELD);
		//queryBuilder.analyzer("analyser");
		queryBuilder.analyzeWildcard(false);
		return queryBuilder;

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
