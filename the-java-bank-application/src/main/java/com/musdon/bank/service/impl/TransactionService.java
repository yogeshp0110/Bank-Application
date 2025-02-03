package com.musdon.bank.service.impl;

import com.musdon.bank.dto.TransactionDto;
import com.musdon.bank.entity.Transaction;

public interface TransactionService {

	void saveTransaction(TransactionDto transaction);
	
}
