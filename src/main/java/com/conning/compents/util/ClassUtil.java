package com.conning.compents.util;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;

public class ClassUtil
{
  private static Logger logger = Logger.getLogger(ClassUtil.class);

  public static Class getOriginalClass(Class clazz) {
    if (clazz == null) {
      return null;
    }
    if (!clazz.getCanonicalName().contains("$$")) {
      return clazz;
    }
    return getOriginalClass(clazz.getSuperclass());
  }

  public static Object getFiledByGetMethod(String fieldName, Object obj)
  {
    Class objClass = getOriginalClass(obj.getClass());
    String objClassName = objClass.getCanonicalName();
    String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
    try {
      Method method = objClass.getMethod(methodName, null);
      if (method != null)
        return method.invoke(obj, new Object[0]);
    }
    catch (Exception e) {
      logger.error(objClassName + "has not the " + methodName + "method", e);
    }
    return null;
  }

  public static void setFieldBySetMethod()
  {
  }
}