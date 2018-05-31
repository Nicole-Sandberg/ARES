package com.retriever.ARES.controller;

import com.retriever.ARES.models.ARESCsvOutput;
import com.retriever.ARES.models.SearchQuery;
import com.retriever.ARES.models.SearchResponseARES;
import com.retriever.ARES.services.ElasticsearchService;
import com.retriever.ARES.services.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.retriever.spring.config.CurrentAccount;
import com.retriever.spring.config.model.Account;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Controller {
	private static final Logger log = LoggerFactory.getLogger(ElasticsearchService.class);

	final
	ElasticsearchService searchService;
	final
	Manager manager;

	@Autowired
	public Controller(ElasticsearchService searchService, Manager manager) {
		this.searchService = searchService;
		this.manager = manager;
	}

	/**
	 * metod som skriver ut CSV-fil till kund. sorterad efter flest träffar per sök.
	 * sök genom test 2 searchUMEA
	 *
	 * @param account Account
	 * @param response HttpServletResponse
	 * @param results List<ARESCsvOutput>
	 */
//--------------------------test Umeå CSV---------------------------
	@RequestMapping("getExcel")
	public void getExcel(@CurrentAccount Account account,
						HttpServletResponse response, List<ARESCsvOutput> results) {
		String header = "Company,Year and month,Amount of hits,Hit 1,Hit 2,Hit 3\n";

		Collections.sort(results);
		try {
			response.setContentType("application/csv");
			response.setHeader("content-disposition",
					"attachment;filename =arsrapport-retriever.csv");
			OutputStream out = response.getOutputStream();
			out.write(header.getBytes(Charset.forName("UTF-8")));
			results.forEach(company -> {
				try {
					out.write(company.getCsvLine().getBytes(Charset.forName("UTF-8")));
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			});
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * använder elasticsearch query string querybuilder som parsar query efter AND,OR,NOT
	 *
	 * @param account Account
	 * @param query SearchQuery
	 *				ex:
	 *				"query" : "betydande tvivel",
	 *				"documentCreatedAfter" : "2018-03-03T09:06:04",
	 *				"maxHits" : 10,
	 *				"includeStory" : false,
	 *				"offset" : 0
	 * @return ResponseEntity<SearchResponseARES>
	 */
//-------------- QueryStringQueryBuilder----------------------------
	@RequestMapping("search")
	public ResponseEntity<SearchResponseARES> search(@CurrentAccount Account account,
													@RequestBody SearchQuery query) {
		SearchResponseARES response = getData(query);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private SearchResponseARES getData(SearchQuery query) {
		return searchService.search(query)
				.map(response -> new SearchResponseARES(query.getOffset(),
						Collections.singletonList(response)))
				.orElse(new SearchResponseARES());

	}

	/**
	 * metod som söker med multi nestlade querys
	 *
	 * @param account Account
	 */

	//-------------------------------TEST 2 umeå--------------------------------------
	@RequestMapping(value = "searchUmea", method = RequestMethod.GET)
	public void searchUmea(
			@CurrentAccount Account account,
			HttpServletResponse httpServletResponse) {
		List<SearchQuery> queries = getUmeaQueries();
		List<SearchResponseARES> responses = queries.stream()
				.map(this::getDataUmea).collect(Collectors.toList());
		List<ARESCsvOutput> results = searchService.parseResults(responses);
		getExcel(account, httpServletResponse, results);
	}

	private List<SearchQuery> getUmeaQueries() {
		List<SearchQuery> queries = new ArrayList<>();
		int maxHits = 1000;
		String documentCreatedAfter = "2018-03-03T09:06:04";
		int offset = 0;

		SearchQuery query = createQuery("väsentlig osäkerhetsfaktor",
				maxHits, documentCreatedAfter, offset);
		queries.add(query);
		query = createQuery("betydande tvivel",
				maxHits, documentCreatedAfter, offset);
		queries.add(query);
		query = createQuery("fortsätta verksamheten",
				maxHits, documentCreatedAfter, offset);
		queries.add(query);
		return queries;
	}


	private SearchQuery createQuery(String query, int maxHits,
									String documentCreatedAfter, int offset) {
		return new SearchQuery(query, maxHits, documentCreatedAfter, offset);
	}

	private SearchResponseARES getDataUmea(SearchQuery query) {
		return searchService.searchUmea(query)
				.map(response -> new SearchResponseARES(query.getOffset(),
						Collections.singletonList(response)))
				.orElse(new SearchResponseARES());

	}

	/**
	 * metod som kör multisearch api för att göra en sökning med 34 sökord/fraser
	 * för umeå
	 * @param account Account
	 * @param httpServletResponse HttpServletResponse
	 */

	@RequestMapping(value = "multiSearch")
	public void multiSearch(@CurrentAccount Account account,
							HttpServletResponse httpServletResponse) {
		List<SearchResponseARES> responses = (List<SearchResponseARES>)
				searchService.searchMulti()
				.map(response -> new SearchResponseARES(0,
						Collections.singletonList(response)))
				.orElse(new SearchResponseARES());

		List<ARESCsvOutput> results = searchService.parseResults(responses);
		getExcel(account, httpServletResponse, results);
	}

}
