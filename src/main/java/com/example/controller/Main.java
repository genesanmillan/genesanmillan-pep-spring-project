package com.example.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.SocialMediaApp;
import com.example.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        HttpClient webClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationContext app = SpringApplication.run(SocialMediaApp.class, args);
        Thread.sleep(500);
        try {

        // Test 1: Login with Valid Credentials
        String json1 = "{\"account_id\":0,\"username\":\"testuser1\",\"password\":\"password\"}";
        HttpRequest postRequest1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login"))
                .POST(HttpRequest.BodyPublishers.ofString(json1))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response1 = webClient.send(postRequest1, HttpResponse.BodyHandlers.ofString());
        printResult(1, response1);

        // Test 2: Login with Invalid Username
        String json2 = "{\"account_id\":9999,\"username\":\"testuser404\",\"password\":\"password\"}";
        HttpRequest postRequest2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login"))
                .POST(HttpRequest.BodyPublishers.ofString(json2))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response2 = webClient.send(postRequest2, HttpResponse.BodyHandlers.ofString());
        printResult(2, response2);

        // Test 3: Login with Invalid Password
        String json3 = "{\"account_id\":9999,\"username\":\"testuser1\",\"password\":\"pass404\"}";
        HttpRequest postRequest3 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/login"))
                .POST(HttpRequest.BodyPublishers.ofString(json3))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response3 = webClient.send(postRequest3, HttpResponse.BodyHandlers.ofString());
        printResult(3, response3);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printResult(int testNumber, HttpResponse<String> response) throws IOException {
        int status = response.statusCode();
        System.out.println("Test " + testNumber + " - Status Code: " + status);
        if (status == 200) {
            ObjectMapper om = new ObjectMapper();
            Account actualResult = om.readValue(response.body().toString(), Account.class);
            System.out.println("Actual Result: " + actualResult);
        } else {
            System.out.println("Response Body: " + response.body());
        }
        System.out.println();
    }
}
