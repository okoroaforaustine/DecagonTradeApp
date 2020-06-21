/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.entity;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

/**
 *
 * @author austine.okoroafor
 */
@Data
@Table(name = "Wallet")
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long wallet_id;
     private Double credit;
     private Double debit;
     private Double balance;
    private @Column(name = "Create_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date last_update;
     @ManyToOne
    @JoinColumn(name = "id")
    AppUser trader;
    
}
