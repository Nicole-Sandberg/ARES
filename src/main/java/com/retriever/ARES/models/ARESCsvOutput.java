package com.retriever.ARES.models;


import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ARESCsvOutput implements Comparable<ARESCsvOutput> {
	private	String name;
	private	int hits = 1;
	private List<String> cells = new ArrayList<>();
	private String yearAndMonth;
	
	public ARESCsvOutput() {
			
		}

	public ARESCsvOutput(String name, String yearAndMonth, String cell) {
		this.name = name;
		this.yearAndMonth = yearAndMonth;
		this.cells.add(cell);
	}


	public String getCsvLine() {
		StringJoiner joiner = new StringJoiner(",")
				.add(name)
				.add(yearAndMonth)
				.add(String.valueOf(hits));
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
	// TODO: 2018-03-13 It is strongly recommended, but not strictly required that
	// (x.compareTo(y)==0) == (x.equals(y)).
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public String getName() {
		return name;
	}
	public void setHits(int hits) {
			this.hits = hits;
		}

	public int getHits() {
		return hits;
	}

	public List<String> getCells() {
		return cells;
	}

	public String getYearAndMonth() {
		return yearAndMonth;
	}

	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}
	public void setCells(String cell) {
		this.cells.add(cell);
	}
	public String toString() {
			return String.format("%s (%d)", name, cells.size());
		}

		public void setName(String name) {
				this.name = name;
		}
}
