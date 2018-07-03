package org.origin.common.rest.util;

import org.origin.common.rest.model.Request;

public class RequestUtil {

	/**
	 * 
	 * @param request
	 * @param paramName
	 * @param clazz
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getParameter(Request request, String paramName, Class<T> clazz, T defaultValue) {
		T paramValue = null;
		if (request != null && paramName != null) {
			Object object = request.getParameter(paramName);
			if (object != null && clazz.isAssignableFrom(object.getClass())) {
				paramValue = (T) object;
			}
		}
		if (paramValue == null && defaultValue != null) {
			paramValue = defaultValue;
		}
		return paramValue;
	}

}
