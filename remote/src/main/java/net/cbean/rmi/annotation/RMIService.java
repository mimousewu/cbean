package net.cbean.rmi.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 *          This annotation should be use in interfaces, the name of value is
 *          the service name of RMI URL
 * 
 */
@Retention(RUNTIME)
@Target({ TYPE })
public @interface RMIService {

	/**
	 * local name of the RMI Service.
	 * <p>
	 * If the value is "##default", then the name is derived from the class
	 * name.
	 * 
	 */
	String value() default "##default";
}
