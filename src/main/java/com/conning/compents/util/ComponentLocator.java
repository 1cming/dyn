package com.conning.compents.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ComponentLocator implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> cla) {
		Object obj = applicationContext.getBean(beanName, cla);
		if (obj != null) {
			return (T) obj;
		}
		return null;
	}

	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}
}
