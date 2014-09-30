package net.cbean.mail;

import junit.framework.TestCase;

import com.ericdaugherty.mail.server.configuration.ConfigurationManager;
import com.ericdaugherty.mail.server.errors.InvalidAddressException;
import com.ericdaugherty.mail.server.errors.NotFoundException;
import com.ericdaugherty.mail.server.info.EmailAddress;
import com.ericdaugherty.mail.server.services.smtp.SMTPMessage;
import com.ericdaugherty.mail.server.services.smtp.SMTPRemoteSender;

public class MailSenderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'net.cbean.comb.mail.jes.JesSmtpMailSender.sendMail(Mail, Set)'
	 */
	public void t1estSendMail() {
		ConfigurationManager.initialize("src/test/resources/conf");
		SMTPRemoteSender sender = new SMTPRemoteSender();
		SMTPMessage message = new SMTPMessage();
		message.setFromAddress(buildAddress("mouse.wu@sina.com"));
		message.addDataLine("Hello");
		
		EmailAddress address = buildAddress("mimousewu@sina.com");
		try {
			sender.sendMessage(address, message);
		} catch (NotFoundException e) {
			fail(e.getMessage());
		} catch (RuntimeException e) {
			fail(e.getMessage());
		}
	}

	private EmailAddress buildAddress(String fullAddress) {
		EmailAddress address = new EmailAddress();
		try {
			address.setAddress(fullAddress);
		} catch (InvalidAddressException e1) {
			fail(e1.getMessage());
		}
		return address;
	}
	
	public void test(){
		assert(true);
	}

}
