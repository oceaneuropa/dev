package org.nb.mgm.client.api;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface ISoftware extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	public ManagementClient getManagement();

	public IProject getProject();

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

	public String getType();

	public void setType(String type) throws ClientException;

	public String getName();

	public void setName(String name) throws ClientException;

	public String getVersion();

	public void setVersion(String version) throws ClientException;

	public String getDescription();

	public void setDescription(String description) throws ClientException;

	public long getLength();

	public void setLength(long length) throws ClientException;

	public Date getLastModified();

	public void setLastModified(Date lastModified) throws ClientException;

	public String getMd5();

	public void setMd5(String md5) throws ClientException;

	// public String getLocalPath();

	public String getFileName();

	// ------------------------------------------------------------------------------------------
	// File content
	// ------------------------------------------------------------------------------------------
	/**
	 * Tests whether the Software file exists.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean exists() throws ClientException;

	/**
	 * Upload Software to Project.
	 * 
	 * @param srcFile
	 * @return
	 * @throws ClientException
	 */
	public boolean uploadSoftware(File srcFile) throws ClientException;

	/**
	 * Download Software from Project.
	 * 
	 * @param destFile
	 * @throws ClientException
	 */
	public boolean downloadSoftware(File destFile) throws ClientException;

	/**
	 * Download Software from Project.
	 * 
	 * @param destFile
	 * @throws ClientException
	 */
	public boolean downloadSoftware(OutputStream output) throws ClientException;

}
