package com.musdon.bank.service.impl;



import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.musdon.bank.dto.EmailDetails;
import com.musdon.bank.entity.Transaction;
import com.musdon.bank.entity.User;
import com.musdon.bank.repository.TransactionRepository;
import com.musdon.bank.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

	
	private TransactionRepository transactionRepository;
	
	private UserRepository userRepository;
	
	private EmailService emailService;
	
	private static final String FILE ="C:\\Users\\yogeshp\\Documents\\MyStatement.pdf";
	
	/**
     * retrive list transactions within a date range given account number
     * generate a pdf file of transaction
     * send the file via email
	 * @throws FileNotFoundException 
	 * @throws DocumentException 
     */
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException
	{
	LocalDate start=LocalDate.parse(startDate,DateTimeFormatter.ISO_DATE);
	LocalDate end=LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
		List<Transaction> transactionList=transactionRepository.findAll().stream().filter(transaction->transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().isEqual(start))
				.filter(transaction->transaction.getCreatedAt().isEqual(end)).toList();
		
		User user=userRepository.findByAccountNumber(accountNumber);
		String customerName= user.getFirstName()+" "+user.getLastName()+" "+user.getOtherName();
		
		Rectangle statementSize=new Rectangle(PageSize.A4);
		Document document=new Document(statementSize);
		log.info("setting size of component");
		OutputStream outputStream=new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		
		PdfPTable bankInfoTable= new PdfPTable(1);
		PdfPCell bankName=new PdfPCell(new Phrase("The Java Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);
		
		
		PdfPCell bankAdress=new PdfPCell(new Phrase("72 Kharadi, Pune Maharashtra"));
		bankAdress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAdress); 
		
				
		PdfPTable statementInfo=new PdfPTable(2);
		PdfPCell customerInfo=new PdfPCell(new Phrase("Start Date:"+startDate));
		customerInfo.setBorder(0);
		PdfPCell statement=new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		PdfPCell stopDate=new PdfPCell(new Phrase("End Date:"+endDate));
		stopDate.setBorder(0);
		PdfPCell name=new PdfPCell(new Phrase("Customer Name:"+customerName));
		name.setBorder(0);
		
		PdfPCell space=new PdfPCell();
		space.setBorder(0);
		
		PdfPCell address=new PdfPCell(new Phrase("Customer Address:"+user.getAddress()));
		address.setBorder(0);
		
		PdfPTable transactionTable=new PdfPTable(4);
		PdfPCell date=new PdfPCell(new Phrase("Date"));
		date.setBorder(0);
		date.setBackgroundColor(BaseColor.BLUE);		
		PdfPCell transactionType=new PdfPCell(new Phrase("transactionType"));
		transactionType.setBorder(0);
		transactionType.setBackgroundColor(BaseColor.BLUE);		
		PdfPCell transactionAmount=new PdfPCell(new Phrase("transactionAmount"));
		transactionAmount.setBorder(0);
		transactionAmount.setBackgroundColor(BaseColor.BLUE);		
		PdfPCell status=new PdfPCell(new Phrase("status"));
		status.setBorder(0);
		status.setBackgroundColor(BaseColor.BLUE);
		
		transactionTable.addCell(date);
		transactionTable.addCell(transactionType);
		transactionTable.addCell(transactionAmount);
		transactionTable.addCell(status);
		
		
		
		
		transactionList.forEach(transaction->{
			transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionTable.addCell(new Phrase(transaction.getStatus()));
			
		});
		
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(stopDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(address);
		
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionTable);
		
		document.close();
/*		
		EmailDetails emailDetails=EmailDetails.builder()
				.recipient(user.getEmail())				
				.subject("STATEMENT OF ACCOUNT")			
				.messageBody("Kindly, find your requested account statement attached!")
				.attachment(FILE)
				.build();
		
		emailService.sendEmailWithAttachment(emailDetails);
		
*/		
		return transactionList;
	}
	
	
	
	
}
