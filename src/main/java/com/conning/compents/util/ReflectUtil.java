package com.conning.compents.util;

import java.lang.reflect.Method;

public class ReflectUtil
{
  public static Method getMethodByName(String methodName, Class clazz)
  {
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        return method;
      }
    }
    return null;
  }
}