package com.retriever.ARES;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.retriever.spring.config.RetrieverConfig;

@EntityScan
@EnableJpaRepositories
@SpringBootApplication
@Import(RetrieverConfig.class)
public class ARESApplication {
	
	protected ARESApplication() { }

	public static void main(String[] args) {
		SpringApplication.run(ARESApplication.class, args);
	}
}
