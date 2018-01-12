package com.retriever.ARES.controller;

import static java.util.Collections.singletonMap;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retriever.spring.config.CurrentAccount;
import com.retriever.spring.config.CurrentEmailLogin;
import com.retriever.spring.config.model.Account;
import com.retriever.spring.config.model.EmailLogin;

@RestController
public class Controller {
	
	@RequestMapping("account")
	public Map<String, Object> getName(@CurrentAccount Account account) {
		return singletonMap("name", account.getName());
	}
	
	@RequestMapping("emailLogin")
	public Map<String, Object> getName(@CurrentEmailLogin EmailLogin emailLogin) {
		return singletonMap("email", emailLogin.getEmail());
	}
}
