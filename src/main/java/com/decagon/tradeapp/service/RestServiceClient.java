/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.service;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author austine.okoroafor
 */
@Service
public class RestServiceClient {
    @Autowired
    private RestTemplate restTemplate;
    public String getCurrentStock(String symbol){
        String APIkey="pk_871a0f5d4e1344d5af3e436d014579e5";
     String Url = "https://cloud-sse.iexapis.com/stable/stock/"+symbol+"/quote?token="+APIkey;
        
        ResponseEntity<String> response = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            response = restTemplate.exchange(Url, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();

        }
        return "{\"status\":false, \"message\":\"Please check your internet connection\", \"data\":null}";
    }
    
    
}
