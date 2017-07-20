package com.conning.compents.servlet.page;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.conning.compents.annotation.Action;
import com.conning.compents.annotation.RequestURL;
import com.conning.compents.servlet.ComponentDirectory;
import com.conning.compents.servlet.ComponentMgr;
import com.conning.compents.util.AopTargetUtils;

@Action(value = "beanContorl")
@RequestURL("/spring")
public class BeanContorl {
	Logger logger = Logger.getLogger(BeanContorl.class);

	@RequestURL("/method/comfirmInvoke")
	public String comfirmInvokeMethod(HttpServletRequest req, HttpServletResponse resp) {
		Map map = new HashMap();
		String id = req.getParameter("id");
		String methodName = req.getParameter("methodName");
		ComponentDirectory componentDirectory = ComponentMgr.queryComponentById(id);
		String[] beanNames = CompentsUtil.getBeanName(componentDirectory, req);
		map.put("beanUrl", beanNames[1]);
		map.put("id", id);
		map.put("methodName", methodName);
		req.setAttribute("data", map);
		return "invokeconfirm";
	}

	@RequestURL("/prop")
	public String prop(HttpServletRequest req, HttpServletResponse resp) {
		Map map = new HashMap();
		String id = req.getParameter("id");
		String propertyName = req.getParameter("propertyName");
		String newValue = req.getParameter("newValue");
		ComponentDirectory componentDirectory = ComponentMgr.queryComponentById(id);
		String[] beanNames = CompentsUtil.getBeanName(componentDirectory, req);
		Object obj = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext())
				.getBean(beanNames[0]);
		try {
			obj = AopTargetUtils.getTarget(obj);
		} catch (Exception e1) {
			this.logger.error("获取目标类失败", e1);
		}
		String propertyValue = "";
		String propertyType = "";

		boolean changeAble = false;
		Boolean finalField = Boolean.valueOf(false);
		try {
			Field field = obj.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			propertyType = field.getType().getName();
			propertyValue = field.get(obj) == null ? "" : field.get(obj).toString();
			finalField = Boolean.valueOf(Modifier.isFinal(field.getModifiers()));
			if (StringUtils.isEmpty(newValue)) {
				changeAble = changeAble(propertyType);
				if ((changeAble) && (finalField.booleanValue()))
					changeAble = false;
			} else {
				String oldValue = propertyValue;
				Object value = getNewValue(propertyType, newValue);
				propertyValue = value.toString();
				field.set(obj, value);
				this.logger.info("<<<<<<<<<<<<<<<SpringAdmin:   " + obj.getClass().getName()
						+ "' properties changed from " + oldValue + " to " + newValue);
				changeAble = true;
			}
			field.setAccessible(false);
		} catch (Exception localException1) {
		}
		map.put("finalField", finalField.toString());
		map.put("beanUrl", beanNames[1]);
		map.put("id", id);
		map.put("changeAble", Boolean.valueOf(changeAble));
		map.put("propertyName", propertyName);
		map.put("propertyValue", propertyValue);
		map.put("propertyType", propertyType);
		map.put("beanName", beanNames[0]);
		req.setAttribute("data", map);
		return "changefield";
	}

	@RequestURL("/method/invoke")
	public String invokeMethod(HttpServletRequest req, HttpServletResponse resp) {
		Map map = new HashMap();
		String id = req.getParameter("id");
		String methodName = req.getParameter("invokeMethod");
		ComponentDirectory componentDirectory = ComponentMgr.queryComponentById(id);
		String[] beanNames = CompentsUtil.getBeanName(componentDirectory, req);
		Object obj = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext())
				.getBean(beanNames[0]);
		try {
			obj = AopTargetUtils.getTarget(obj);
		} catch (Exception e1) {
			this.logger.error("获取目标类失败", e1);
		}
		String errMsg = "";
		Object value = null;
		try {
			Method method = getMethod(obj.getClass(), methodName);
			method.setAccessible(true);
			value = method.invoke(obj, null);
			this.logger.info("<<<<<<<<<<<<<<<SpringAdmin:   " + obj.getClass().getName() + "'method[" + methodName
					+ "] have invoked");
		} catch (Exception e) {
			e.printStackTrace();
			StackTraceElement[] ses = e.getStackTrace();
			StringBuffer sb = new StringBuffer();
			sb.append(e.toString() + "<br/>");
			for (StackTraceElement se : ses) {
				sb.append(se.toString() + "<br/>");
			}
			errMsg = sb.toString();
		} finally {
			map.put("beanUrl", beanNames[1]);
			map.put("methodName", methodName);
			map.put("errMsg", errMsg);
			if (value != null) {
				map.put("returnValue", value.toString());
				map.put("returnClass", value.getClass().getName());
			} else {
				map.put("returnValue", "null");
				map.put("returnClass", "null");
			}
			req.setAttribute("data", map);
		}
		return "invoke";
	}

	private Method getMethod(Class clazz, String methodName) {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, new Class[0]);
		} catch (Exception e) {
			if (clazz.getSuperclass() != null) {
				method = getMethod(clazz.getSuperclass(), methodName);
			}
		}
		return method;
	}

	private boolean changeAble(String classType) {
		boolean changeAble = false;
		if (("boolean".equals(classType)) || ("java.lang.Boolean".equals(classType)))
			changeAble = true;
		else if (("int".equals(classType)) || ("java.lang.Integer".equals(classType)))
			changeAble = true;
		else if (("char".equals(classType)) || ("java.lang.Character".equals(classType)))
			changeAble = true;
		else if (("double".equals(classType)) || ("java.lang.Double".equals(classType)))
			changeAble = true;
		else if (("float".equals(classType)) || ("java.lang.Float".equals(classType)))
			changeAble = true;
		else if (("long".equals(classType)) || ("java.lang.Long".equals(classType)))
			changeAble = true;
		else if (("short".equals(classType)) || ("java.lang.Short".equals(classType)))
			changeAble = true;
		else if ("java.lang.String".equals(classType)) {
			changeAble = true;
		}
		return changeAble;
	}

	private Object getNewValue(String classType, String newValue) {
		Object value = null;
		if (("boolean".equals(classType)) || ("java.lang.Boolean".equals(classType)))
			value = Boolean.valueOf(newValue);
		else if (("int".equals(classType)) || ("java.lang.Integer".equals(classType)))
			value = Integer.valueOf(newValue);
		else if (("char".equals(classType)) || ("java.lang.Character".equals(classType)))
			value = Character.valueOf(newValue.charAt(0));
		else if (("double".equals(classType)) || ("java.lang.Double".equals(classType)))
			value = Double.valueOf(newValue);
		else if (("float".equals(classType)) || ("java.lang.Float".equals(classType)))
			value = Float.valueOf(newValue);
		else if (("long".equals(classType)) || ("java.lang.Long".equals(classType)))
			value = Long.valueOf(newValue);
		else if (("short".equals(classType)) || ("java.lang.Short".equals(classType)))
			value = Short.valueOf(newValue);
		else if ("java.lang.String".equals(classType)) {
			value = String.valueOf(newValue);
		}
		return value;
	}
}