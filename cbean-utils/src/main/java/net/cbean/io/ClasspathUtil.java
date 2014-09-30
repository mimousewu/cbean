package net.cbean.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classpath Utilities, static methods
 * 
 * @author wutao [e-mail: twu@hp.com]
 * 
 */
public class ClasspathUtil {
	private static final Log log = LogFactory.getLog(ClasspathUtil.class);

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @param annotated
	 *            class have annotation or not
	 * @return The classes
	 * @throws IOException
	 */
	public static List<InputStream> findPackageFiles(String packageName,
			String fileName) throws IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		if (path == null || "".equals(path)) {
			path = ".";
		}
		ArrayList<InputStream> inputStreams = new ArrayList<InputStream>();

		Enumeration<URL> resources = classLoader.getResources(path);
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			inputStreams.addAll(findFiles(resource, packageName, fileName));
			log.info("Load file from resource: " + resource);
		}

		if (inputStreams.size() == 0) {
			InputStream in = ClassLoader.getSystemResourceAsStream(fileName);
			if (in.available() > 0) {
				inputStreams.add(in);
			}
			log.info("Load file from resource: " + fileName);
		}
		return inputStreams;
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @param annotated
	 * @return The classes
	 * @throws IOException
	 */
	private static List<InputStream> findFiles(URL resource,
			String packageName, String fileName) throws IOException {
		List<InputStream> classes = new ArrayList<InputStream>();
		File directory = new File(resource.getFile());
		if (directory.exists()) {
			findClassInDir(directory, packageName, fileName, classes);
		} else {
			JarURLConnection conn = (JarURLConnection) resource
					.openConnection();
			JarFile jarFile = conn.getJarFile();
			Enumeration<JarEntry> e = jarFile.entries();
			String prefix = packageName.replace('.', '/');
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				String entryname = entry.getName();
				if (entryname.startsWith(prefix) && !entry.isDirectory()
						&& entryname.endsWith(fileName)) {
					classes.add(jarFile.getInputStream(entry));
				}
			}
		}
		return classes;
	}

	private static void findClassInDir(File directory, String packageName,
			String fileName, List<InputStream> classes)
			throws FileNotFoundException {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				findClassInDir(file, packageName + "." + file.getName(),
						fileName, classes);
			} else if (file.getName().endsWith(fileName)) {
				classes.add(new FileInputStream(file));
			}
		}
	}
}
