package com.retriever.ARES.services;

import com.retriever.ARES.models.ARESCsvOutput;
import com.retriever.ARES.models.SearchQuery;
import com.retriever.ARES.models.SearchResponseARES;
import com.retriever.ARES.utils.QueryBuilderUtils;
import com.retriever.ARES.utils.ResponseUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ElasticsearchService {

	private static final Logger log = LoggerFactory.getLogger(ElasticsearchService.class);

	@Value("${logging.debug}")
	private Boolean debug;

	@Value("${elasticsearch.timeout}")
	protected long timeout;

	@Autowired
	QueryBuilderUtils queryBuilder;


	public Optional<SearchResponse> search(SearchQuery query) {
		//	 query.getQueryObject().flatMap(this::actionGet);
	return queryBuilder.getRequestBuilderByQuery(query).flatMap(this::actionGet);
	}
	public Optional<SearchResponse> test(SearchQuery query) {
		return queryBuilder.test(query).flatMap(this::actionGet);
	}



	private <U extends ActionResponse> Optional<U>
		actionGet(ActionRequestBuilder<?, U, ?> builder) {
		if (debug) log.info("Performing query :\n{}", builder.toString());
		long startTime = System.currentTimeMillis();
		try {
			U actionGet = builder.execute().actionGet(timeout);
			if (debug) {
				log.info("Query used: {} ms",
						System.currentTimeMillis() - startTime);
			}
			return Optional.of(actionGet);
		} catch (ElasticsearchException e) {
			log.warn("Elasticsearch failed : {}", e);
			return Optional.empty();
		}
	}

		public Optional<SearchResponse> searchUmea(SearchQuery query) {
		return queryBuilder.umea(query).flatMap(this::actionGet);
		}

		public List<ARESCsvOutput> parseResults(List<SearchResponseARES> response) {

			return ResponseUtils.parseResultsForUmea(response);		}
}
