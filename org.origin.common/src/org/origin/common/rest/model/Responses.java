package org.origin.common.rest.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Responses {

	@XmlElement
	protected String requestName;
	@XmlElement
	protected Map<String, Object> responseMap = new LinkedHashMap<String, Object>();

	public Responses() {
	}

	/**
	 * 
	 * @return
	 */
	@XmlElement
	public String getRequestName() {
		return this.requestName;
	}

	/**
	 * 
	 * @param requestName
	 */
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	@XmlTransient
	public boolean isEmpty() {
		return this.responseMap.isEmpty();
	}

	public void setResponse(Object value) {
		setResponse("response", value);
	}

	/**
	 * Set a response value.
	 * 
	 * @param key
	 * @param value
	 */
	public void setResponse(String key, Object value) {
		if (value != null) {
			this.responseMap.put(key, value);
		} else {
			this.responseMap.remove(key);
		}
	}

	/**
	 * Get a response value.
	 * 
	 * @param key
	 * @return
	 */
	@XmlTransient
	public Object getResponse() {
		return this.responseMap.get("response");
	}

	/**
	 * Get a response value.
	 * 
	 * @param key
	 * @return
	 */
	@XmlTransient
	public Object getResponse(String key) {
		return this.responseMap.get(key);
	}

	/**
	 * Get a response value.
	 * 
	 * @param clazz
	 * @return
	 */
	@XmlTransient
	public <T> T getResponse(Class<T> clazz) {
		return getResponse("response", clazz);
	}

	/**
	 * Get a response value.
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@XmlTransient
	public <T> T getResponse(String key, Class<T> clazz) {
		T t = null;
		Object object = this.responseMap.get(key);
		if (object != null && clazz.isAssignableFrom(object.getClass())) {
			t = (T) object;
		}
		return t;
	}

}
