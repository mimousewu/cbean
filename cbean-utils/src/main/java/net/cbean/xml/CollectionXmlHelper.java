package net.cbean.xml;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

public class CollectionXmlHelper<E> implements XmlHelper<E> {
	private CastorXmlHelper<XMLModel> castorHelper ;
	
	public CollectionXmlHelper(String mappingFile){
		castorHelper = new CastorXmlHelper<XMLModel>(mappingFile);
	}
	
	public CollectionXmlHelper(InputStream mappingInput){
		castorHelper = new CastorXmlHelper<XMLModel>(mappingInput);
	}
	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#saveToXml(java.lang.Object, java.lang.String)
	 */
	public void saveToXml(E confObj, String saveingFile) {
		XMLModel model = buildXmlModel(confObj);
		castorHelper.saveToXml(model, saveingFile);
	}

	@SuppressWarnings("unchecked")
	private XMLModel buildXmlModel(E confObj) {
		XMLModel model = new XMLModel();
		if(confObj instanceof Collection){
			model.setCollection((Collection)confObj);
		}else if(confObj instanceof Map){
			model.setMap((Map)confObj);
		}
		return model;
	}

	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#saveToXmlWithoutMappingFile(java.lang.Object, java.lang.String)
	 */
	public void saveToXmlWithoutMappingFile(E confObj, String saveingFile) {
		XMLModel model = buildXmlModel(confObj);
		castorHelper.saveToXmlWithoutMappingFile(model, saveingFile);
	}

	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#loadFromXml(java.io.InputStream)
	 */
	@SuppressWarnings("unchecked")
	public E loadFromXml(InputStream configInput) {
		XMLModel model = castorHelper.loadFromXml(configInput);
		if(model.getCollection()!=null){
			return (E) model.getCollection();
		}else if(model.getMap()!=null){
			return (E) model.getMap();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#loadFromXml(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public E loadFromXml(String xmlFileName) {
		XMLModel model = castorHelper.loadFromXml(xmlFileName);
		if(model.getCollection()!=null){
			return (E) model.getCollection();
		}else if(model.getMap()!=null){
			return (E) model.getMap();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.shjv.tdscdma.omc.simulator.server.util.xml.XmlHelper#loadFromXmlWithoutMappingFile(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public E loadFromXmlWithoutMappingFile(String xmlFileName) {
		XMLModel model = castorHelper.loadFromXmlWithoutMappingFile(xmlFileName);
		if(model.getCollection()!=null){
			return (E) model.getCollection();
		}else if(model.getMap()!=null){
			return (E) model.getMap();
		}
		return null;
	}

}

