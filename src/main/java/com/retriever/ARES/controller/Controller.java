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

	@RequestMapping("test")
	public void getExcel(@CurrentAccount Account account, HttpServletResponse response) {
		// TODO: 2018-03-13 fält som ska sökas på = headers förutom företag
		// TODO: 2018-03-13 match med ord och DocValueFormat.DateTime processingDateDate
		// TODO: 2018-03-13 resultat = företag med träffar på query med främst datum
		String header = "företag,antal träffar,abc,verksamhetbla,xyz,söksträng2\n";

		List<ARESCsvOutput> results = new ArrayList<>();
		results.add(new ARESCsvOutput("5560125793",  4, Arrays.asList(
				"X", "X", "X", "X")));
		results.add(new ARESCsvOutput("5560125790",  3, Arrays.asList(
				"X", "X", "O", "X")));
		results.add(new ARESCsvOutput("5560125799",  1, Arrays.asList(
				"X", "X", "O", "X")));
		results.add(new ARESCsvOutput("5560125791",  1, Arrays.asList(
				"O", "O", "O", "X")));
		results.add(new ARESCsvOutput("5560125792",  2, Arrays.asList(
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

	@RequestMapping("search")
	public ResponseEntity<SearchResponseARES> search(@CurrentAccount Account account,
													@RequestBody SearchQuery query) {
		SearchResponseARES response = getData(query);
		ResponseEntity<SearchResponseARES> result =
				new ResponseEntity<>(response, HttpStatus.OK);
		return result;
	}

	private SearchResponseARES getData(SearchQuery query) {
		SearchResponseARES result = searchService.search(query)
				.map(response -> new SearchResponseARES(query,
						Arrays.asList(response)))
				.orElse(new SearchResponseARES());
		return result;

	}
}
