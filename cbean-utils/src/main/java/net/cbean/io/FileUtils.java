package net.cbean.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FileUtils {

	private static final Logger log = LogManager.getLogger(FileUtils.class);

	/**
	 * Copy a file to a target file.
	 * 
	 * @param source need copy file.
	 * @param target copy to file.
	 */
	public static void copy(String source, String target) throws IOException {
		int bufferSize = 1024;
		
		File afile = new File(source);
		File bfile = new File(target);

		InputStream inStream = new FileInputStream(afile);
		OutputStream outStream = new FileOutputStream(bfile);

		byte[] buffer = new byte[bufferSize];

		int length;
		while ((length = inStream.read(buffer)) > 0) {
			outStream.write(buffer, 0, length);
		}

		inStream.close();
		outStream.close();

		if (log.isInfoEnabled())
			log.info("File is copied successful from " + source + " to "
					+ target + "!");
	}
}
