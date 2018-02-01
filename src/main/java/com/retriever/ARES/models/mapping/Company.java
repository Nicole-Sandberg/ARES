package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {
    private String companyName;
    private String orgnr;
    private Report report;

    public Company(@JsonProperty("companyName") String companyName,
                   @JsonProperty("orgnr") String orgnr,
                   @JsonProperty("report") Report report){
        this.companyName = companyName;
        this.orgnr = orgnr;
        this.report= report;
    }




}
