package com.retriever.ARES.models.mapping;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pages {
    private String story;
    private String page;

    public Pages(@JsonProperty("story") String story,
                 @JsonProperty("page") String page){
        this.story = story;
        this.page = page;
    }
}
