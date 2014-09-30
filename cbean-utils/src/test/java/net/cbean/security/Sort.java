/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 16:51:08
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  Sort.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: Sort.java,v $  $Revision: 1.0 $  $Date: 16:51:08 $
 * $Log: Sort.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.security;

/**
 * @author Simon.Wu <br>
 * <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a><p>
 * 
 */
public class Sort<K extends Model,T> {

	@SuppressWarnings("unchecked")
	public void sort(K k,T t){
		T d= (T) k.getValue();
		d.toString();
	}
}
