package com.musdon.bank.utils;

import java.time.Year;

public class AccountUtils {
	
	
	public static final String ACCOUNT_EXISTS_CODE="001";
	public static final String ACCOUNT_EXISTS_MESSAGE="This user already has an account Created!";
	
	public static final String ACCOUNT_CREATION_SUCCESS_CODE="002";
	public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE="Account has been Successfully Created!";
	
	public static final String ACCOUNT_NOT_EXISTS_CODE="003";
	public static final String ACCOUNT_NOT_EXISTS_MESSAGE="User with the provided Account Number doen not exists";
	
	public static final String ACCOUNT_FOUND_CODE="004";
	public static final String ACCOUNT_FOUND_SUCESS_MESSAGE="User Account Found";
	
	public static final String ACCOUNT_CREDITED_SUCCESS_CODE="005";
	public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE="Account has been Successfully Credited!";
	
	public static final String INSUFFICIENT_BALANCE_CODE="006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE="Insufficient Balance!";
	
	
	public static final String ACCOUNT_DEBITED_SUCCESS_CODE="007";
	public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE="Account has been Successfully Debited!";
	
	public static final String DEBITED_ACCOUNT_NOT_EXIST_CODE="008";
	public static final String DEBITED_ACCOUNT_NOT_EXIST_MESSAGE="Account has been Successfully Debited!";
	
	public static final String TRANSFER_AMOUNT_SUCCESS_CODE="009";
	public static final String TRANSFER_AMOUNT_SUCCESS_MESSAGE="Amount transfer Successfully!";
	
	
	
	public static String generateAccountNumber() {
	
		/**
		 * 2025 + randomSixDigits
		 */
		
		Year currentYear=Year.now();
		int min=100000;
		int max=999999;
		
		//genrate a random number between min and max

		int randNumber=(int) Math.floor(Math.random()*(max-min +1) + min) ;
		
		//convert the current and random Number to String , then concatenate them
		
		String year=String.valueOf(currentYear);
		String randomNumber=String.valueOf(randNumber);
		
		StringBuilder accountNumber=new StringBuilder();
		
		return accountNumber.append(year).append(randomNumber).toString();
	}
	
	
}
