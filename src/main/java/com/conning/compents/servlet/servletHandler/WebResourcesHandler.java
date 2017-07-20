package com.conning.compents.servlet.servletHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class WebResourcesHandler implements ServletHandler {
	Logger logger = Logger.getLogger(WebResourcesHandler.class);
	public static final String webSourcePath = "webresources";

	public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reqUrl = req.getRequestURL().toString();
		createStaticInfo(reqUrl, resp);
	}

	private void createStaticInfo(String reqUrl, HttpServletResponse resp) throws IOException {
		OutputStream ops = resp.getOutputStream();
		String sourceName = reqUrl.substring(reqUrl.lastIndexOf("/"));
		String reqSourcePath = "webresources";

		if (reqUrl.endsWith("css")) {
			resp.setContentType("text/css");
			reqSourcePath = reqSourcePath + "/css" + sourceName;
		} else if (reqUrl.endsWith("js")) {
			resp.setContentType("text/js");
			reqSourcePath = reqSourcePath + "/js" + sourceName;
		} else if (("jpg,png,jpeg,bmp,gif".indexOf(sourceName.substring(sourceName.length() - 3)) > -1)
				|| ("jpg,png,jpeg,bmp,gif".toUpperCase().indexOf(sourceName.substring(sourceName.length() - 3)) > -1)) {
			resp.setContentType("image/gif");
			reqSourcePath = reqSourcePath + "/images" + sourceName;
		}
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(reqSourcePath);
		if (input == null)
			this.logger.info("the path[" + reqUrl + "] is not exist resource");
		else
			try {
				int temp;
				while ((temp = input.read()) != -1) {
					ops.write(temp);
				}
				ops.flush();
				ops.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}