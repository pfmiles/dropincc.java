package com.github.pfmiles.dropincc.testhelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Some helper methods to help tests.
 * 
 * @author pf-miles
 * 
 */
public class TestHelper {
	/**
	 * invoke non-public method of a class
	 * 
	 * @param obj
	 * @param mName
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T priIvk(Object obj, String mName, Object... args) {
		Class<?>[] parameterTypes = null;
		if (args != null && args.length > 0) {
			List<Class<?>> ats = new ArrayList<Class<?>>();
			for (Object arg : args) {
				ats.add(arg.getClass());
			}
			parameterTypes = ats.toArray(new Class<?>[ats.size()]);
		}
		Method m;
		try {
			m = obj.getClass().getDeclaredMethod(mName, parameterTypes);
			m.setAccessible(true);
			return (T) m.invoke(obj, args);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getCause().getMessage(), e.getCause());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * fetch non-public field value
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T priField(Object obj, String fieldName) {
		if (obj == null)
			return null;
		Class<?> cls = obj.getClass();
		Field f;
		try {
			f = cls.getDeclaredField(fieldName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (f == null)
			return null;
		f.setAccessible(true);
		try {
			return (T) f.get(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
