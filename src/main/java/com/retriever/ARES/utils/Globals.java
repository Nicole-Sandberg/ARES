package com.retriever.ARES.utils;


import java.util.Arrays;

public class Globals {
	private Globals() {
	}

	public static final String INDEX = "annreport.se.v1";
	public static final String TYPE = "retriever";
	public static final String PATH_ONE = "report";
	public static final String PATH_TWO = "report.pages";
	public static final String MATCH_FIELD = "report.pages.story";

	private static final String[] FIELDS = new String[] {
			"companyName",
			"orgnr",
			"report.year",
			"report.from_month",
			"report.length_in_month",
			"report.pages.story",
			"report.pages.page"

	};

	public static String[] getFIELDS() {
		return Arrays.copyOf(FIELDS, FIELDS.length);
	}
}
