package net.cbean.mail.javamail;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.cbean.mail.Mail;
import net.cbean.mail.MailSender;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class JavaMailSenderImpl implements MailSender {
	private static final Logger log = Logger
			.getLogger(JavaMailSenderImpl.class);

	private static final String ENCODING = "UTF-8";

	JavaMailSender mailSender;

	public boolean sendMail(final Mail mail, Set<String> sendTo) {
		if (!sendTo.contains(mail.getTo())) {
			sendTo.add(mail.getTo());
		}

		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(msg, true, ENCODING);
			helper.setFrom(new InternetAddress(mail.getFrom()));
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getText(), true);

			addAttachments(mail.getAttachments(), helper);

			List<InternetAddress> ads = new ArrayList<InternetAddress>();
			for (String to : sendTo) {
				String toMail = to.trim();
				if (toMail != null && toMail.matches("[^@,]+@[^,@]+")) {
					ads.add(new InternetAddress(toMail));
				} else {
					log.error("Invalid Email Address: [" + toMail + "]");
				}
			}
			helper.setTo(ads.toArray(new InternetAddress[]{}));
			mailSender.send(msg);
		} catch (MessagingException e) {
			log.error("message error.", e);
			return false;
		}
		return true;
	}

	public boolean sendSingleMail(final Mail mail) {
		MimeMessagePreparator mmp = new MimeMessagePreparator() {
			public void prepare(MimeMessage msg) throws Exception {
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
						mail.getTo()));
				msg.setFrom(new InternetAddress(mail.getFrom()));
				MimeMessageHelper helper = new MimeMessageHelper(msg, true,
						ENCODING);
				helper.setSubject(mail.getSubject());
				helper.setText(mail.getText(), true);

				addAttachments(mail.getAttachments(), helper);
			}

		};

		try {
			mailSender.send(mmp);
		} catch (MailException e) {
			log.error("message error.", e);
			return false;
		}
		return true;
	}

	private void addAttachments(final Set<String> attachments,
			MimeMessageHelper helper) throws MessagingException {
		if (attachments == null)
			return;

		for (Iterator<String> iter = attachments.iterator(); iter.hasNext();) {
			String fileName = (String) iter.next();
			File file = new File(fileName);
			FileSystemResource fileResource = new FileSystemResource(file);
			helper.addAttachment(file.getName(), fileResource);
		}
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
