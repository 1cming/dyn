package com.conning.compents.servlet.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.conning.compents.annotation.Action;
import com.conning.compents.annotation.RequestURL;
import com.conning.compents.util.MD5Util;

@Action(value = "loginContorl")
public class LoginContorl {
	private static Properties prop = new Properties();
	private static Logger logger = Logger.getLogger(LoginContorl.class);

	@RequestURL("/login/toRePwdPage")
	public String toRePwdPage(HttpServletRequest req, HttpServletResponse resp) {
		DynUserBean dub = (DynUserBean) req.getSession().getAttribute("dyn_user");
		Map map = new HashMap();
		map.put("userName", dub.getUserName());
		req.setAttribute("data", map);
		return "repwd";
	}

	@RequestURL("/login/rePwd")
	public String repwd(HttpServletRequest req, HttpServletResponse resp) {
		Map map = new HashMap();
		String oldpwd = req.getParameter("oldpassword");
		String newpwd = req.getParameter("newpassword");
		DynUserBean dub = (DynUserBean) req.getSession().getAttribute("dyn_user");
		String errorMsg = "modify success";
		if (StringUtils.isEmpty(newpwd)) {
			errorMsg = "the new password is empty";
		} else if (oldpwd.trim().equals(newpwd.trim())) {
			errorMsg = "the new password equals old password!";
		} else if (oldpwd.equals(dub.getPwssword())) {
			dub.setPwssword(newpwd);
			prop.setProperty("password", MD5Util.toMD5(newpwd));
			req.setAttribute("dyn_user", dub);
		} else {
			errorMsg = "the old password is error";
		}
		map.put("errorMsg", errorMsg);
		map.put("userName", dub.getUserName());
		req.setAttribute("data", map);
		return "repwd";
	}

	@RequestURL("/login/validate")
	public String loginValidate(HttpServletRequest req, HttpServletResponse resp) {
		String oldrequrl = req.getParameter("oldrequrl");
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String mpwd = MD5Util.toMD5(password);
		try {
			if ((prop.getProperty("user").equalsIgnoreCase(userName))
					&& (prop.getProperty("password").equalsIgnoreCase(mpwd))) {
				DynUserBean dub = new DynUserBean();
				dub.setPwssword(password);
				dub.setUserName(userName);
				req.getSession().setAttribute("dyn_user", dub);
				resp.sendRedirect(oldrequrl);
				return null;
			}
			Map map = new HashMap();
			map.put("oldrequrl", oldrequrl);
			req.setAttribute("data", map);
		} catch (IOException e) {
			logger.error("user validate loginValidate Redirect failure ", e);
		}

		return "login";
	}

	@RequestURL("/login/login")
	public String login(HttpServletRequest req, HttpServletResponse resp) {
		Map map = new HashMap();
		map.put("oldrequrl", req.getAttribute("oldrequrl"));
		req.setAttribute("data", map);
		return "login";
	}

	static {
		try {
			InputStream input = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("DataWarehouse/admin.properties");
			prop.load(input);
		} catch (IOException e) {
			logger.error("user resource properties load fail", e);
		}
	}
}