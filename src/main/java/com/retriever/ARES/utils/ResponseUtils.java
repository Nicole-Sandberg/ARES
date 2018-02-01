package com.retriever.ARES.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retriever.ARES.models.mapping.Company;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public class ResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public static List<Company> parseHits(SearchResponse response){
        return Arrays.stream(response.getHits().getHits())
                .map(hit -> {
                    try {
                        Company searchResultObject = mapper.readValue(hit
                                .getSourceAsString(), Company.class);
                        return searchResultObject;
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("failed parse hit");
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());

    }


}
