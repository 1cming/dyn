package com.conning.compents.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.conning.compents.annotation.Compareable;

public class CollectionUtilsExt<T> {
	private Comparator<T> comparator;

	public CollectionUtilsExt(Class<T> t) {
		Compareable annotation = (Compareable) t.getAnnotation(Compareable.class);
		if (annotation == null) {
			throw new RuntimeException(t.getName() + " dont have @Compareable!");
		}
		String filedName = annotation.value();
		final String filedMethodName = "get" + filedName.substring(0, 1).toUpperCase()
				+ filedName.substring(1, filedName.length());
		Class adapter = annotation.adapter();
		if ((adapter != null) && (!adapter.getSimpleName().equals(Object.class.getSimpleName())))
			try {
				Object c = adapter.newInstance();
				if ((c instanceof Comparator))
					this.comparator = ((Comparator) c);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		else
			this.comparator = new Comparator() {
				@Override
				public int compare(Object t1, Object t2) {
					try {
						Method mt1 = t1.getClass().getMethod(filedMethodName, null);
						Method mt2 = t2.getClass().getMethod(filedMethodName, null);
						if ((mt1 == null) || (mt2 == null)) {
							throw new RuntimeException(
									t1.getClass().getName() + " dont have the method '" + filedMethodName + "'");
						}

						Object v1 = mt1.invoke(t1, null);
						Object v2 = mt2.invoke(t2, null);
						if ((v1 instanceof Integer))
							return ((Integer) v1).intValue() - ((Integer) v2).intValue();
						if ((v1 instanceof Double))
							return ((Double) v1).intValue() - ((Double) v2).intValue();
						if ((v1 instanceof Float))
							return ((Float) v1).intValue() - ((Float) v2).intValue();
						if ((v1 instanceof Character))
							return ((Character) v1).charValue() - ((Character) v2).charValue();
						if ((v1 instanceof String))
							return ((String) v1).compareTo((String) v2);
						if ((v1 instanceof List)) {
							List lv1 = (List) v1;
							List lv2 = (List) v2;
							if ((lv1 == null) && (lv2 == null))
								return 0;
							if (lv1 == null)
								return -1;
							if (lv2 == null)
								return 1;
							if (lv1.size() < lv2.size()) {
								return -1;
							}
							return 1;
						}

					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					return 0;
				}

			};
	}

	public void sort(List list) {
		Collections.sort(list, this.comparator);
	}
}