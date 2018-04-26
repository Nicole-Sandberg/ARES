package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.search.DocValueFormat;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {
	
	private String companyName;
	private String orgnr;
	private List<InnerHitObject> innerHitsReport;
	private String[] matchedQueries;
	private String amountEmployee;
	private String revenue;
	private DocValueFormat.DateTime processingDate;
	private String county;
	private String year;
	private String month;
	private String date;
	private List<Page> pages;



	public Company(@JsonProperty("anst_x") String amountEmployee,
			@JsonProperty("ftgnamn") String companyName,
			@JsonProperty("oms_x") String revenue,
			@JsonProperty("orgnr") String orgnr,
			@JsonProperty("processingdate")
							DocValueFormat.DateTime processingDate,
			@JsonProperty("sate_lan") String county,
			@JsonProperty("year") String year,
			@JsonProperty("from_month") String month,
			@JsonProperty("length_in_month") String date,
			@JsonProperty("pages") List<Page> pages
					) {
		this.amountEmployee = amountEmployee;
		this.revenue = revenue;
		this.companyName = companyName;
		this.orgnr = orgnr;
		this.processingDate = processingDate;
		this.county = county;
		this.year = year;
		this.month = month;
		this.date = date;
		this.pages = pages;
	}

	public Company(List<InnerHitObject> hitObjects) {
		this.innerHitsReport = hitObjects;
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

	public void setMatchedQueries(String[] matchedQueries) {
		this.matchedQueries = Arrays.copyOf(matchedQueries, matchedQueries.length);
	}

	public String[] getMatchedQueries() {
			return Arrays.copyOf(matchedQueries, matchedQueries.length);
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}
}
