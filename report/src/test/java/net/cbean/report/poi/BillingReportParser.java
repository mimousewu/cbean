package net.cbean.report.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.cbean.report.ReportParser;
import net.cbean.report.model.Report;
import net.cbean.report.model.ReportConfig;
import net.cbean.report.model.TableConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BillingReportParser implements ReportParser<Workbook, Report> {
	private static final Log log = LogFactory.getLog(BillingReportParser.class);

	private String storePath;

	public Workbook parse(Report report, ReportConfig config) {
		String templateFile = config.getTemplate();

		Workbook workbook = null;
		try {
			workbook = WorkbookFactory
					.create(new FileInputStream(templateFile));
		} catch (IOException e) {
			log.error(e);
		} catch (InvalidFormatException e) {
			log.error(e);
		}

		POITableParser poiParser = new POITableParser();
		poiParser.setWorkbook(workbook);

		for (Map.Entry<String, List<?>> table : report.getTables().entrySet()) {
			String tableId = table.getKey();
			String[] variables = tableId.split("_");
			String customerId = variables[0];
			// String tableTemplate = report.getTableTitles().get(tableId);
			String tableTemplate = (variables.length > 1) ? variables[1]
					: variables[0];
			List<?> tableValue = table.getValue();

			TableConfig tableConfig = config.getTableMap().get(tableTemplate);
			// poiParser.clearTempInReport(tableConfig);
			tableConfig.setTitle(customerId);
			Sheet sheet = poiParser.parse(tableValue, tableConfig);
			log.info("Successful parser sheet: " + sheet.getSheetName());
		}

		poiParser.removeTemplateSheet(config.getTables().toArray(
				new POITableConfig[0]));
		return workbook;
	}

	public void storeWorkbook(Workbook workbook, String outputFile)
			throws IOException {
		if (storePath != null) {
			outputFile = storePath + File.separator + outputFile;
		}
		FileOutputStream fOut = new FileOutputStream(outputFile);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
		log.info("Output Excel file generate successful!");
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

}
