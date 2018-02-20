package com.retriever.ARES.models;

import java.text.SimpleDateFormat;

public class SearchQuery {
		boolean includeStory = false;
		String query;
		int maxHits;
		SimpleDateFormat documentCreatedAfter;

		SearchQuery() { }

	public SimpleDateFormat getDocumentCreatedAfter() {
		return documentCreatedAfter;
	}

	public void setDocumentCreatedAfter(SimpleDateFormat documentCreatedAfter) {
		this.documentCreatedAfter = documentCreatedAfter;
	}

	public String getQuery() {
				return query;
		}

		public void setQuery(String query) {
				this.query = query;
		}

		public int getMaxHits() {
				return maxHits;
		}

		public void setMaxHits(int maxHits) {
				this.maxHits = maxHits;
		}

		public boolean isIncludeStory() {
				return includeStory;
		}

		public void setIncludeStory(boolean includeStory) {
				this.includeStory = includeStory;
		}
}
