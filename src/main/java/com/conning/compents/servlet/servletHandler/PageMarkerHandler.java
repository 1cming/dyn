package com.conning.compents.servlet.servletHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.conning.compents.servlet.PageServletScanner;
import com.conning.compents.servlet.page.DynUserBean;
import com.conning.compents.util.TemplateParser;

public class PageMarkerHandler implements ServletHandler {
	Logger logger = Logger.getLogger(PageMarkerHandler.class);

	public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getContextPath();
		String basePath = "";
		if (req.getServerPort() == 80) {
			basePath = req.getScheme() + "://" + req.getServerName() + path;
		} else {
			basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path;
		}

		String requrl = req.getRequestURL().toString();
		String ftl = "";
		String mappingUrl = requrl.substring(requrl.indexOf("dyn/admin") + 9);

		if ((!mappingUrl.contains("/login/validate")) && (!mappingUrl.contains("/login/login"))) {
			DynUserBean dynUserBean = (DynUserBean) req.getSession().getAttribute("dyn_user");
			String params = "?";
			if (dynUserBean == null) {
				Map map = req.getParameterMap();
				if (map != null) {
					Iterator iterator = map.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						params = params + key + "=" + ((String[]) (String[]) map.get(key))[0] + "&";
					}
				}
				req.setAttribute("oldrequrl", requrl + params);
				req.getRequestDispatcher("/dyn/admin/login/login").forward(req, resp);
				return;
			}
		}
		try {
			req.setCharacterEncoding("UTF-8");
			ftl = (String) PageServletScanner.handlerRequest(mappingUrl, new Object[] { req, resp });
			if (ftl == null)
				return;
		} catch (Exception e) {
			this.logger.error("process request fail", e);
		}
		String htmlstr = "";
		if (!StringUtils.isEmpty(ftl)) {
			Map map = (Map) req.getAttribute("data");
			if (map == null) {
				map = new HashMap();
			}
			map.put("project", basePath);
			htmlstr = TemplateParser.getInstance().getStrByTemplate(map, ftl + ".ftl");
			resp.setContentType("text/html");
			resp.setCharacterEncoding("UTF-8");
		} else {
			htmlstr = "<p style='font-size:40px;text-align:center;margin-top:100px'>request resource is not available!</p>";
			resp.setStatus(404);
		}
		PrintWriter pw = resp.getWriter();
		pw.write(htmlstr);
		pw.flush();
		pw.close();
	}
}