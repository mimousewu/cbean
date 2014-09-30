package net.cbean.rmi;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class Utilities, static methods
 * 
 * @author wutao [e-mail: twu@hp.com]
 * 
 */
public class ClassUtil {

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @param annotated
	 *            class have annotation or not
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?>[] findPackageClasses(String packageName,
			boolean annotated) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);

		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			classes.addAll(findClasses(resource, packageName, annotated));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	public static Class<?>[] findPackageClasses(String packageName)
			throws ClassNotFoundException, IOException {
		return findPackageClasses(packageName, false);
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
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static List<Class<?>> findClasses(URL resource, String packageName,
			boolean annotated) throws ClassNotFoundException, IOException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		File directory = new File(resource.getFile());
		if (directory.exists()) {
			findClassInDir(directory, packageName, classes, annotated);
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
						&& entryname.endsWith(".class")) {
					String classname = entryname.substring(0,
							entryname.length() - 6);
					Class<?> clazz = Class.forName(classname.replace('/', '.'));
					if (!annotated || clazz.getAnnotations().length > 0) {
						classes.add(clazz);
					}
				}
			}
		}
		return classes;
	}

	private static void findClassInDir(File directory, String packageName,
			List<Class<?>> classes, boolean annotated)
			throws ClassNotFoundException {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				findClassInDir(file, packageName + "." + file.getName(),
						classes, annotated);
			} else if (file.getName().endsWith(".class")) {
				Class<?> clazz = Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6));
				if (!annotated || clazz.getAnnotations().length > 0) {
					classes.add(clazz);
				}
			}
		}
	}
}
