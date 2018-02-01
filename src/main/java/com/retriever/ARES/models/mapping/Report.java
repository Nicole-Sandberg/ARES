package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.retriever.ARES.models.mapping.Pages;

public class Report {

    private String year;
    private String from_month;
    private String length_in_month;
    private Pages pages;

    public Report(@JsonProperty("year") String year,
                  @JsonProperty("from_month") String from_month,
                  @JsonProperty("length_in_month")String length_in_month,
                  @JsonProperty("pages") Pages pages){
        this.year = year;
        this.from_month = from_month;
        this.length_in_month = length_in_month;
        this.pages = pages;
    }
}
