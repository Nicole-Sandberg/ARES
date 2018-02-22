package com.retriever.ARES;

import com.retriever.ARES.utils.QueryBuilderUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.retriever.spring.config.RetrieverConfig;

import java.net.InetSocketAddress;
import java.util.List;

@EntityScan
@EnableJpaRepositories
@SpringBootApplication
@Import(RetrieverConfig.class)
public class ARESApplication {
	@Value("#{'${elasticsearch.url}'.split(',')}")
	private List<String> urls;
	@Value("${elasticsearch.port}")
	private Integer port;
	@Value("${elasticsearch.cluster.name}")
	private String clusterName;

	public static void main(String[] args) {
		String input = "håkan AND hellström OR båt";
		QueryStringQueryBuilder query = QueryBuilderUtils.parse(input);
		SpringApplication.run(ARESApplication.class, args);
	}

	@Bean
	public Client client() {
		Settings settings = Settings.builder()
				.put("cluster.name", clusterName)
				.put("client.transport.ping_timeout", "5s")
				.put("client.transport.nodes_sampler_interval", "5s")
				.put("client.transport.sniff", true).build();
		TransportClient client = new PreBuiltTransportClient(settings);

		urls.stream()
				.map(url -> new InetSocketAddress(url, port.intValue()))
				.map(InetSocketTransportAddress::new)
				.forEach(client::addTransportAddress);
		return client;
	}
}
