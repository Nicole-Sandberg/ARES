package com.retriever.ARES;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.catalina.core.ContainerBase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.retriever.spring.config.dao.AuthenticationDao;
import com.retriever.spring.config.model.Account;
import com.retriever.spring.config.security.MockSessionIdAuthenticationFilter;
import com.retriever.spring.config.util.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ARESApplication.class,
		webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ARESApplicationTest {
	
	@Value("/${server.servletPath}/")
	private String baseUrl;
	@Autowired
	private TestRestTemplate template;
	@Autowired
	private AuthenticationDao authenticationDao;
	
	@Test
	public void healthEndpoint_withoutAuthentication_statusOk() {
		ResponseEntity<Object> response = doGet("health");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void authenticatedEndpoint_withAuthentication_statusOk() {
		Account account = MockSessionIdAuthenticationFilter.MOCK_ACCOUNT;
		String path = "auditevents?sessionId=" + account.getSessionId();
		ResponseEntity<Object> response = doGet(path);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void authenticatedEndpoint_withoutAuthentication_notOk() {
		TestUtils.disableLogging(() -> {
			ResponseEntity<Object> response = doGet("auditevents");
			assertNotEquals(HttpStatus.OK, response.getStatusCode());
		}, ContainerBase.class);
	}
	
	private ResponseEntity<Object> doGet(String path) {
		return template.getForEntity(baseUrl + path, Object.class);
	}

}
