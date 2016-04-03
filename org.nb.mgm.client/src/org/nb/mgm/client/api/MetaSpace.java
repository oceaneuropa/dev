package org.nb.mgm.client.api;

import org.nb.mgm.client.util.ClientException;
import org.nb.mgm.client.util.IAdaptable;

public interface MetaSpace extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public Management getManagement();

	public MetaSector getMetaSector();

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

	public String getDescription();

	public void setDescription(String description) throws ClientException;

}
