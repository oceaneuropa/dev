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
		return requestName;
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

	/**
	 * Set a response value.
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
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
	public Object get(String key) {
		return this.responseMap.get(key);
	}

}
