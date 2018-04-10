package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.search.DocValueFormat;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {
	private String amountEmployee;
	private String companyName;
	private String revenue;
	private String orgnr;
	private DocValueFormat.DateTime processingDate;
	private List<Report> reports;
	private String county;

	public Company(@JsonProperty("anst_x") String amountEmployee,
			@JsonProperty("ftgnamn") String companyName,
			@JsonProperty("oms_x") String revenue,
			@JsonProperty("orgnr") String orgnr, @JsonProperty("processingdate")
							DocValueFormat.DateTime processingDate,
			@JsonProperty("sate_lan") String county,
			@JsonProperty("report") List<Report> reports) {
		this.amountEmployee = amountEmployee;
		this.revenue = revenue;
		this.companyName = companyName;
		this.orgnr = orgnr;
		this.processingDate = processingDate;
		this.reports = reports;
				this.county = county;
	}

		public String getCounty() {
				return county;
		}

		public void setCounty(String county) {
				this.county = county;
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
