package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.search.DocValueFormat;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {
	private String companyName;
	private String orgnr;
	private DocValueFormat.DateTime processingDate;
	private List<Report> reports;

	public Company(@JsonProperty("companyname") String companyName,
			@JsonProperty("orgnr") String orgnr, @JsonProperty("processingdate")
							DocValueFormat.DateTime processingDate,
			@JsonProperty("report") List<Report> reports) {
		this.companyName = companyName;
		this.orgnr = orgnr;
		this.processingDate = processingDate;
		this.reports = reports;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getOrgnr() {
		return orgnr;
	}

	public void setOrgnr(String orgnr) {
		this.orgnr = orgnr;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
}
