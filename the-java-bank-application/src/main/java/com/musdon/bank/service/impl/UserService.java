package com.musdon.bank.service.impl;

import com.musdon.bank.dto.BankResponse;
import com.musdon.bank.dto.CreditDebitRequest;
import com.musdon.bank.dto.EnquiryRequest;
import com.musdon.bank.dto.LoginDto;
import com.musdon.bank.dto.TransferRequest;
import com.musdon.bank.dto.UserRequest;

public interface  UserService {

	BankResponse createAccount(UserRequest userRequest);
	BankResponse balanceEnquiry(EnquiryRequest request);
	BankResponse creditAccount(CreditDebitRequest creaditDebitRequest);
	BankResponse debitAccount(CreditDebitRequest creaditDebitRequest);
	BankResponse transfer(TransferRequest transferRequest);
	
	
	String nameEnquiry(EnquiryRequest request);
	BankResponse login(LoginDto loginDto);
	
	
}
