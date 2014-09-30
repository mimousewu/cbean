package net.cbean.commons.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;

public class FileRegisterSearcher {
	public static Collection<String> getResources(String path, FileFilter filter) {
		Collection<String> res = new ArrayList<String>();
		IResourceAction action = new ResourceGatherAction(res);
		File file = new File(path);
		if (file.isDirectory()) {
			File[] files = file.listFiles(filter);
			for (int i = 0; i < files.length; i++) {
				File one = files[i];
				if (one.isFile()) {
					action.execute(path + "/" + one.getName());
				}
			}
		}
		return res;
	}

	public static Collection<String> getResourcesInWebInf(String path,
			FileFilter filter) {
		if (path.startsWith("/") || path.substring(0, 2).matches("[\\w]?:")) {
			return getResources(path, filter);
		}
		Collection<String> res = null;
		String tmp = ".";
		tmp = FileRegisterSearcher.class.getClassLoader().getResource(tmp)
				.getPath();
		tmp = tmp.substring(0, tmp.length() - 1);
		tmp = tmp.substring(0, tmp.lastIndexOf("/"));
		path = tmp.endsWith("/") ? (tmp + path) : (tmp + "/" + path);
		res = getResources(path, filter);
		return res;
	}

	public static Collection<String> getResourcesInWebInf(final String path,
			final String extendName) {

		FileFilter filter = new FileFilter() {
			public boolean accept(File path) {
				return path.getName().endsWith(extendName);
			}
		};
		if (path.startsWith("/")
				|| path.substring(0, 3).matches("^[\\w]?:\\\\"))
			return getResources(path, filter);
		else
			return getResourcesInWebInf(path, filter);
	}

	public static Collection<String> getResourcesByPrefix(final String path,
			final String prefix) {

		FileFilter filter = new FileFilter() {
			public boolean accept(File path) {
				return path.getName().startsWith(prefix);
			}
		};
		if (path.startsWith("/")
				|| path.substring(0, 3).matches("^[\\w]?:\\\\"))
			return getResources(path, filter);
		else
			return getResourcesInWebInf(path, filter);
	}
}
