package com.musdon.bank.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musdon.bank.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
    
	boolean existsByAccountNumber(String accountNumber);
	
	User findByAccountNumber(String accountNumber);
}
