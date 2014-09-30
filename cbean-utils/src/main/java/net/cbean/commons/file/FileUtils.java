package net.cbean.commons.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileUtils {
	public static boolean moveTo(File file, String distDir){
		String path = parsePath(distDir);
		checkDir(path);
		String filePath = (path.endsWith(File.separator))?path+file.getName() : path+File.separator+file.getName();
		return file.renameTo(new File(filePath));
	}

	private static void checkDir(String path) {
		File dist = new File(path);
		if(dist.exists()){
			if(!dist.isDirectory()){
				dist.renameTo(new File(dist.getPath()+".swp."+System.currentTimeMillis()));
				dist.mkdir();
			}
		}else{
			dist.mkdir();
		}
	}

	/**
	 * 可以解析类似 %d{yyyy-MM-dd hh:mm:ss}这样的日期格式
	 */
	private static String parsePath(String distDir) {
		Pattern parseReg = Pattern.compile("%d\\{(.*?)\\}");
		Matcher m = null;
		CharSequence inputStr = distDir.subSequence(0,distDir.length());
		m = parseReg.matcher(inputStr);

		StringBuffer sb = new StringBuffer();
		boolean found = m.find();
		while(found){
			String timeStr = new SimpleDateFormat(m.group(1)).format(new Date());
			m.appendReplacement(sb,timeStr);
			found = m.find();
		}
		return m.appendTail(sb).toString();
	}
	
	public static void main(String[] args){
		checkDir("t:\\viewed\\%d{yyyy-MM-dd}");
		System.out.println("ok");
	}
}
