/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 12:06:36
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  JesSmtpMailSender.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: JesSmtpMailSender.java,v $  $Revision: 1.0 $  $Date: 12:06:36 $
 * $Log: JesSmtpMailSender.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.mail.jes;

import java.util.Set;

import net.cbean.mail.Mail;
import net.cbean.mail.MailSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ericdaugherty.mail.server.configuration.ConfigurationManager;
import com.ericdaugherty.mail.server.errors.InvalidAddressException;
import com.ericdaugherty.mail.server.errors.NotFoundException;
import com.ericdaugherty.mail.server.info.EmailAddress;
import com.ericdaugherty.mail.server.services.smtp.SMTPMessage;
import com.ericdaugherty.mail.server.services.smtp.SMTPRemoteSender;

/**
 * @author Simon.Wu <br>
 * <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a><p>
 * 
 */
public class JesSmtpMailSender implements MailSender {
	private static final Log log = LogFactory.getLog(JesSmtpMailSender.class);
	private SMTPRemoteSender sender;
	
	public JesSmtpMailSender(String dir){
		try{
			ConfigurationManager.initialize(dir);
		}catch(RuntimeException e){
			log.error(e);
		}
		sender = new SMTPRemoteSender();
	}
	/* (non-Javadoc)
	 * @see net.cbean.comb.mail.MailSender#sendMail(net.cbean.comb.mail.Mail, java.util.Set)
	 */
	public boolean sendMail(Mail mail, Set<String> sendTo) {
		SMTPMessage message = new SMTPMessage();
		message.setFromAddress(buildAddress(mail.getFrom()));
		message.addDataLine("From: \"" + mail.getFromName()+"\" <"+mail.getFrom()+">");
		StringBuilder to = new StringBuilder();
		for(Object toStr : sendTo){
			to.append(toStr).append(",");
		}
		message.addDataLine("To: <"+to+">");
		message.addDataLine("MIME-Version: 1.0");
		message.addDataLine("Subject: "+mail.getSubject()+"\n");
		message.addDataLine(mail.getText());
		EmailAddress address = buildAddress(to.toString());
		try {
			sender.sendMessage(address, message);
		} catch (NotFoundException e) {
			log.error(e);
			return false;
		} catch (RuntimeException e) {
			log.error(e);
			return false;
		}
		return true;
	}
	
	private EmailAddress buildAddress(String fullAddress) {
		EmailAddress address = new EmailAddress();
		try {
			address.setAddress(fullAddress);
		} catch (InvalidAddressException e1) {
			throw new IllegalArgumentException("can't set address "+ fullAddress,e1);
		} 
		return address;
	}

	/* (non-Javadoc)
	 * @see net.cbean.comb.mail.MailSender#sendSingleMail(net.cbean.comb.mail.Mail)
	 */
	public boolean sendSingleMail(Mail mail) {
		SMTPMessage message = new SMTPMessage();
		message.setFromAddress(buildAddress(mail.getFrom()));
		message.addDataLine("From: \"" + mail.getFromName()+"\" <"+mail.getFrom()+">");
		message.addDataLine("To: \""+mail.getTo()+"\" <"+mail.getTo()+">");
		message.addDataLine("MIME-Version: 1.0");
		message.addDataLine("Subject: "+mail.getSubject()+"\n");
		message.addDataLine(mail.getText());
		
		EmailAddress address = buildAddress(mail.getTo());
		try {
			sender.sendMessage(address, message);
		} catch (NotFoundException e) {
			log.error(e);
			return false;
		} catch (RuntimeException e) {
			log.error(e);
			return false;
		}
		return true;
	}

}
