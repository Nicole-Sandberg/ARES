package com.retriever.ARES.models;

import java.time.LocalDateTime;

public class SearchQuery {
		boolean includeStory = false;
		String query;
		int maxHits;
		LocalDateTime documentCreatedAfter;

		SearchQuery() { }

	public LocalDateTime getDocumentCreatedAfter() {
		return documentCreatedAfter;
	}

	public void setDocumentCreatedAfter(LocalDateTime documentCreatedAfter) {
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
