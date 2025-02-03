package com.musdon.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musdon.bank.dto.BankResponse;
import com.musdon.bank.dto.CreditDebitRequest;
import com.musdon.bank.dto.EnquiryRequest;
import com.musdon.bank.dto.LoginDto;
import com.musdon.bank.dto.TransferRequest;
import com.musdon.bank.dto.UserRequest;
import com.musdon.bank.service.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name ="User Account Management APIs")
public class UserController {

	@Autowired
	UserService userService;
	
	@Operation(
			summary = "Create New User Account",
			description = "Creating a new user and assigning an account ID"
			)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status 201 Created"
			)
	@PostMapping 
	public BankResponse createAccount(@RequestBody UserRequest userRequest)
	{
	   return userService.createAccount(userRequest);
	}
	
	
	
	
	@Operation(
			summary = "Balance Enquiry",
			description = "Given an account number, check how much the user has"
			)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status 200 SUCCESS"
			)
	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request)
	{
		 return userService.balanceEnquiry(request);
	}
	
	
	@GetMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request)
	{
		 return userService.nameEnquiry(request);
	}
	
	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest)
	{
		return userService.creditAccount(creditDebitRequest);
	}
	
	@PostMapping("/debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest)
	{
		return userService.debitAccount(creditDebitRequest);
	}

	@PostMapping("/transfer")
	public BankResponse transfer(@RequestBody TransferRequest transferRequest)
	{
		return userService.transfer(transferRequest);
	
	}
	
	@PostMapping("/login")
	public BankResponse login(@RequestBody LoginDto loginDto)
	{
		return userService.login(loginDto);
	}
	
}
