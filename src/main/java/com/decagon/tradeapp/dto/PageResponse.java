/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.dto;

import com.decagon.tradeapp.entity.Trade;
import java.util.List;
import lombok.Data;

/**
 *
 * @author austine.okoroafor
 */
@Data
public class PageResponse {
    
     int page;
     int per_page;
     int total;
     int total_pages;
     List<Trade>  data;
}
