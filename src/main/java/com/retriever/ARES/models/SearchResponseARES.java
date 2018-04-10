package com.retriever.ARES.models;

import com.retriever.ARES.models.mapping.Company;
import com.retriever.ARES.utils.ResponseUtils;
import org.elasticsearch.action.search.SearchResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseARES {
	private int fromIndex;
	private int toIndex;
	private long returnedHits;
	private long totalHits;
	private List<Company> results = new ArrayList<>();

	public SearchResponseARES() {
	}

	public SearchResponseARES(SearchQuery query, List<SearchResponse> responses) {

		this.totalHits = responses.isEmpty() ? 0 : responses.get(0).getHits()
				.getTotalHits();
		responses.forEach(response ->
		results.addAll(ResponseUtils.parseHits(response)));
	}
	public SearchResponseARES(String query, List<SearchResponse> responses) {

		this.totalHits = responses.isEmpty() ? 0 : responses.get(0).getHits()
				.getTotalHits();
		responses.forEach(response ->
				results.addAll(ResponseUtils.parseHits(response)));
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
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
