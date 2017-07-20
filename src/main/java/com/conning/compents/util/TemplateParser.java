package com.conning.compents.util;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import org.apache.log4j.Logger;

public class TemplateParser {
	private static final Logger logger = Logger.getLogger(TemplateParser.class);
	private Configuration config = null;
	private static final String templatePath = "ftl";
	
	private TemplateParser() {
		this.config = new Configuration();
		this.config.setObjectWrapper(new DefaultObjectWrapper());
		ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "/ftl");
		TemplateLoader tl = this.config.getTemplateLoader();
		TemplateLoader[] loaders = { tl, ctl };
		MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
		this.config.setTemplateLoader(mtl);
	}

	public static TemplateParser getInstance() {
		return SingletonHolder.signletion;
	}

	public String getStrByTemplate(Map<String, Object> root, String templatePath, String templateName) {
		logger.info("模板文件:" + templatePath);
		String result = "";
		try {
			this.config.setDirectoryForTemplateLoading(new File(templatePath));
			Template template = this.config.getTemplate(templateName);
			StringWriter out = new StringWriter();
			template.process(root, out);
			out.flush();
			out.close();
			result = out.toString();
		} catch (Exception e) {
			logger.error("加载" + templatePath + templateName + "文件异常", e);
			e.printStackTrace();
		}
		return result;
	}

	public String getStrByTemplate(Map<String, Object> root, String templateName) {
		logger.info("模板文件:ftl");
		String result = "";
		try {
			Template template = this.config.getTemplate(templateName);
			StringWriter out = new StringWriter();
			template.process(root, out);
			out.flush();
			out.close();
			result = out.toString();
		} catch (Exception e) {
			logger.error("加载ftl" + templateName + "文件异常", e);
		}
		return result;
	}

	private static class SingletonHolder {
		private static TemplateParser signletion = new TemplateParser();
	}
}