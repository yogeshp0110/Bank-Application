package com.musdon.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musdon.bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

}
