package com.chatmate.chatmate.dto;

public class Response {
    public Response(String response) {
        this.response = response;
    }

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
