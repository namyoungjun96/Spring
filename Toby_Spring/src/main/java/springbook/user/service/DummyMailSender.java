package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;

public class DummyMailSender implements MailSender {

	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		// TODO Auto-generated method stub
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
		// TODO Auto-generated method stub
		
	}
	
}
