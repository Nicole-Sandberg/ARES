package com.retriever.ARES.models;

import java.util.List;
import java.util.StringJoiner;

public class ARESCsvOutput implements Comparable<ARESCsvOutput> {
	private final String name;
	private final int hits;
	private final List<String> cells;

	public ARESCsvOutput(String name, int hits, List<String> cells) {
		this.name = name;
		this.hits = hits;
		this.cells = cells;
	}

	public String getCsvLine() {
		StringJoiner joiner = new StringJoiner(",")
				.add(name).add(String.valueOf(hits));
		for (String cell : cells) {
			joiner.add(cell);
		}
		joiner.add("\n");
		return joiner.toString();
	}

	@Override
	public int compareTo(ARESCsvOutput other) {
		return this.hits != other.hits ?
				other.hits - this.hits : other.name.compareTo(this.name);
	}

	// TODO: 2018-03-13 detta tar bort findbugs error EQ_COMPARETO_USE_OBJECT_EQUALS. 
	// TODO: 2018-03-13 It is strongly recommended, but not strictly required that (x.compareTo(y)==0) == (x.equals(y)).
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
