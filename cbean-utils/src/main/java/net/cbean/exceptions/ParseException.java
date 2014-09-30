/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 7:24:01
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  ParseException.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: ParseException.java,v $  $Revision: 1.0 $  $Date: 7:24:01 $
 * $Log: ParseException.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.exceptions;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 *         <p>
 * 
 */
public class ParseException extends RuntimeException {
	private static final long serialVersionUID = 7798247559248950168L;

	public ParseException() {
		super();
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable course) {
		super(message, course);
	}

	public ParseException(Throwable course) {
		super(course);
	}
}
