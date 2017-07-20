package com.conning.compents.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class DynamicMergePropertiesBean
		implements InitializingBean, DisposableBean, ApplicationContextAware, Observer {
	private Log logger = LogFactory.getLog(DynamicMergePropertiesBean.class);
	private ApplicationContext context;
	private ConcurrentHashMap<String, List<Field>> localMap = new ConcurrentHashMap();

	public void update(Observable o, Object arg) {
		if ((arg instanceof String)) {
			String beanName = (String) arg;
			List<Field> fieldList = (List<Field>) this.localMap.get(beanName);
			if (fieldList != null)
				for (Field field : fieldList) {
					Value value = (Value) field.getAnnotation(Value.class);
					String configId = getConfigId(value.value());
					String propName = getPropertiesName(value.value());
					ObservedProperties prop = (ObservedProperties) this.context.getBean(configId,
							ObservedProperties.class);
					Object vObject = prop.get(propName);
					try {
						setFieldValue(field, vObject);
					} catch (Exception e) {
						if (vObject == null)
							vObject = "NULL";
						this.logger.error("Class " + getClass().getCanonicalName() + "update value error,the field is "
								+ field.getName() + " , the value type is " + vObject.toString(), e);
					}
				}
		}
	}

	protected void setFieldValue(Field field, Object vObject) throws Exception {
		if ((field == null) || (vObject == null)) {
			return;
		}
		Boolean oldAccessible = Boolean.valueOf(field.isAccessible());
		if ((!Modifier.isFinal(field.getModifiers())) || (!oldAccessible.booleanValue())) {
			field.setAccessible(true);
		}
		if (field.getType().isAssignableFrom(vObject.getClass())) {
			field.set(this, vObject);
		} else if ((vObject instanceof String)) {
			if ((field.getType().isAssignableFrom(Integer.class)) || (field.getType().isAssignableFrom(Integer.TYPE)))
				vObject = Integer.valueOf(Integer.parseInt(vObject.toString()));
			else if ((field.getType().isAssignableFrom(Boolean.class))
					|| (field.getType().isAssignableFrom(Boolean.TYPE)))
				vObject = Boolean.valueOf(vObject.toString());
			else if ((field.getType().isAssignableFrom(Double.class))
					|| (field.getType().isAssignableFrom(Double.TYPE)))
				vObject = Double.valueOf(Double.parseDouble(vObject.toString()));
			else if ((field.getType().isAssignableFrom(Long.class)) || (field.getType().isAssignableFrom(Long.TYPE)))
				vObject = Long.valueOf(Long.parseLong(vObject.toString()));
			else if (field.getType().isAssignableFrom(Class.class)) {
				vObject = Class.forName(vObject.toString().trim());
			}
			field.set(this, vObject);
		}

		if (!oldAccessible.booleanValue())
			field.setAccessible(oldAccessible.booleanValue());
	}

	public void afterPropertiesSet() throws Exception {
		Field[] fileds = getClass().getDeclaredFields();
		if (ArrayUtils.isNotEmpty(fileds))
			for (Field field : fileds) {
				Value value = (Value) field.getAnnotation(Value.class);
				if (value != null) {
					String values = value.value();
					String configId = getConfigId(values);
					ObservedProperties prop = (ObservedProperties) this.context.getBean(configId,
							ObservedProperties.class);
					prop.addObserver(this);
					put(configId, field);
				}
			}
	}

	private void put(String beanName, Field field) {
		List fieldList = (List) this.localMap.get(beanName);
		if (fieldList == null) {
			fieldList = new ArrayList();
			this.localMap.put(beanName, fieldList);
		}
		if (!fieldList.contains(field))
			fieldList.add(field);
	}

	public void setApplicationContext(ApplicationContext paramApplicationContext) throws BeansException {
		this.context = paramApplicationContext;
	}

	private String getConfigId(String values) {
		return values.substring(values.indexOf("{") + 1, values.indexOf("[")).trim();
	}

	private String getPropertiesName(String values) {
		return values.substring(values.indexOf("[") + 1, values.indexOf("]")).trim();
	}

	public void destroy() throws Exception {
		Field[] fileds = getClass().getDeclaredFields();
		if (ArrayUtils.isNotEmpty(fileds))
			for (Field field : fileds) {
				Value value = (Value) field.getAnnotation(Value.class);
				if (value != null) {
					String values = value.value();
					String configId = getConfigId(values);
					ObservedProperties prop = (ObservedProperties) this.context.getBean(configId,
							ObservedProperties.class);
					prop.deleteObserver(this);
				}
			}
	}
}