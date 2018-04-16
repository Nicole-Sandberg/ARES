package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.search.DocValueFormat;

import java.util.Arrays;
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
	private List<InnerHitObject> innerHitsReport;
	private String[] matchedQueries;




	public Company(@JsonProperty("anst_x") String amountEmployee,
			@JsonProperty("ftgnamn") String companyName,
			@JsonProperty("oms_x") String revenue,
			@JsonProperty("orgnr") String orgnr,
			@JsonProperty("processingdate")
							DocValueFormat.DateTime processingDate,
			@JsonProperty("sate_lan") String county,
			@JsonProperty("report") List<Report> reports
					) {
		this.amountEmployee = amountEmployee;
		this.revenue = revenue;
		this.companyName = companyName;
		this.orgnr = orgnr;
		this.processingDate = processingDate;
		this.reports = reports;
		this.county = county;
	}

	public Company(List<InnerHitObject> hitObjects) {
		this.innerHitsReport = hitObjects;
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


	public void addInnerHits(List<InnerHitObject> hitObjects) {
		this.innerHitsReport = hitObjects;
	}

		public List<InnerHitObject> getInnerHitsReport() {
				return innerHitsReport;
		}

		public void setInnerHitsReport(List<InnerHitObject> innerHitsReport) {
				this.innerHitsReport = innerHitsReport;
		}

		public void setAmountEmployee(String amountEmployee) {
				this.amountEmployee = amountEmployee;
		}

		public void setRevenue(String revenue) {
				this.revenue = revenue;
		}

		public void setProcessingDate(DocValueFormat.DateTime processingDate) {
				this.processingDate = processingDate;
		}

		public String getAmountEmployee() {
				return amountEmployee;
		}

		public String getRevenue() {
				return revenue;
		}

		public DocValueFormat.DateTime getProcessingDate() {
				return processingDate;
		}

		public void addNameOfHits(String[] matchedQueries) {
			this.matchedQueries = Arrays.copyOf(matchedQueries, matchedQueries.length);
		}

		public String[] getMatchedQueries() {
				return Arrays.copyOf(matchedQueries, matchedQueries.length);
		}


}
