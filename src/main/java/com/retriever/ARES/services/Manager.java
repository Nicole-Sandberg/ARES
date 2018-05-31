package com.retriever.ARES.services;

import com.retriever.ARES.models.ARESCsvOutput;
import com.retriever.ARES.models.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Manager {
	private static final Logger log = LoggerFactory.getLogger(ElasticsearchService.class);


	public void getExcel(HttpServletResponse response, List<ARESCsvOutput> results) {
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

	public List<SearchQuery> getUmeaQueries() {
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

}
