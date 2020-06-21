/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.util;

import com.decagon.tradeapp.dto.Response;
import com.decagon.tradeapp.dto.Error;
import com.decagon.tradeapp.dto.PageResponse;
import com.decagon.tradeapp.entity.Trade;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


/**
 *
 * @author austine.okoroafor
 */

@Component
@Slf4j
public class AppUtil {
    
   public ResponseEntity<Response> returnErrorResponse(List<Error> errors, HttpStatus code)
	{
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.ERROR);
	    response.setErrors(errors);
	    return new ResponseEntity<Response>(response, httpHeaders, code);
	}
	
	public ResponseEntity<Response> returnErrorResponse(String error, HttpStatus code)
	{
		List<Error> errors = new ArrayList<Error>();
		errors.add(new Error(error, 4));
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.ERROR);
	    response.setErrors(errors);
	    return new ResponseEntity<Response>(response, httpHeaders, code);
	}
	
	public ResponseEntity<Response> returnFailedResponse(String error, HttpStatus code)
	{
		List<Error> errors = new ArrayList<Error>();
		errors.add(new Error(error, 5));
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.FAILED);
	    response.setErrors(errors);
	    return new ResponseEntity<Response>(response, httpHeaders, code);
	}
	
	public ResponseEntity<Response> returnStatusResponse()
	{
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.SUCCESS);
	    response.setCode(ConstantsUtil.SUCCESS_CODE);
	    return new ResponseEntity<Response>(response, httpHeaders, HttpStatus.OK);
	}
	
	public ResponseEntity<Response> returnSuccessResponse(Object responseObj, String message)
	{
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.SUCCESS);
	    response.setCode(ConstantsUtil.SUCCESS_CODE);
	    response.setMessage(message);
	    response.setResponse(responseObj);
	    return new ResponseEntity<Response>(response, httpHeaders, HttpStatus.OK);
	}
        public ResponseEntity<PageResponse> returnSuccessResponse(List<Trade> allStockPurcahse , Integer currentPage,Integer per_page,Integer total, Integer total_pages) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
       PageResponse response = new PageResponse();
       response.setPage(currentPage);
         response.setPer_page(per_page);
        response.setTotal(total);
         response.setTotal_pages(total_pages);
         response.setData(allStockPurcahse);
        
        return new ResponseEntity<PageResponse>( response, httpHeaders, HttpStatus.OK);
    }
	
	public boolean isEmptyString(String str) 
	{
		return null == str || "".equalsIgnoreCase(str.trim()) || "null".equalsIgnoreCase(str);
	}

	public ResponseEntity<Response> returnPostValidationErrors(Errors errors)
	{
		log.debug("Returning post validation errors...");
		List<FieldError> fields = errors.getFieldErrors();
        List<Error> errorList = new ArrayList<Error>();
        Iterator<FieldError> eIt = fields.iterator();
        while(eIt.hasNext())
        {
        	FieldError fe = eIt.next();
        	errorList.add(getFieldRequiredError(fe.getField()));
        }
        return returnErrorResponse(errorList, HttpStatus.BAD_REQUEST);
	}
	
	public Error getFieldRequiredError(String field)
	{
		return new Error(ConstantsUtil.FIELD_REQUIRED_MESSAGE.replace("{}", field), 4, field);
	}
	
	public String getFieldFromGenericError(String error)
	{
		if(null == error) return null;
		if(error.contains("Required String parameter")) {
			String[] arr = error.split("'");
			if(arr.length > 2) return arr[1];
		}
		return null;
	}

	public ResponseEntity<Response> returnSystemError(Exception e)
	{
		log.error("Exception while processing request: ", e);
		List<Error> errors = new ArrayList<Error>();
		errors.add(new Error("Something went wrong. Exception: " + e.getLocalizedMessage(), 5));
		Response response = new Response();
	    response.setStatus(ConstantsUtil.FAILED);
	    response.setErrors(errors);
	    
	    final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    
	    return new ResponseEntity<Response>(response, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private JsonNode getJsonObjectFromResponse(String response)
	{
		if(null == response) return null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response);
			return rootNode;
		}
		catch (IOException e) {
			log.error("Exception while retrieving object from response.", e);
			return null;
		}
	}

	

	public ResponseEntity<?> returnFailedResponse(List<Error> invalidSubscriptionMessage, HttpStatus badRequest) {
		// TODO Auto-generated method stub
		return null;
	}
        
        public ResponseEntity<Response> Sucesss(String error, HttpStatus code)
	{
		List<Error> errors = new ArrayList<Error>();
		errors.add(new Error(error, 5));
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	    Response response = new Response();
	    response.setStatus(ConstantsUtil.FAILED);
	    response.setErrors(errors);
	    return new ResponseEntity<Response>(response, httpHeaders, code);
	}
    
}
