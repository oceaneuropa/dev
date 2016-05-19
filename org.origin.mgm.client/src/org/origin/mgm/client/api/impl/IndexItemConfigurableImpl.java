package org.origin.mgm.client.api.impl;

import java.util.Map;

import javax.xml.namespace.QName;

import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemConfigurable;
import org.origin.mgm.client.api.IndexServiceConfiguration;

public class IndexItemConfigurableImpl extends IndexItemImpl implements IndexItem, IndexItemConfigurable {

	public IndexItemConfigurableImpl(IndexServiceConfiguration config, String indexProviderId, QName qName) {
		super(config, indexProviderId, qName);
	}

	/** implement IndexItemConfigurable interface */
	@Override
	public void update(Map<String, Object> properties) {

	}

	@Override
	public void delete() {

	}

	@Override
	public void setProperty(String propName, Object propValue) {

	}

	@Override
	public void setProperty(String propName, Object propValue, boolean isVolatile) {

	}

	@Override
	public void removeProperty(String propName) {

	}

}
