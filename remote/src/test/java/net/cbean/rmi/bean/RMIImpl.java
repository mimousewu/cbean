package net.cbean.rmi.bean;

import org.springframework.stereotype.Service;

@Service
public class RMIImpl implements RMIInterface {

	public String name() {
		return "Wu Tao";
	}

}
