package com.miss.server.alert.domain.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

	public static <F, C> C fatherToChild(F father, Class<C> childClass) throws Exception {
		if (childClass.getSuperclass() != father.getClass()) {
			throw new Exception("child 不是 father 的子类");
		}

		Constructor<C> constructor = childClass.getConstructor();
		C child = constructor.newInstance();

		Field[] declaredFields = father.getClass().getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field field = declaredFields[i];
			Method method = father.getClass().getDeclaredMethod("get" + upperHeadChar(field.getName()));
			Object obj = method.invoke(father);
			field.setAccessible(true);
			field.set(child, obj);
		}

		return child;
	}

	private static String upperHeadChar(String in) {
		String head = in.substring(0, 1);
		String out = head.toUpperCase() + in.substring(1, in.length());
		return out;
	}

}
