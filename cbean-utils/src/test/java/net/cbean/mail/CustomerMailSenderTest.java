package net.cbean.mail;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

public class CustomerMailSenderTest extends TestCase {
	private MailSender sender;
	protected ApplicationContext context;
	private GreenMail greenMail;
	
	public CustomerMailSenderTest(){
				context = new GenericXmlApplicationContext(
						"applicationContext-mail.xml");
	}
	protected void setUp() throws Exception {
		super.setUp();
		greenMail =  new GreenMail(new ServerSetup(3025, null, "smtp"));
		greenMail.setUser("twu", "pass");
	    greenMail.start();
		
		sender = (MailSender) context.getBean("mailSender");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		greenMail.stop();
	}

	/*
	 * Test method for 'com.a8.log.mail.AttachmentMailSender.sendMail()'
	 */
	public void testSendMail() throws Exception {
		String subject = "Test by Mail Sender new";
		
		Mail mail = new Mail();
		mail.setFromName("CBEAN Mail Sender");
		mail.setFrom("mimousewu@sina.com");
		mail.setTo("mimousewu@sina.com");
		mail.setSubject(subject);
		mail.setText("can you see? <img src=\"cid:maomao.jpg\"> End.");
//		mail.addAttachment("C:\\Users\\cnlwut\\Pictures\\maomao.jpg");
		sender.sendSingleMail(mail);
		
		MimeMessage mimeMessage = greenMail.getReceivedMessages()[0];
		assertEquals(subject,mimeMessage.getSubject());
		MimeMultipart content = (MimeMultipart) mimeMessage.getContent();
		assertNotNull(content);
	}

}
