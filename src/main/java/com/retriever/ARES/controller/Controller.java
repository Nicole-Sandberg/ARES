package com.retriever.ARES.controller;
import com.retriever.ARES.models.SearchQuery;
import com.retriever.ARES.models.SearchResponseARES;
import com.retriever.ARES.services.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.retriever.spring.config.CurrentAccount;
import com.retriever.spring.config.model.Account;
import java.util.Arrays;

@RestController
public class Controller {

	@Autowired
	ElasticsearchService searchService;

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
