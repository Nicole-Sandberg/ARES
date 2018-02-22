package com.retriever.ARES.utils;

import com.retriever.ARES.models.SearchQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
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
		// TODO: 2018-02-22 if input sista ordet. stanna och skicka iväg query
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



	//	if (rawInput.contains(Arrays.toString(Globals.getKeys()))) {
	//		return Optional.of(getMustQuery(rawInput));
	//	}
		return parseQuery(rawInput);
	}
	public static QueryStringQueryBuilder parse(String input){
		//String string =  "håkan AND hellström OR båt";
		QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(input);
		//queryBuilder.defaultField("df");
		queryBuilder.field(Globals.MATCH_FIELD);
		//queryBuilder.analyzer("analyser");
		queryBuilder.analyzeWildcard(false);
		if (input.contains("OR")){
			queryBuilder.defaultOperator(Operator.OR);
		}
		if (input.contains("AND")){
			queryBuilder.defaultOperator(Operator.AND);
		}
	 else {
		throw new IllegalArgumentException("Unsupported defaultOperator [" + input + "], can either be [OR] or [AND]");
	}
    return queryBuilder;
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

	private static QueryBuilder getMustQuery(String section) {
		BoolQueryBuilder mustQueries = QueryBuilders.boolQuery();
		Arrays.stream(section.split("AND")).forEach(phrase -> {
			mustQueries.must(getNestedQuery(phrase.trim()));
		});
		return mustQueries;
	}
}
