/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 *
 * @author austine.okoroafor
 */
@Data
public class Response {
    
    	@ApiModelProperty(notes="The status of the API request: success|failed|error")
	String status;
	
	@ApiModelProperty(notes="The response code")
	Integer code;
	
	@ApiModelProperty(notes="The response message", required=false)
	String message;
	
	@ApiModelProperty(notes="The response data as received from the remote host", required=false)
	Object response;

	@ApiModelProperty(notes="The list of errors encountered while processing this request, if any.", required=false)
	List<Error> errors;
    
}
