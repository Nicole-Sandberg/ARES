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
}
