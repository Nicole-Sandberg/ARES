package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Report {
    private String year;
    private String from_month;
    private String length_in_month;
    private List<Page> pages;

    public Report(@JsonProperty("year") String year,
                  @JsonProperty("from_month") String from_month,
                  @JsonProperty("length_in_month")String length_in_month,
                  @JsonProperty("pages") List<Page> pages){
        this.year = year;
        this.from_month = from_month;
        this.length_in_month = length_in_month;
        this.pages = pages;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFrom_month() {
        return from_month;
    }

    public void setFrom_month(String from_month) {
        this.from_month = from_month;
    }

    public String getLength_in_month() {
        return length_in_month;
    }

    public void setLength_in_month(String length_in_month) {
        this.length_in_month = length_in_month;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
