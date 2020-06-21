/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.service;

import com.decagon.tradeapp.Repository.TradeRepository;
import com.decagon.tradeapp.entity.Trade;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author austine.okoroafor
 */

@Service
public class TradeService {
    @Autowired
    TradeRepository repo;
    
    public List<Trade> getAllTransactionPurchase(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);

        Page<Trade> pagedResult = repo.transactionQuery(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Trade>();
        }
    }
    
}
