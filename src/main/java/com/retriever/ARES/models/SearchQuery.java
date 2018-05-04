package com.retriever.ARES.models;

public class SearchQuery {
	private boolean includeStory = false;
	private String query;
	private int maxHits;
	private int offset;
	private	String documentCreatedAfter;

	SearchQuery() { }

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getDocumentCreatedAfter() {
		return documentCreatedAfter;
	}

	public void setDocumentCreatedAfter(String documentCreatedAfter) {
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
