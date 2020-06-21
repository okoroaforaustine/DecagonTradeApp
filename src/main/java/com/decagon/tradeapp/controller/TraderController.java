/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.decagon.tradeapp.controller;

import javax.validation.Valid;
import com.decagon.tradeapp.Repository.AppUserRepository;
import com.decagon.tradeapp.Repository.TradeRepository;
import com.decagon.tradeapp.Repository.WalletRepository;
import com.decagon.tradeapp.dto.Response;
import com.decagon.tradeapp.dto.Error;
import com.decagon.tradeapp.entity.AppUser;
import com.decagon.tradeapp.entity.Trade;
import com.decagon.tradeapp.entity.Wallet;
import com.decagon.tradeapp.service.RestServiceClient;
import com.decagon.tradeapp.service.TradeService;
import com.decagon.tradeapp.util.AppUtil;
import com.decagon.tradeapp.util.ClientUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author austine.okoroafor
 */
@RestController
@RequestMapping("/trader")
@Api(value = "Trader-Controller", description = "All API operations on Trading app...")
@Slf4j
@CrossOrigin
public class TraderController {

    @Autowired
    AppUserRepository traderRepository;
    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TradeRepository tradeRepo;

    @Autowired
    AppUtil appUtils;

    @Autowired
    ClientUtils Utils;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RestServiceClient rest;
    
     @Autowired
    TradeService tradeService;

    @ApiOperation("Login.")
@PostMapping(value = "/login", produces = "Application/json", consumes = "Application/json")
public void fakeLogin(@ApiParam("username") @RequestParam String username, @ApiParam("password") @RequestParam String password) {
    throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
}

    
     
     
    @PostMapping(value = "/register", produces = "Application/json", consumes = "Application/json")

    @ApiOperation(value = "Register a new trader",
            notes = "Register a new trader in the system, provided all information are correct and trader username does not exist already..",
            response = Response.class)
    public ResponseEntity<?> saveUser(@Valid @RequestBody AppUser trader, @ApiIgnore Errors errors) {
        log.debug("Received request to register new trader.");

        if (errors.hasErrors()) {
            return appUtils.returnPostValidationErrors(errors);
        }

        List<Error> validationErrors = validateTrader(trader);
        if (!validationErrors.isEmpty()) {
            return appUtils.returnErrorResponse(validationErrors, HttpStatus.CONFLICT);
        }
        trader.setPassword(passwordEncoder.encode(trader.getPassword()));

        AppUser newTrader = traderRepository.save(trader);

        //newMerchant.setUser(clientUtils.createNewAppUser(newMerchant.getId().toString()));
        log.debug("New Trader registered successfully. Returning response...");

        return appUtils.returnSuccessResponse(newTrader, "You have Succesfully registered");
    }

    private List<Error> validateTrader(AppUser trader) {
        List<Error> errors = new ArrayList<Error>();

        log.debug("Validating trader request...");

        AppUser existingName = traderRepository.findByUsername(trader.getUsername());
        if (existingName != null) {
            errors.add(new Error("trader with this username '" + existingName.getUsername() + "' already exists.", 4, "name"));
        }

        log.debug("trader request validation completed...");

        return errors;
    }

    @GetMapping(value = "/currentStock/{symbol}", produces = "Application/json", consumes = "Application/json")
    @ApiOperation(value = "Retrive current Stock",
            notes = "trader user can retrive current stock by passing symbol e.g nflx",
            response = Response.class)
    public ResponseEntity<?> currentStock(@PathVariable("symbol") String symbol) throws IOException {

        if (symbol.equalsIgnoreCase("")) {
            return appUtils.returnFailedResponse("Stock symbol is empty", HttpStatus.BAD_REQUEST);
        }

        String resp = rest.getCurrentStock(symbol);

        return appUtils.returnSuccessResponse(resp, "Current Stock retrived Succesfully");

    }

    @PostMapping(value = "/buyStock", produces = "Application/json", consumes = "Application/json")

    @ApiOperation(value = "buy a stock",
            notes = "enable user to buy stock with current price of the stock",
            response = Response.class)
    public ResponseEntity<?> buyStock(@Valid @RequestBody Trade trade, @PathVariable("username") String SellerUsername, @ApiIgnore Errors errors) {
        log.debug("Received request to buy a stock.");

        if (errors.hasErrors()) {
            return appUtils.returnPostValidationErrors(errors);
        }

        String loginUserName = Utils.getCurrentUser();

        Wallet getDebitUser = walletRepository.transactionQuery(loginUserName);
        //  Wallet getDebitUser= walletRepository.transactionQuery(username);

        if (getDebitUser.getBalance() > trade.getPrice()) {

            return appUtils.returnFailedResponse("your account balance " + getDebitUser.getBalance() + " is too low to buy Stock", HttpStatus.BAD_REQUEST);
        }

        if (trade.getIstrade() == 1) {
            return appUtils.returnFailedResponse("You are suppose to be Buying a stock not Selling ", HttpStatus.NOT_FOUND);
        }
        //
        AppUser existingName = traderRepository.findByUsername(SellerUsername);
        if (existingName == null) {
            return appUtils.returnFailedResponse("The user you are buying from does not exist ", HttpStatus.NOT_FOUND);
        }
        String getUsername = existingName.getUsername();

        Wallet getCreditUser = walletRepository.transactionQuery(getUsername);

        Double doCredit = getCreditUser.getBalance() + trade.getPrice();

        Trade newTrade = tradeRepo.save(trade);
        Wallet creditUserWallet = new Wallet();
        creditUserWallet.setCredit(trade.getPrice());
        creditUserWallet.setBalance(doCredit);

        Double doDebit = getDebitUser.getBalance() - trade.getPrice();
        Wallet debitWallet = new Wallet();

        debitWallet.setBalance(doDebit);
        debitWallet.setDebit(trade.getPrice());

        walletRepository.save(debitWallet);
        walletRepository.save(creditUserWallet);

        return appUtils.returnSuccessResponse(newTrade, "Your buying of stock was Succesfull");
    }

    @PostMapping(value = "/sellStock/{sellerUsername}", produces = "Application/json", consumes = "Application/json")

    @ApiOperation(value = "sell stock",
            notes = "allow a user to sell shares of a stock that he or she owns",
            response = Response.class)
    public ResponseEntity<?> sellStock(@Valid @RequestBody Trade trade, @PathVariable("sellerUsername") String sellerUsername, @ApiIgnore Errors errors) {
        log.debug("Received request to register new trader.");

        if (errors.hasErrors()) {
            return appUtils.returnPostValidationErrors(errors);
        }
        String loginUserName = Utils.getCurrentUser();

        Wallet getDebitUser = walletRepository.transactionQuery(loginUserName);
        //  Wallet getDebitUser= walletRepository.transactionQuery(username);

        if (getDebitUser.getBalance() > trade.getPrice()) {

            return appUtils.returnFailedResponse("your account balance " + getDebitUser.getBalance() + " is too low to buy Stock", HttpStatus.BAD_REQUEST);
        }

        if (trade.getIstrade() == 0) {
            return appUtils.returnFailedResponse("You are suppose to be Buying a stock not Selling ", HttpStatus.NOT_FOUND);
        }
        //
        AppUser existingName = traderRepository.findByUsername(sellerUsername);
        if (existingName == null) {
            return appUtils.returnFailedResponse("The user you are Selling to does not exist ", HttpStatus.NOT_FOUND);
        }
        String getUsername = existingName.getUsername();

        Wallet getCreditUser = walletRepository.transactionQuery(getUsername);

        Double doCredit = getCreditUser.getBalance() + trade.getPrice();

        Trade newTrade = tradeRepo.save(trade);
        Wallet creditUserWallet = new Wallet();
        creditUserWallet.setCredit(trade.getPrice());
        creditUserWallet.setBalance(doCredit);

        Double doDebit = getDebitUser.getBalance() - trade.getPrice();
        Wallet debitWallet = new Wallet();

        debitWallet.setBalance(doDebit);
        debitWallet.setDebit(trade.getPrice());

        walletRepository.save(debitWallet);
        walletRepository.save(creditUserWallet);

        return appUtils.returnSuccessResponse(newTrade, "Your buying of stock was Succesfull");
    }

    @GetMapping(value = "/allCurrentStockPurchase", produces = "Application/json", consumes = "Application/json")
     @ApiOperation(value="All current purchase stock ", 
		notes="list all the current stocks that the user has purchase.", 
		response = Response.class)
    public ResponseEntity<?> allPurchase(@RequestParam("start") int start, @RequestParam("threshold") int threshold) throws JsonProcessingException {

        List<Trade> allPurchaseTrade = tradeService.getAllTransactionPurchase(start, threshold);
        int size = allPurchaseTrade.size();

        Integer currentPage = 0;
        Integer per_page = threshold;
        Integer total = allPurchaseTrade.size();
        Integer total_pages = total / per_page;

        return appUtils.returnSuccessResponse(allPurchaseTrade, currentPage, per_page, total, total_pages);

    }
     @GetMapping(value = "/allUserTransactions", produces = "Application/json", consumes = "Application/json")
     @ApiOperation(value=" Summarise Users Transactions ", 
		notes="Summarise all of a user transactions over or over a time period using start Date and endDate .", 
		response = Response.class)
    public ResponseEntity<?> findAll(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws JsonProcessingException {

        List<Wallet> allTransaction = walletRepository.transactionQueryAll(startDate, endDate);
        

        return appUtils.returnSuccessResponse(allTransaction,"Summary of transaction based on StartDate and EndDate");

    }
    
    

}
