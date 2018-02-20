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
			"companyname",
			"orgnr",
			"report.year",
			"report.from_month",
			"report.length_in_month",
			"report.pages.page"
	};
	private static final String[] FIELDSANDSTORY = new String[] {
			"companyname",
			"orgnr",
			"report.year",
			"report.from_month",
			"report.length_in_month",
			"report.pages.story",
			"report.pages.page"
	};
	private static final String[]keys = new String[] {
			"AND",
			"OR",
			"ANDNOT",
			"("
	};
	public static String[] getFIELDS(boolean includeStory) {
		String[] fields	= includeStory ? FIELDSANDSTORY : FIELDS;
		return Arrays.copyOf(fields, fields.length);
	}

	public static String[] getKeys() {
		return Arrays.copyOf(keys, keys.length);
	}
}
