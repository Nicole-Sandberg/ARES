package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InnerHitObject {
	private final String month;
	private final String year;

	public InnerHitObject(@JsonProperty("year") String year,
						@JsonProperty("from_month") String from_month) {
		this.year = year;
		this.month = from_month;
	}

	public String getMonth() {
		return month;
	}

	public String getYear() {
		return year;
	}
	public String toString() {
		return month + year;
	}

}
