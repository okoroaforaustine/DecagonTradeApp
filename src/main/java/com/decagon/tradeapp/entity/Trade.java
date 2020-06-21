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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import lombok.Data;

/**
 *
 * @author austine.okoroafor
 */
@Data
@Table(name = "Trade")
@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tradeid;
    
   
    @NotNull(message = "price is required!")
    private Double price;
     @ApiModelProperty(notes = "latestSource is required")
    @NotEmpty(message = "latestSource is required!")
    private String latestSource;
      @ApiModelProperty(notes = "companyName is required")
    @NotEmpty(message = "comapnyName is required!")
    private String companyName;
      @ApiModelProperty(notes = "istrade is required")
    @NotNull(message = "istrade is required!")
   private int istrade;
      @ApiModelProperty(notes = "Quantity is required")
    @NotEmpty(message = "Quantity is required!")
    private String quantity;
      @ApiModelProperty(notes = "Symbol is required")
    @NotEmpty(message = "Symbol is required!")
    private String symbol;
    private @Column(name = "Create_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date transactionDate;
     @ManyToOne
    @JoinColumn(name = "id")
    AppUser trader;

}
