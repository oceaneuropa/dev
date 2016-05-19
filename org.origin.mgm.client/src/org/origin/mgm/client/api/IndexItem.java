package org.origin.mgm.client.api;

import java.util.Map;

import javax.xml.namespace.QName;

public interface IndexItem {

	/**
	 * 
	 * @return
	 */
	public String getIndexProviderId();

	/**
	 * 
	 * @return
	 */
	public QName getQName();

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getProperties();

}
