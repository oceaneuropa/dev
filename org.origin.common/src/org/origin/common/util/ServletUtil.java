package org.origin.common.util;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletUtil {

	/**
	 * 
	 * @param request
	 * @param paramName
	 * @param defaultValue
	 * @return
	 */
	public static String getParameter(HttpServletRequest request, String paramName, String defaultValue) {
		String paramValue = null;
		if (request != null && paramName != null) {
			paramValue = request.getParameter(paramName);
		}
		if (paramValue == null && defaultValue != null) {
			paramValue = defaultValue;
		}
		return paramValue;
	}

	/**
	 * 
	 * @param request
	 * @param paramName
	 * @param defaultValues
	 * @return
	 */
	public static String[] getParameterValues(HttpServletRequest request, String paramName, String[] defaultValues) {
		String[] paramValues = null;
		if (request != null && paramName != null) {
			paramValues = request.getParameterValues(paramName);
		}
		if (paramValues == null && defaultValues != null) {
			paramValues = defaultValues;
		}
		return paramValues;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, Object> getInitParameters(ServletContext context) {
		Map<String, Object> initParameters = new LinkedHashMap<String, Object>();
		Enumeration<String> initParamNames = context.getInitParameterNames();
		while (initParamNames.hasMoreElements()) {
			String paramName = initParamNames.nextElement();
			Object paramValue = context.getInitParameter(paramName);
			initParameters.put(paramName, paramValue);
		}
		return initParameters;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, Object> getAttributes(ServletContext context) {
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
		Enumeration<String> attrNames = context.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String attrName = attrNames.nextElement();
			Object attrValue = context.getAttribute(attrName);
			attributes.put(attrName, attrValue);
		}
		return attributes;
	}

	/**
	 * 
	 * @param properties
	 * @param name
	 * @param clazz
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Map<String, Object> properties, String name, Class<T> clazz, T defaultValue) {
		T value = null;
		if (properties != null && name != null && clazz != null) {
			Object object = properties.get(name);
			if (object != null && clazz.isAssignableFrom(object.getClass())) {
				value = (T) object;
			}
		}
		if (value == null && defaultValue != null) {
			value = defaultValue;
		}
		return value;
	}

}
