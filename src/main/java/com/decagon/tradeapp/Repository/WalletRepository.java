/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.Repository;

import com.decagon.tradeapp.entity.AppUser;
import com.decagon.tradeapp.entity.Wallet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author austine.okoroafor
 */
@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long>{
    
    Double findByBalance(Double balance);
     @Query(value = "select a.wallet_id,a.balance, a.credit, a.debit,b.username,a.create_date,a.id,a.username,a.firstname,a.lastname from wallet a join   app_user b on a.id=b.id where b.username=:username",  nativeQuery = true)
     Wallet transactionQuery(@Param("username") String username);
     
      @Query(value = "select a.wallet_id,a.balance, a.credit, a.debit,b.username,a.create_date,a.id,a.username,a.firstname,a.lastname from wallet a join   app_user b on a.id=b.id where b.trans_date between b.trans_date: startdate  and  b.trans_date: endDate  ",  nativeQuery = true)
    List<Wallet>transactionQueryAll(@Param("startdate") String startdate,@Param("endDate") String endDate);
}
