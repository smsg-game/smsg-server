package com.lodogame.ldsg.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

public class CompressHelper {

	private static final Logger LOG = Logger.getLogger(CompressHelper.class);

	/**
	 * 字段压缩传输
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object compress(Object obj) {

		Class<?> cls = obj.getClass();

		if (cls.isAnnotationPresent(Compress.class)) {

			Map<String, Object> ret = new HashMap<String, Object>();

			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {

				if (!field.isAnnotationPresent(Mapper.class)) {
					continue;
				}

				Mapper mapper = field.getAnnotation(Mapper.class);
				String key = mapper.name();
				Object value = getFieldValue(obj, cls, field.getName());
				if (value != null) {
					value = compress(value);
				}
				ret.put(key, value);
			}

			return ret;

		} else if (obj instanceof List) {

			List<Object> objList = new ArrayList<Object>();
			for (Object o : (List<Object>) obj) {
				objList.add(compress(o));
			}

			return objList;

		} else if (obj instanceof Map) {

			Map<String, Object> objMap = new HashMap<String, Object>();

			Map<String, Object> m = (Map<String, Object>) obj;

			for (Map.Entry<String, Object> entry : m.entrySet()) {
				objMap.put(entry.getKey(), compress(entry.getValue()));
			}

			return objMap;

		}

		return obj;

	}

	/**
	 * 读取一个字段值
	 * 
	 * @param obj
	 * @param cls
	 * @param name
	 * @return
	 */
	public static Object getFieldValue(Object obj, Class<?> cls, String name) {

		try {

			Class<?>[] parmTypes = {};
			Method method = cls.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), parmTypes);
			return method.invoke(obj, new Object[] {});

		} catch (InvocationTargetException ie) {
			LOG.error(ie.getMessage(), ie);
		} catch (IllegalAccessException iie) {
			LOG.error(iie.getMessage(), iie);
		} catch (NoSuchMethodException ne) {
			LOG.error(ne.getMessage(), ne);
		}

		return null;
	}
}
