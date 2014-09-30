/******************************
 * 版权所有：Cbean.net
 * 创建日期: 2006-3-24 14:47:07
 * 创建作者：吴涛
 * 文件名称：XMLJDKHelper.java
 * 最后修改时间：
 * 修改记录：
 *****************************************/
package net.cbean.xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/*
 * XMLJDKHelper
 */
public class XMLJDKHelper<E> {
	private Log log = LogFactory.getLog(XMLJDKHelper.class);
	public boolean saveToXml(E obj,String fileName){
		XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
            encoder.writeObject(obj);
            return true;
        } catch (FileNotFoundException e) {
            log.error("Can not write the config file" + fileName, e);
            return false;
        } catch (Exception e) {
            log.info("Unknown Exception with read the XML file - " + fileName, e);
            return false;
        } finally {
            if (encoder != null) {
                encoder.close();
            }
        }
	}

    @SuppressWarnings("unchecked")
	public E loadFromXml(InputStream input){
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(input);
            return (E) decoder.readObject();
        } catch (Exception e) {
            log.error("Unknown Exception with XML input stream",e);
        } finally {
            if (decoder != null) {
                decoder.close();
            }
        }
        return null;
    }

    /**
     * @deprecated
     * @param fileName
     * @return
     */
	public E loadFromXml(String fileName){
        try {
            InputStream input = new FileInputStream(fileName);
            return loadFromXml(input);
        } catch (FileNotFoundException e) {
            log.info("XML data file not exist - " + fileName + ", I will make a default one");
            return null;
        }
    }
}
