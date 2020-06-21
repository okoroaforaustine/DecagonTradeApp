/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.Repository;

import com.decagon.tradeapp.entity.AppUser;
import com.decagon.tradeapp.entity.Trade;
import com.decagon.tradeapp.entity.Wallet;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author austine.okoroafor
 */
@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>{
    
  
     
     @Query(value = "select a.tradeid,a.company_name,a.istrade,a.latest_source,a.price,a.quantity,a.symbol,a.create_date,b.id,b.username,b.firstname,b.lastname  from trade a join app_user b on a.id=b.id where a.istrade=0 ",  nativeQuery = true)
   Page<Trade> transactionQuery(Pageable pageable);
    
}
