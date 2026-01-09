package com.example.support.model;

public class ChatResponse {

    private String response;
    private String matchedRule;

    public ChatResponse() {
    }

    public ChatResponse(String response, String matchedRule) {
        this.response = response;
        this.matchedRule = matchedRule;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMatchedRule() {
        return matchedRule;
    }

    public void setMatchedRule(String matchedRule) {
        this.matchedRule = matchedRule;
    }
}
