package com.conning.compents.servlet.page;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.conning.compents.annotation.Action;
import com.conning.compents.annotation.RequestURL;
import com.conning.compents.monitor.MonitorableCodeBean;
import com.conning.compents.servlet.ComponentDirectory;
import com.conning.compents.servlet.ComponentMgr;
import com.conning.compents.util.AopTargetUtils;
import com.conning.compents.util.CollectionUtilsExt;

@Action(value = "adminCompentContorl")
public class AdminCompentContorl {
	Logger logger = Logger.getLogger(AdminCompentContorl.class);

	@RequestURL("")
	public String adminCompents(HttpServletRequest req, HttpServletResponse resp) {
		return "admin";
	}

	@RequestURL("/spring")
	public String ComponentBrowser(HttpServletRequest req, HttpServletResponse resp) {
		Map map = new HashMap();
		String id = req.getParameter("id");
		String beanName = req.getParameter("beanName");
		List list = null;
		if ((StringUtils.isEmpty(id)) && (StringUtils.isEmpty(beanName))) {
			list = ComponentMgr.queryComponentDirectory(Integer.valueOf(1), null);
		} else if (!StringUtils.isEmpty(id)) {
			ComponentDirectory componentDirectory = ComponentMgr.queryComponentById(id);
			list = componentDirectory.getChildren();
			String[] beanUrls = CompentsUtil.getBeanName(componentDirectory, req);
			map.put("beanUrl", beanUrls[1]);
		} else if (!StringUtils.isEmpty(beanName)) {
			list = ComponentMgr.queryComponentDirectory(beanName);
		}
		CollectionUtilsExt util = new CollectionUtilsExt(ComponentDirectory.class);
		if (list != null)
			util.sort(list);
		map.put("data", list);
		req.setAttribute("data", map);
		return "components";
	}

	@RequestURL("/springBean")
	public String showBeanDetails(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		Map map = new HashMap();
		ComponentDirectory componentDirectory = ComponentMgr.queryComponentById(id);
		String[] beanUrls = CompentsUtil.getBeanName(componentDirectory, req);
		Object obj = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext())
				.getBean(beanUrls[0]);
		try {
			obj = AopTargetUtils.getTarget(obj);
		} catch (Exception e1) {
			this.logger.error("获取目标类失败", e1);
		}
		Class objClass = obj.getClass();
		String objClassName = objClass.getCanonicalName();
		Field[] fields = objClass.getDeclaredFields();
		List fieldList = new ArrayList();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object value = field.get(obj);

				String objValue = null;
				if (((value instanceof String)) || ((value instanceof Float)) || ((value instanceof Double))
						|| ((value instanceof Boolean)) || ((value instanceof Character))
						|| ((value instanceof Integer)) || ((value instanceof Long)) || ((value instanceof Short))) {
					objValue = value.toString();
				} else if (value == null)
					objValue = "";
				else {
					objValue = value.getClass().getName();
				}
				FieldBean fb = new FieldBean();
				fb.setName(field.getName());
				fb.setValue(objValue);
				fb.setType(field.getType().getName());
				field.setAccessible(false);
				fieldList.add(fb);
			}
		} catch (SecurityException e) {
			this.logger.error(e);
		} catch (IllegalArgumentException e) {
			this.logger.error(e);
		} catch (IllegalAccessException e) {
			this.logger.error(e);
		}

		List methodList = new ArrayList();
		processMethod(objClass, methodList);

		if (objClass.isAssignableFrom(MonitorableCodeBean.class)) {
			MonitorableCodeBean bean = (MonitorableCodeBean) obj;
			Map monitorData = bean.getMonitorData();
			Map monitDataMap = new HashMap();
			monitDataMap.put("themes", monitorData.keySet());
			Map themeProperties = bean.getMonitorDataProp();
			monitDataMap.put("themeProperties", themeProperties);
			monitDataMap.put("data", monitorData);
			map.put("monitData", monitDataMap);
		}

		map.put("methodList", methodList);
		map.put("fieldList", fieldList);
		map.put("stringValue", obj.toString());
		map.put("beanUrl", beanUrls[1]);
		map.put("id", id);
		req.setAttribute("data", map);
		map.put("beanName", objClassName);
		return "classdetail";
	}

	private void processMethod(Class clazz, List list) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 0) {
				MethodBean mb = new MethodBean();
				mb.setName(method.getName());
				mb.setReturnType(method.getReturnType().getName());
				mb.setDeclaringClass(method.getDeclaringClass().getName());
				list.add(mb);
			}
		}
		if (clazz.getSuperclass() != null)
			processMethod(clazz.getSuperclass(), list);
	}
}