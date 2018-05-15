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
import java.util.Objects;
import java.util.Optional;

@Component
public class QueryBuilderUtils {
	@Autowired
	private Client client;

	private static Logger log = org.slf4j.LoggerFactory.
			getLogger(QueryBuilderUtils.class);

	/**
	 * Kör en nestlad query med match och path "pages".
	 *
	 * @param query String
	 * @return QueryBuilder
	 */
	private static QueryBuilder getNestedQuery(String query) {

		return QueryBuilders.nestedQuery(
				Globals.PATH_ONE, matchQuery(query), ScoreMode.Avg);
	}

	/**
	 * gör en match-query där träffarna visas först,
	 * dock så rensar den inte bort resultat som inte matchar, de hamnar bara längst ned.
	 * Den matchar mot årsredovisningens inmatningstext.
	 *
	 * @param query String
	 * @return QueryBuilder
	 */
	private static QueryBuilder matchQuery(String query) {
		return QueryBuilders.matchQuery(Globals.MATCH_FIELD, query);
	}

	/**
	 * Skapar en builder för queryt. Den anpassar så resultatet visar det som
	 * kunden ville ha spefifikt. Den kör en nestlad query med namn.
	 *
	 * @param query SearchQuery
	 * @return Optional<SearchRequestBuilder>
	 */
	public Optional<SearchRequestBuilder> umea(SearchQuery query) {
		// TODO: 2018-04-11	maxhit
		SearchRequestBuilder builder = getBuilderWithMaxHits(query.getMaxHits());
		builder.setFetchSource(Globals.getUmeasearchresultfields(), new String[0]);
		getNestedQueryWithName(query.getQuery()).ifPresent(builder::setQuery);
		log.info(builder.toString());
		return Optional.of(builder);
	}

	/**
	 * Kör en builder med maxhit,
	 * om story ska visas i resultatet,
	 * vilken offset bestämd av queyObjektet som skickas in.
	 * Används för salesInsight
	 *
	 * @param query SearchQuery
	 * @return Optional<SearchRequestBuilder>
	 */
	public Optional<SearchRequestBuilder> getRequestBuilderByQuery(SearchQuery query) {
		SearchRequestBuilder builder = getBuilderWithMaxHits(query.getMaxHits());
		builder.setFetchSource(Globals.getFIELDS(query.isIncludeStory()), new String[0]);
		builder.setFrom(query.getOffset());
		parseQuery(query).ifPresent(builder::setQuery);
		log.info(builder.toString());
		return Optional.of(builder);
	}

	/**
	 * Första testet för umeå projektetet. Använder namedquery på en databas-struktur
	 * som inte
	 * stödjer en namequery
	 *
	 * @param searchQuery SearchQuery
	 * @return Optional<SearchRequestBuilder>
	 */
	public Optional<SearchRequestBuilder> test(SearchQuery searchQuery) {
		SearchRequestBuilder builder = getBuilderWithMaxHits(10);
		builder.setFetchSource(Globals.getFIELDS(searchQuery.isIncludeStory()),
				new String[0]);
		builder.setFrom(searchQuery.getOffset());
		parseQueryTest(searchQuery.getQuery()).ifPresent(builder::setQuery);
		return Optional.of(builder);
	}

	/**
	 * försöker testa namedquery API, fungerar ej för index 4.
	 *
	 * @param query String
	 * @return Optional<QueryBuilder>
	 */
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
	}
	/**
	 * Använder en querystringquerybuilder som parsar queryn efter AND,OR,NOT.
	 * Den kör med Or operatorn.
	 * Används i salesInsight
	 *
	 * @param query String
	 * @return Optional<QueryStringQueryBuilder>
	 */
	private Optional<QueryBuilder> parseQuery(SearchQuery query) {

		BoolQueryBuilder bool = QueryBuilders.boolQuery();

		dateField(query.getDocumentCreatedAfter()).ifPresent(bool::must);
		queryString(query.getQuery()).ifPresent(bool::must);
		return bool.hasClauses() ? Optional.of(bool) : Optional.empty();
	}


		private Optional<QueryBuilder> dateField(String dateTime) {
			if (Objects.isNull(dateTime)) return Optional.empty();
			return Optional.of(QueryBuilders.rangeQuery(Field.DATE).gt(dateTime));
		}

		private Optional<QueryBuilder> queryString(String query) {
			if (query.equals("")) return Optional.empty();

			QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query);
			queryBuilder.lenient(true);
			queryBuilder.defaultOperator(Operator.OR);
			queryBuilder.minimumShouldMatch(String.valueOf(1));
			return Optional.of(queryBuilder);
		}

		private SearchRequestBuilder getBuilderWithMaxHits(int maxHits) {
			return getBuilder(0, maxHits);
		}

		private SearchRequestBuilder getBuilder(int from, int size) {
			return client.prepareSearch().setFrom(from).setSize(size).setIndices(Globals
					.INDEX);
		}

		/**
		 * namnger nested query med query sökningen. Dvs nyckelordet.
		 * @param query String
		 * @return Optional<QueryBuilder>
		 */
		private Optional<QueryBuilder> getNestedQueryWithName(String query) {
			QueryBuilder nestedQueryBuilder = getNestedQuery(query);
//		nestedQueryBuilder.innerHit(new InnerHitBuilder(), true);
//		nestedQueryBuilder.innerHit()
//				.addDocValueField("report.year")
//				.addDocValueField("report.from_month");
			nestedQueryBuilder.queryName(query);
			return Optional.of(nestedQueryBuilder);
		}


		/**
		 * metoden parsar queryt manuellt,
		 * användes innan querystringquerybuilder fungerade.
		 * @param rawInputString String
		 * @return Optional<BoolQueryBuilder>
		 */
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

