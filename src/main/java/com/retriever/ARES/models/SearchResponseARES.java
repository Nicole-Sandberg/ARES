package com.retriever.ARES.models;

import com.retriever.ARES.models.mapping.Company;
import com.retriever.ARES.utils.ResponseUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseARES {
	private long returnedHits;
	private long totalHits;
	private int offset;
	private List<Company> results = new ArrayList<>();

	private static Logger log = org.slf4j.LoggerFactory.
			getLogger(SearchResponseARES.class);

	public SearchResponseARES() {
	}

	public SearchResponseARES(SearchQuery query, List<SearchResponse> responses) {

		this.totalHits = responses.isEmpty() ? 0 : responses.get(0).getHits()
				.getTotalHits();
		this.returnedHits = responses.isEmpty() ? 0 : responses.get(0).getHits()
				.getHits().length;
		this.offset = query.getOffset();

		responses.forEach(response ->
				results.addAll(ResponseUtils.parseHits(response)));
	}

	// TODO: 2018-04-16 to bort rep kod
	public SearchResponseARES(List<SearchResponse> responses) {

		this.totalHits = responses.isEmpty() ? 0 : responses.get(0).getHits()
				.getTotalHits();
		this.returnedHits = responses.isEmpty() ? 0 : responses.get(0).getHits()
				.getHits().length;
		responses.forEach(response ->
				results.addAll(ResponseUtils.parseHits(response)));

	}

	public long getReturnedHits() {
		return returnedHits;
	}

	public void setReturnedHits(long returnedHits) {
		this.returnedHits = returnedHits;
	}

	public long getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}

	public List<Company> getResults() {
		return results;
	}

	public void setResults(List<Company> results) {
		this.results = results;
	}
}
