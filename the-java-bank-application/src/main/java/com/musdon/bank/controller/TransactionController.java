package com.musdon.bank.controller;

import com.itextpdf.text.DocumentException;
import com.musdon.bank.entity.Transaction;
import com.musdon.bank.service.impl.BankStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class TransactionController {

	@Autowired
    private final BankStatement bankStatementService;

    public TransactionController(BankStatement bankStatementService) {
        this.bankStatementService = bankStatementService;
    }

    @GetMapping("/bankStatement")
    public List<Transaction> generateStatement(
    		   @RequestParam(name = "accountNumber") String accountNumber,
    		   @RequestParam(name = "startDate") String startDate,
    	        @RequestParam(name = "endDate") String endDate) throws FileNotFoundException, DocumentException {
        return bankStatementService.generateStatement(accountNumber, startDate, endDate);
    }
}
