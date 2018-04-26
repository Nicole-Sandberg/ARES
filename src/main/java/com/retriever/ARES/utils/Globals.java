package com.retriever.ARES.utils;


import java.util.Arrays;

 class Globals {
	private Globals() {
	}

	static final String INDEX = "annreport.se.v5";
	static final String TYPE = "retriever";
	static final String PATH_ONE = "pages";
	// static final String PATH_TWO = "report.pages"; <-- för index 4
	static final String MATCH_FIELD = "pages.story";

	private static final String[] FIELDS = new String[] {
			"ftgnamn",
			"orgnr",
			"year",
			"from_month",
			"length_in_month",
			"pages.page"
	};
	private static final String[] UMEASEARCHRESULTFIELDS = new String[]{
			"ftgnamn",
			"orgnr",
			"year",
			"from_month"
	};
	private static final String[] FIELDSANDSTORY = new String[] {
			"ftgnamn",
			"orgnr",
			"year",
			"from_month",
			"length_in_month",
			"pages.story",
			"pages.page"
	};
	static String[] getFIELDS(boolean includeStory) {
		String[] fields	= includeStory ? FIELDSANDSTORY : FIELDS;
		return Arrays.copyOf(fields, fields.length);
	}
	static String[] getUmeasearchresultfields() {
		return Arrays.copyOf(UMEASEARCHRESULTFIELDS, UMEASEARCHRESULTFIELDS.length);
	}
}
