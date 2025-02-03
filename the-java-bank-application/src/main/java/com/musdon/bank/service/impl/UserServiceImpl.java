package com.musdon.bank.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.musdon.bank.config.JwtTokenProvider;
import com.musdon.bank.dto.AccountInfo;
import com.musdon.bank.dto.BankResponse;
import com.musdon.bank.dto.CreditDebitRequest;
import com.musdon.bank.dto.EmailDetails;
import com.musdon.bank.dto.EnquiryRequest;
import com.musdon.bank.dto.LoginDto;
import com.musdon.bank.dto.TransactionDto;
import com.musdon.bank.dto.TransferRequest;
import com.musdon.bank.dto.UserRequest;
import com.musdon.bank.entity.Role;
import com.musdon.bank.entity.User;
import com.musdon.bank.repository.TransactionRepository;
import com.musdon.bank.repository.UserRepository;
import com.musdon.bank.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired UserRepository userRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/**
		 * creating new account - saving a new user info the db
		 * check If user is already has a account
		 */
		
		
		if(userRepository.existsByEmail(userRequest.getEmail()))
		{
		    return BankResponse.builder()
		    		.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
		            .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
		            .accountInfo(null)
		    		.build();
		   
		}
		
		User newUser=User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO)
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.email(userRequest.getEmail())
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("ACTIVE")
				.role(Role.valueOf("ROLE_ADMIN"))
				.build();
		
		User savedUser =userRepository.save(newUser);
		
//		//Send email Alert
//		EmailDetails emailDetails=EmailDetails.builder()
//				.recipient(savedUser.getEmail())
//				.subject("ACCOUNT CREATION")
//				.messageBody("Congratualations! Your Account Has been Successfully Created. \nYou Account Details: \n "
//						+ "Account Name:"+savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName()
//						+"\n Account Number: "+savedUser.getAccountNumber())
//				
//				.build();
//		emailService.sendEmailAlert(emailDetails);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName()+ "" + savedUser.getLastName() +""+ savedUser.getOtherName())
						.build())
				.build();
		
	
	}
	
	
	public BankResponse login(LoginDto loginDto)
	{
		
		Authentication authentication=null;
		authentication=authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
		
//		EmailDetails loginAlert=EmailDetails.builder()
//		  .subject("You'r Logged in !")
//		  .recipient(loginDto.getEmail())
//		  .messageBody("You are logged into your Account. If you did not initaiate this request, please contact your bank")
//		.build();
//		
//		emailService.sendEmailAlert(loginAlert);
		return BankResponse.builder()
				.responseCode("Login Success")
				.responseMessage(jwtTokenProvider.generateToken(authentication))	
				.build();
		
	}
	

	//balance Enquiry, name Enquiry, creadit, debit, transfer 
	
	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		/**
		 * check if the provided account number is exists in the db
		 */
		
		boolean isAccountExists=userRepository.existsByAccountNumber(request.getAccountNumber());
		if(!isAccountExists)
		{
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					.accountInfo(null)
					
					.build();
		}
		
		User foundUser=userRepository.findByAccountNumber(request.getAccountNumber());
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_SUCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(foundUser.getAccountBalance())
						.accountNumber(request.getAccountNumber())
						.accountName(foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName()) 
						
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		boolean isAccountExists=userRepository.existsByAccountNumber(request.getAccountNumber());
		if(!isAccountExists)
		{
			return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;		
		}
		
		User foundUser=userRepository.findByAccountNumber(request.getAccountNumber());
		return foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
		//Checking Account is exists
		boolean isAccountExists=userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		 if(!isAccountExists)
		 {
			 return BankResponse.builder()
					 .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					 .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					 .accountInfo(null)
					 .build();
		 }
		 
		 User userToCredit=userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
		 userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
		 userRepository.save(userToCredit);
		 
		 //save Tranction
		 TransactionDto transactionDto=TransactionDto.builder()
				 .accountNumber(userToCredit.getAccountNumber())
				 .amount(creditDebitRequest.getAmount())
                 .transactionType("CREDIT")				 
				 .build(); 
		 		  transactionService.saveTransaction(transactionDto);
		 
		 
		 return BankResponse.builder()
				 .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
				 .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
				 .accountInfo(AccountInfo.builder()
						 .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
						 .accountBalance(userToCredit.getAccountBalance())
						 .accountNumber(userToCredit.getAccountNumber())			 
						 .build())
				 .build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
		//check if the account exists
		//check if the amount you intend to withdrow is not more than the current account balance
		boolean isAccountExists=userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		 if(!isAccountExists)
		 {
			 return BankResponse.builder()
					 .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					 .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					 .accountInfo(null)
					 .build();
		 }
		 
		 User userToDebit=userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
	     BigInteger availableBalance=(userToDebit.getAccountBalance().toBigInteger());
	     BigInteger debitAmount=(creditDebitRequest.getAmount().toBigInteger());
	     
	     if(availableBalance.intValue() < debitAmount.intValue())
	     {
	    	 return BankResponse.builder()
	    			 .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					 .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					 .accountInfo(null)
	    			 .build();
	     }else
	     {
	    	 userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
	    	 userRepository.save(userToDebit);
	    	 
	    	 //save Tranction
			 TransactionDto transactionDto=TransactionDto.builder()
					 .accountNumber(userToDebit.getAccountNumber())
					 .amount(creditDebitRequest.getAmount())
	                 .transactionType("DEBIT")				 
					 .build(); 
			 		  transactionService.saveTransaction(transactionDto);
			 		  
	    	 return BankResponse.builder()
	    			 .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
	    			 .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
	    			 .accountInfo(AccountInfo.builder()
	    					 .accountNumber(creditDebitRequest.getAccountNumber())
	    					 .accountBalance(userToDebit.getAccountBalance())
	    					 .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())					
	    					 .build())	    			 
	    			 .build();
	     }
	}

	@Override
	public BankResponse transfer(TransferRequest transferRequest) {
		//get the account to debit(Check If it exists)
		//check if the amount I'm debiting is not more then current balance
		//debit the account
		//get the account to credit
		//credit the account
		
		boolean isDestinationAccountExist=userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
		
		if(!isDestinationAccountExist)
		{
			return BankResponse.builder()
					 .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					 .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
					 .accountInfo(null)
					 .build();
		
		}
		User sourceAccountUser=userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
		if(transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0)
		{
			 return BankResponse.builder()
	    			 .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					 .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					 .accountInfo(null)
	    			 .build();
		}
		
		
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
	    String sourceUsername=sourceAccountUser.getFirstName()+" "+sourceAccountUser.getLastName()+" "+sourceAccountUser.getOtherName();
		userRepository.save(sourceAccountUser);
		/*		
		EmailDetails debitAlert=EmailDetails.builder()
				.subject("DEBIT ALERT")
				.recipient(sourceAccountUser.getEmail())
				.messageBody("The Sum Of: "+ transferRequest.getAmount()+" has been deducted from you account! Your Cuurent Balance is :"+sourceAccountUser.getAccountBalance())
				.build();
		emailService.sendEmailAlert(debitAlert);
		*/		
		User destinationAccountUser=userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
	    userRepository.save(destinationAccountUser);
	    String recipientUsername=destinationAccountUser.getFirstName()+" "+destinationAccountUser.getLastName()+" "+destinationAccountUser.getOtherName();
   
/*
		EmailDetails creditAlert=EmailDetails.builder()
				.subject("Credit ALERT")
				.recipient(sourceAccountUser.getEmail())
				.messageBody("The Sum Of: "+ transferRequest.getAmount()+" has been send to your account from "+sourceUsername+" ! Your Cuurent Balance is :"+sourceAccountUser.getAccountBalance())
				.build();
		emailService.sendEmailAlert(creditAlert);
*/	    
	    //save Tranction
		 TransactionDto transactionDto=TransactionDto.builder()
				 .accountNumber(destinationAccountUser.getAccountNumber())
				 .amount(transferRequest.getAmount())
                .transactionType("CREDIT")				 
				 .build(); 
		 		  transactionService.saveTransaction(transactionDto);
	    
		
		return BankResponse.builder()
				.responseCode(AccountUtils.TRANSFER_AMOUNT_SUCCESS_CODE)
				.responseMessage(AccountUtils.TRANSFER_AMOUNT_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(destinationAccountUser.getAccountBalance())
				        .accountName(recipientUsername)
						.accountNumber(destinationAccountUser.getAccountNumber())
						.build())
				.build();
	
	}
	
	
	
	
	
	
}
