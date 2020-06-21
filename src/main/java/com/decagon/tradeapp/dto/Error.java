/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.dto;

import lombok.Data;

/**
 *
 * @author austine.okoroafor
 */
@Data
public class Error {
    
    private int code;
	
	private String description;
	
	private String field;

	public Error(String description, int code, String field) {
		this.description = description;
		this.code = code;
		this.field = field;
	}
	
	public Error(String description, int code) {
		this.description = description;
		this.code = code;
	}
	
	public Error(String description, String field) {
		this.description = description;
		this.field = field;
	}
    
}
