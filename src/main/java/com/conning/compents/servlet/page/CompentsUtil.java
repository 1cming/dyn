package com.conning.compents.servlet.page;

import javax.servlet.http.HttpServletRequest;

import com.conning.compents.servlet.ComponentDirectory;

public class CompentsUtil {
	public static String[] getBeanName(ComponentDirectory cd, HttpServletRequest req) {
		String path = req.getContextPath();
		String basePath = "";
		if (req.getServerPort() == 80)
			basePath = req.getScheme() + "://" + req.getServerName() + path;
		else {
			basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path;
		}

		String beanName = cd.getPackageName();
		String beanUrlName = "";
		do {
			if (cd.getParent() != null)
				beanName = cd.getParent().getPackageName() + "." + beanName;
			String tempBeanUrl = "<a href=\"" + basePath + "/dyn/admin/spring";
			if (cd.isLeaf())
				tempBeanUrl = tempBeanUrl + "Bean";
			tempBeanUrl = tempBeanUrl + "?id=" + cd.getId() + "\">" + cd.getPackageName() + "/</a>";

			beanUrlName = tempBeanUrl + beanUrlName;
			cd = cd.getParent();
		} while (cd != null);
		beanUrlName = "<a href=\"" + basePath + "/dyn/admin/spring\">Service</a> &nbsp;&nbsp;&nbsp;" + beanUrlName;

		return new String[] { beanName, beanUrlName };
	}
}