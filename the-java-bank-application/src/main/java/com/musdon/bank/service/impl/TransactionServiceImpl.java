package com.musdon.bank.service.impl;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.musdon.bank.dto.TransactionDto;
import com.musdon.bank.entity.Transaction;
import com.musdon.bank.repository.TransactionRepository;

@Component
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	TransactionRepository transactionRepository;
	
	@Override
	public void saveTransaction(TransactionDto transactiondto) {
		
		Transaction transaction=Transaction.builder()
				.transactionType(transactiondto.getTransactionType())
		        .accountNumber(transactiondto.getAccountNumber())
				.amount(transactiondto.getAmount())
				.status("SUCCESS")
				.build();
		
		transactionRepository.save(transaction);
		System.out.println("Transaction saved Successfully");
	}

}
