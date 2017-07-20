package com.conning.compents.servlet;


import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.conning.compents.annotation.Action;
import com.conning.compents.annotation.RequestURL;

public class PageServletScanner {
	private static Logger logger = Logger.getLogger(PageServletScanner.class);
	private static String actionPath = "com/conning/compents/servlet/page/";

	private static Hashtable<String, ExcuteBean> table = new Hashtable();

	public static Object handlerRequest(String contentPath, Object[] params) throws Exception {
		ExcuteBean eb = (ExcuteBean) table.get(contentPath);
		if (eb == null) {
			if (contentPath.endsWith("/"))
				eb = (ExcuteBean) table.get(contentPath.substring(0, contentPath.length() - 1));
			else {
				eb = (ExcuteBean) table.get(new StringBuilder().append(contentPath).append("/").toString());
			}
			if (eb == null) {
				logger.info(
						new StringBuilder().append("Spring dyn/admin don't have a compent to proess the contentPath[")
								.append(contentPath).append("]").toString());
				return "";
			}
		}
		return eb.excute(params);
	}

	public static void scanner(String contentPath) throws Exception {
		if (StringUtils.isEmpty(contentPath)) {
			contentPath = actionPath;
		}
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String fileName = classLoader.getResource(contentPath).getFile();
		File file = new File(fileName);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null)
				for (File f : files) {
					Class clazz = classLoader.loadClass(new StringBuilder().append(contentPath)
							.append(f.getName().substring(0, f.getName().length() - 6)).toString()
							.replaceAll("/", "\\."));
					analyseClass(clazz);
				}
			else
				logger.debug("load jar file fail");
		} else {
			String[] jarPath = fileName.split("\\!");
			String queryPackageName = jarPath[1].substring(1);
			FileInputStream fis = null;
			String jarFilePath = jarPath[0].substring(6);
			try {
				fis = new FileInputStream(jarPath[0]);
			} catch (Exception e1) {
				try {
					fis = new FileInputStream(jarPath[0].substring(6));
				} catch (Exception e) {
					fis = new FileInputStream(new StringBuilder().append(System.getProperty("file.separator"))
							.append(jarPath[0].substring(6)).toString());
				}
			}
			JarInputStream jis = new JarInputStream(fis, false);
			JarEntry e = null;
			while ((e = jis.getNextJarEntry()) != null) {
				String eName = e.getName();
				if ((eName.startsWith(queryPackageName)) && (eName.endsWith("class"))) {
					String className = eName.replaceAll("/", ".").substring(0, eName.length() - 6);
					Class clazz = Class.forName(className);
					analyseClass(clazz);
				}
				jis.closeEntry();
			}
			jis.close();
		}
	}

	private static void analyseClass(Class clazz) {
		Action action = (Action) clazz.getAnnotation(Action.class);
		RequestURL requestUrl = (RequestURL) clazz.getAnnotation(RequestURL.class);
		if (action != null) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				RequestURL murl = (RequestURL) method.getAnnotation(RequestURL.class);
				if (murl != null) {
					ExcuteBean eb = new ExcuteBean();
					eb.setBeanName(clazz);
					eb.setMethodName(method);
					String mappingurl = new StringBuilder().append(requestUrl == null ? "" : requestUrl.value())
							.append(murl.value()).toString();
					table.put(mappingurl, eb);
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("file.separator"));
	}

}