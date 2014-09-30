package net.cbean.report.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.cbean.report.dao.ReportDao;
import net.cbean.report.model.ReportConfig;
import net.cbean.report.model.ReportParam;
import net.cbean.report.model.TableColumn;
import net.cbean.report.model.TableConfig;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 */
public class AbstractReportService {
	static final Logger log = LogManager.getLogger(AbstractReportService.class);

	private Map<String, ReportConfig> configs = new HashMap<String, ReportConfig>();
	protected ReportDao dao;
	private String path;

	private Class<?>[] annotations = { ReportConfig.class, TableConfig.class,
			TableColumn.class, ReportParam.class };

	public AbstractReportService() {
		super();
	}

	public ReportConfig getConfig(String reportName) {
		ReportConfig config = configs.get(reportName);
		if (config == null) {
			XStream xstream = getXStream();
			try {
				FileInputStream fis = new FileInputStream(fileName(path,
						reportName));
				config = (ReportConfig) xstream.fromXML(fis);
				fis.close();
			} catch (Exception e) {
				log.error(e);
			}
			
			File tempFile = new File(config.getTemplate());
			if (!tempFile.exists()) {
				config.setTemplate(path + File.separator + config.getTemplate());
			}
		}
		return config;
	}

	public void saveConfig(ReportConfig config) {
		XStream xstream = getXStream();
		try {
			FileOutputStream fs = new FileOutputStream(fileName(path,
					config.getName()));
			xstream.toXML(config, fs);
			fs.close();
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	private String fileName(String dir, String name) {
		return dir + File.separator + name + ".xml";
	}

	private XStream getXStream() {
		XStream xstream = new XStream(new DomDriver("UTF-8"));
		xstream.processAnnotations(annotations);
		return xstream;
	}

	public void setDao(ReportDao dao) {
		this.dao = dao;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void addAnnotationClass(Class<?>... clazz) {
		Class<?>[] tmp = annotations;
		this.annotations = new Class<?>[annotations.length + clazz.length];
		System.arraycopy(tmp, 0, annotations, 0, tmp.length);
		System.arraycopy(clazz, 0, annotations, tmp.length, clazz.length);
	}

}