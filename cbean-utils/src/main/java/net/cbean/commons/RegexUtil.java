package net.cbean.commons;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;

public class RegexUtil {
public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Long getPhoneNo(String phonestr) {
		if (phonestr.startsWith("86"))
			phonestr = phonestr.substring(2,phonestr.length());
		if(phonestr.trim().length()==0) return new Long(0);
		return new Long(phonestr);
	}

	public static void print(Matcher m) {
		for(int i=0;i<=m.groupCount();i++){
			String mstr = m.group(i);
			mstr = getPhoneNo(mstr).toString();
			System.out.println(i+": "+mstr);
		}
	}
}
