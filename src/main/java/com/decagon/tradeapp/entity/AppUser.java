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
import javax.validation.constraints.NotEmpty;


/**
 *
 * @author austine.okoroafor
 */
@Entity
@Data
@Table(name = "AppUser")

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(notes = "UserName for logging into this platform")
    @NotEmpty(message = "Username is required!")
    private String username;

    @ApiModelProperty(notes = "Password for logging into this platform")
    @NotEmpty(message = "Password is required!")
    private String password;

    @ApiModelProperty(notes = "Trader firstname")
    @NotEmpty(message = "firstnamee is required!")
    private String firstname;
    @ApiModelProperty(notes = "Trader lastname")
    @NotEmpty(message = "lastname is required!")
    private String lastname;
    @ApiModelProperty(notes = "Trader email")
    @NotEmpty(message = "email is required!")
    private String email;
    @Column(name = "Create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfCreation;
    
   

}
