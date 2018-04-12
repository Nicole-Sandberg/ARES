package com.retriever.ARES.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retriever.ARES.models.mapping.Company;
import com.retriever.ARES.models.mapping.InnerHitObject;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public final class ResponseUtils {
	private ResponseUtils() { };
	private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

	private static ObjectMapper mapper = new ObjectMapper();


	public static List<Company> parseHits(SearchResponse response) {
		return getResults(response);
	}
	public static List<Company> parseHitsForUmea(SearchResponse response) {
		return getResults(response);
	}

	private static List<Company> getResults(SearchResponse response) {
		return Arrays.stream(response.getHits().getHits())
				.map(hit -> {
					try {
						List<InnerHitObject> hitObjects =
								Arrays.stream(hit.getInnerHits().get("report")
										.getHits()).map(innerHit -> {
									try {
										return mapper.readValue(
												innerHit.getSourceAsString(),
												InnerHitObject.class);
									} catch (IOException e) {
										log.error(e.getMessage());
										return null;
									}
								}).filter(Objects::nonNull).collect(Collectors.toList());
						Company searchResult = mapper.readValue(hit
								.getSourceAsString(), Company.class);
						searchResult.addInnerHits(hitObjects);
						return searchResult;
					} catch (IOException e) {
						log.info(e.getMessage());
						log.error("failed parse hit");
						return null;
					}
				}).filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}
