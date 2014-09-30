/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: ÏÂÎç10:14:26
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  IOUtils.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: IOUtils.java,v $  $Revision: 1.0 $  $Date: ÏÂÎç10:14:26 $
 * $Log: IOUtils.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 * 
 */
public class IOUtils {

	/**
	 * get file inputstream, if start with classpath:, then get from classpath.
	 * if not, get from main directory.
	 * 
	 * @param fileString
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(String fileString)
			throws FileNotFoundException {
		InputStream in;
		if (fileString.startsWith("classpath:")) {
			in = IOUtils.class.getClassLoader().getResourceAsStream(
					fileString.substring(10));
		} else {
			in = new FileInputStream(new File(fileString));
		}

		return in;
	}
}
