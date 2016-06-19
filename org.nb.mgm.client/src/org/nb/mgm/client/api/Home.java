package org.nb.mgm.client.api;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface Home extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public Management getManagement();

	public Machine getMachine();

	// ------------------------------------------------------------------------------------------
	// Auto Update Attributes
	// ------------------------------------------------------------------------------------------
	public boolean isAutoUpdate();

	public void setAutoUpdate(boolean autoUpdate);

	public boolean update() throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	public String getId();

	public String getName();

	public void setName(String name) throws ClientException;

	public String getUrl();

	public void setUrl(String url) throws ClientException;

	public String getDescription();

	public void setDescription(String description) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------------------------
	public Map<String, Object> getProperties() throws ClientException;

}
