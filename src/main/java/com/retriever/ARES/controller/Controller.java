package com.retriever.ARES.controller;



import com.retriever.ARES.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retriever.spring.config.CurrentAccount;
import com.retriever.spring.config.model.Account;

@RestController
public class Controller {

	@Autowired
	SearchService searchService;


	@RequestMapping("search")
	public void search(@CurrentAccount Account account) {
		searchService.search(null);
	}
}
