package com.conning.compents.servlet;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class ExcuteBean {
	private Class beanName;
	private Method methodName;
	private Object[] params;

	public Class getBeanName() {
		return this.beanName;
	}

	public void setBeanName(Class beanName) {
		this.beanName = beanName;
	}

	public Method getMethodName() {
		return this.methodName;
	}

	public void setMethodName(Method methodName) {
		this.methodName = methodName;
	}

	public Object excute(Object[] params) throws Exception {
		this.params = params;
		Object obj = this.beanName.newInstance();
		return this.methodName.invoke(obj, params);
	}

	private Class[] getParamsClass() {
		if (this.params == null)
			return null;
		Class[] classes = new Class[this.params.length];
		for (int i = 0; i < this.params.length; i++) {
			classes[i] = this.params[i].getClass();
		}
		return classes;
	}
}