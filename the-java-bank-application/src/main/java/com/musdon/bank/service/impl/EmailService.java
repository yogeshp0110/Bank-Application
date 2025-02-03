package com.musdon.bank.service.impl;

import com.musdon.bank.dto.EmailDetails;

public interface EmailService {
	
	void sendEmailAlert(EmailDetails emailDetails); 
	void sendEmailWithAttachment(EmailDetails emailDetails);

}
