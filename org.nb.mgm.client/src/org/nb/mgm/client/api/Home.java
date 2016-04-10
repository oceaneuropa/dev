package org.nb.mgm.client.api;

import org.nb.mgm.client.util.ClientException;
import org.nb.mgm.client.util.IAdaptable;

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

	public void update() throws ClientException;

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

}
