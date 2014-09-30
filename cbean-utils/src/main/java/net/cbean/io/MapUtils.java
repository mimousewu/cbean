package net.cbean.io;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class MapUtils {

	public static void copyProperties(Map<String, Object> map, Object bean) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
		PropertyDescriptor[] descriptor = beanWrapper.getPropertyDescriptors();
		for (int i = 0; i < descriptor.length; i++) {
			String name = descriptor[i].getName();
			map.put(name, beanWrapper.getPropertyValue(name));
		}
	}

}
