package net.cbean.xml;

import java.io.InputStream;

public interface XmlHelper<E> {
	public abstract void saveToXml(E confObj, String saveingFile);

	public abstract void saveToXmlWithoutMappingFile(E confObj,
			String saveingFile);

	@SuppressWarnings("unchecked")
	public abstract E loadFromXml(InputStream configInput);

	/**
	 * @param xmlFileName
	 * @return
	 */
	public abstract E loadFromXml(String xmlFileName);

	@SuppressWarnings("unchecked")
	public abstract E loadFromXmlWithoutMappingFile(String xmlFileName);

}
