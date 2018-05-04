package com.retriever.ARES.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retriever.ARES.models.ARESCsvOutput;
import com.retriever.ARES.models.SearchResponseARES;
import com.retriever.ARES.models.mapping.Company;
import com.retriever.ARES.models.mapping.InnerHitObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public final class ResponseUtils {
	private ResponseUtils() { }
	private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

	private static ObjectMapper mapper = new ObjectMapper();


	public static List<Company> parseHits(SearchResponse response) {
		return getResults(response);
	}

	/**
	 * Mappar resultatet och null-checkar.
	 * @param response SearchResponse
	 * @return List<Company>
	 */
	private static List<Company> getResults(SearchResponse response) {
		return Arrays.stream(response.getHits().getHits())
				.map(ResponseUtils::apply).filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	/**
	 * Hämtar ut innerhits (om de finns några, används vid nested querys bl.a). null-check
	 * Samt mappar resultatet med Company class. null-check
	 * Om queryt använder sig utav namedquery så kommer matchedquery visa
	 * vilken query som fick träff.
	 * @param hit SearchHit
	 * @return Company
	 */
	private static Company apply(SearchHit hit) {
		try {
			Company searchResult = mapper.readValue(hit
					.getSourceAsString(), Company.class);
			if (hit.getInnerHits() != null) {
				searchResult.addInnerHits(getAndMapInnerHits(hit));
			}
			if (hit.getMatchedQueries().length > 0) {
				searchResult.setMatchedQueries(hit.getMatchedQueries()[0]);
			}
			return searchResult;
		} catch (IOException e) {
			log.info(e.getMessage());
			log.error("failed parse hit");
			return null;
		}
	}

	/**
	 * hämtar innerhit om de finns och mappar till klassen innerHitsObjekt. null-check
	 * @param hit SearchHit
	 * @return List<InnerHitObject>
	 */
	private static List<InnerHitObject> getAndMapInnerHits(SearchHit hit) {
		return Arrays.stream(hit.getInnerHits().get("report")
				.getHits()).map(innerHit -> {
			try {
				return mapper.readValue(
						innerHit.getSourceAsString(),
						InnerHitObject.class);
			} catch (IOException e) {
				log.error(e.getMessage());
				log.error("failed parse inner hit");
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}
	/**
	 * parsar responses från queries så att
	 * samma rapport inte nämns flera ggr,
	 * utan att List av cells lägger till träffen istället.
	 * @param responses List<SearchResponsesARES>
	 * @return List<ARESCsvOutput>
	 */
	public static List<ARESCsvOutput> parseResultsForUmea(
			List<SearchResponseARES> responses) {
		
		Map<String, ARESCsvOutput> correctMapping = new HashMap<>();

		for (SearchResponseARES response : responses) {
			for (int i = 0; i < response.getReturnedHits(); i++) {
				String uniqueID = String.format("%s%s%s",
						response.getResults().get(i).getOrgnr() + " ",
						response.getResults().get(i).getYear() + " ",
						response.getResults().get(i).getMonth());
				if (correctMapping.containsKey(uniqueID)) {
					correctMapping.get(uniqueID).setCells(response
							.getResults().get(i).getMatchedQueries());
					correctMapping.get(uniqueID).setHits(
							correctMapping.get(uniqueID).getCells().size());
					break;
				} else {
					correctMapping.put(uniqueID, new ARESCsvOutput(
							response.getResults().get(i).getOrgnr(),
							response.getResults().get(i).getYear() +
									response.getResults().get(i).getMonth(),
							response.getResults().get(i)
									.getMatchedQueries()));
				}
			}
		}
		return new ArrayList<>(correctMapping.values());
	}
}
