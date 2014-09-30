package net.cbean.mail;

import java.util.Set;

public interface MailSender {
	public boolean sendMail(Mail mail,Set<String> sendTo);
	public boolean sendSingleMail(Mail mail);
}
