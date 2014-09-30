package net.cbean.mail;

import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Mail")
public class Mail {
	/**
	 * From mail address
	 */
	private String from;
	
	private String fromName;
	private String subject;
	private String text;
	private String to;
	
	private Set<String> attachments;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setFrom(String name,String address) {
		this.fromName = name;
		this.from = address;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public Set<String> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<String> attachments) {
		this.attachments = attachments;
	}
	
	public void addAttachment(String fileName){
		if(attachments==null){
			attachments = new HashSet<String>();
		}
		attachments.add(fileName);
	}
}
