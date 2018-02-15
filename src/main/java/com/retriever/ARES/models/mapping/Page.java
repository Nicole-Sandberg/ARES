package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
	private String story;
	private String page;

	public Page(
			@JsonProperty("story") String story,
				@JsonProperty("page") String page) {
		this.story = story;
		this.page = page;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
}
