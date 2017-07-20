package com.conning.compents.servlet;


import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.conning.compents.servlet.servletHandler.ServletHandler;
import com.conning.compents.servlet.servletHandler.ServletHandlerFactory;

public class DynServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8180278446600701204L;
	
	private static ContextLoader contextLoader;
	private static ServletContext servletContext;

	public void setContextLoader(ContextLoader contextLoader) {
		contextLoader = contextLoader;
	}

	public void init(ServletConfig config) throws ServletException {
		String actionPath = config.getInitParameter("actionPath");
		try {
			PageServletScanner.scanner(actionPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletContext = config.getServletContext();
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		if (context == null) {
			contextLoader = new ContextLoader();
			contextLoader.initWebApplicationContext(servletContext);
			context = ContextLoader.getCurrentWebApplicationContext();
		}
		String[] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			String[] beanNode = beanName.split("\\.");
			ComponentDirectory parent = null;
			for (int i = 0; i < beanNode.length; i++) {
				ComponentDirectory cd = new ComponentDirectory();
				cd.setLevel(i + 1);
				cd.setPackageName(beanNode[i]);
				cd.setLeaf(false);
				cd.setParent(parent);
				if (i + 1 == beanNode.length) {
					cd.setLeaf(true);
				}
				cd = ComponentMgr.addComponentDirectory(cd, parent);
				parent = cd;
			}
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reqUrl = req.getRequestURL().toString();
		ServletHandler handler = ServletHandlerFactory.buildHandler(reqUrl);
		handler.handle(req, resp);
	}

	public void destroy() {
		if (contextLoader != null) {
			contextLoader.closeWebApplicationContext(servletContext);
		}
		super.destroy();
	}
}