package com.retriever.ARES.models;

public class SearchQuery {
		boolean includeStory = false;
		String query;
		int maxHits;

		SearchQuery() { }

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
