/******************************
 * 版权所有：Cbean.net
 * 创建日期: 2006-3-14 13:23:57
 * 创建作者：吴涛
 * 文件名称：CastorXmlHelper.java
 * 最后修改时间：
 * 修改记录：
 *****************************************/
package net.cbean.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

/*
 * CastorXmlHelper use to help marshal xml to object or unmarshal xml to object
 * must set mapping file when create instance of this class
 */
public class CastorXmlHelper<E> implements XmlHelper<E> {
	private String encoding = "GBK";
	private Log log = LogFactory.getLog(CastorXmlHelper.class);
	private Mapping map = new Mapping();
	
	public CastorXmlHelper(String mappingFile){
		try {
			InputStream mappingInputStream = new FileInputStream(new File(mappingFile));
			loadMapping(mappingInputStream);
		} catch (FileNotFoundException e) {
			log.error("Can't load mapping file "+ mappingFile, e);
		}
	}
	
	public CastorXmlHelper(InputStream mappingInput, String encoding){
		loadMapping(mappingInput);
		setEncoding(encoding);
	}
	
	public CastorXmlHelper(InputStream mappingInput){
		loadMapping(mappingInput);
	}

	private void loadMapping(InputStream mappingInput) {
		map.loadMapping(new InputSource(mappingInput));
	}
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#saveToXml(E, java.lang.String)
	 */
	public void saveToXml(E confObj,String saveingFile){
		File xmlFile = new File(saveingFile);
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			XMLWriter writer = new XMLWriter(new FileWriter(xmlFile),format);
			Marshaller marshaller = new Marshaller(writer);
			marshaller.setMapping(map);
			marshaller.marshal(confObj);
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException("Can't load file "+saveingFile,e);
		} catch (MappingException e) {
			throw new IllegalStateException("mapping setting error.",e);
		} catch (MarshalException e) {
			throw new IllegalStateException("Can't marshal input Object!",e);
		} catch (ValidationException e) {
			throw new IllegalStateException("marshal object validation faild!",e);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#saveToXmlWithoutMappingFile(E, java.lang.String)
	 */
	public void saveToXmlWithoutMappingFile(E confObj,String saveingFile){
		File xmlFile = new File(saveingFile);
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			XMLWriter writer = new XMLWriter(new FileWriter(xmlFile),format);
			Marshaller marshaller = new Marshaller(writer);
			marshaller.marshal(confObj);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Can't load file "+saveingFile,e);
		} catch (MarshalException e) {
			throw new RuntimeException("Can't marshal input Object!",e);
		} catch (ValidationException e) {
			throw new RuntimeException("marshal object validation faild!",e);
		}
		
	}

    /* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#loadFromXml(java.io.InputStream)
	 */
    @SuppressWarnings("unchecked")
	public E loadFromXml(InputStream configInput){
        try {
            Unmarshaller unmarshaller = new Unmarshaller(map);
            InputSource is = new InputSource(configInput);
            is.setEncoding(encoding);
            E unmarshal = (E) unmarshaller.unmarshal(is);
            return unmarshal;

        } catch (MarshalException e) {
            throw new RuntimeException("Can't unmarshal input Object!",e);
        } catch (MappingException e) {
            throw new RuntimeException("The mapping is not conrrect!",e);
        } catch (ValidationException e) {
            throw new RuntimeException("marshal object validation faild!",e);
        }
    }

    /* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#loadFromXml(java.lang.String)
	 */
	public E loadFromXml(String xmlFileName){
        InputStream configInput = null;
		try {
			configInput = new FileInputStream(new File(xmlFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
        return loadFromXml(configInput);
	}
	
	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#loadFromXmlWithoutMappingFile(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public E loadFromXmlWithoutMappingFile(String xmlFileName){
		try {
			InputStream configInput = this.getClass().getClassLoader().getResourceAsStream(xmlFileName);
			Unmarshaller unmarshaller = new Unmarshaller();
			InputSource is = new InputSource(configInput);
			is.setEncoding(encoding);
			E unmarshal = (E) unmarshaller.unmarshal(is);
			return unmarshal;
			
		} catch (MarshalException e) {
			throw new RuntimeException("Can't unmarshal input Object!",e);
		} catch (ValidationException e) {
			throw new RuntimeException("marshal object validation faild!",e);
		}
	}
}

