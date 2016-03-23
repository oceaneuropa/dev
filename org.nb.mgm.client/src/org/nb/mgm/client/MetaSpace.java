package org.nb.mgm.client;

import osgi.mgm.common.util.ClientException;
import osgi.mgm.common.util.IAdaptable;

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
