package com.retriever.ARES.controller;
import com.retriever.ARES.models.ARESCsvOutput;
import com.retriever.ARES.models.SearchQuery;
import com.retriever.ARES.models.SearchResponseARES;
import com.retriever.ARES.services.ElasticsearchService;
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

@RestController
public class Controller {
	private static final Logger log = LoggerFactory.getLogger(ElasticsearchService.class);

	@Autowired
	ElasticsearchService searchService;

	/**
	 * metod som testar named queries i en nestad query. Ej fått detta att fungera.
	 * fungerar med onestlande boolquerys
	 * @param account Account
	 * @param query SearchQuery
	 * @return ResponseEntity<SearchResponseARES>
	 */
//----------------------test Named queries---------------------------
	@RequestMapping("testName")
	public ResponseEntity<SearchResponseARES> testName(@CurrentAccount Account account,
														@RequestBody SearchQuery query) {
		SearchResponseARES result = searchService.test(query)
				.map(response -> new SearchResponseARES(query.getOffset(),
                        Collections.singletonList(response)))
				.orElse(new SearchResponseARES());
        return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * metod som skriver ut CSV-fil till kund. sorterad efter flest träffar per sök.
	 * sökmetod ej klar kolla umeå
	 * @param account Account
	 * @param response HttpServletResponse
	 */
//--------------------------test Umeå CSV---------------------------
	@RequestMapping("test")
	public void getExcel(@CurrentAccount Account account, HttpServletResponse response) {
		String header = "företag,antal träffar,abc,verksamhetbla,xyz,söksträng2\n";

		List<ARESCsvOutput> results = new ArrayList<>();
		results.add(new ARESCsvOutput("5560125793",	4, Arrays.asList(
				"X", "X", "X", "X")));
		results.add(new ARESCsvOutput("5560125790",	3, Arrays.asList(
				"X", "X", "O", "X")));
		results.add(new ARESCsvOutput("5560125799",	1, Arrays.asList(
				"X", "X", "O", "X")));
		results.add(new ARESCsvOutput("5560125791",	1, Arrays.asList(
				"O", "O", "O", "X")));
		results.add(new ARESCsvOutput("5560125792",	2, Arrays.asList(
				"X", "O", "O", "X")));

		Collections.sort(results);
		try {
			response.setContentType("application/csv");
			response.setHeader("content-disposition",
					"attachment;filename =arsrapport-retriever.csv");
			OutputStream out = response.getOutputStream();
			out.write(header.getBytes(Charset.forName("UTF-8")));
			final ARESCsvOutput object = new ARESCsvOutput(
					"Object", 4, Arrays.asList("X", "X", "X", "X"));
			results.add(object);
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
	 * @param account Account
	 * @param query SearchQuery
	 * ex:
	 * "query" : "betydande tvivel",
	"documentCreatedAfter" : "2018-03-03T09:06:04",
	"maxHits" : 10,
	"includeStory" : false,
	"offset" : 0
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
	 * metod som söker med multi nestlade querys och ger innerhits(report år och månad)
	 * obs måste vara method post när man skickar in en lista
	 * @param account Account
	 * @param query List<SearchQuery>
	 * @return ResponseEntity<SearchResponseARES>
	 */

	//-------------------------------TEST 2 umeå--------------------------------------
	@RequestMapping(value = "searchUmea", method = RequestMethod.POST)
	public ResponseEntity<SearchResponseARES> searchUmea(
			@CurrentAccount Account account, @RequestBody List<SearchQuery> query) {
		SearchResponseARES response = getDataUmea(query.get(0));
//		SearchResponseARES responseTwo = getDataUmea(query.get(1));
		Map<String, List<SearchResponseARES>> resultsMap = new HashMap<>();
		resultsMap.put(response.getResults().get(0).getCompanyName(), Collections.singletonList(response));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private SearchResponseARES getDataUmea(SearchQuery query) {
		return searchService.searchUmea(query)
				.map(response -> new SearchResponseARES(query.getOffset(),
						Collections.singletonList(response)))
				.orElse(new SearchResponseARES());

	}

}
