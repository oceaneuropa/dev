package org.nb.mgm.client.api.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;

import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.model.dto.SoftwareDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;

public class SoftwareImpl implements ISoftware {

	private ManagementClient management;
	private IProject project;
	private SoftwareDTO softwareDTO;

	private AdaptorSupport adaptorSupport = new AdaptorSupport();
	private boolean autoUpdate = false;

	/**
	 * 
	 * @param management
	 * @param project
	 * @param softwareDTO
	 */
	public SoftwareImpl(ManagementClient management, IProject project, SoftwareDTO softwareDTO) {
		this.management = management;
		this.project = project;
		this.softwareDTO = softwareDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public ManagementClient getManagement() {
		return this.management;
	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	// ------------------------------------------------------------------------------------------
	// Auto Update Attributes
	// ------------------------------------------------------------------------------------------
	@Override
	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	@Override
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public boolean update() throws ClientException {
		checkManagement(this.management);
		checkProject(project);

		String projectId = this.project.getId();
		return this.management.updateProjectSoftware(projectId, this.softwareDTO);
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.softwareDTO.getId();
	}

	@Override
	public String getType() {
		return this.softwareDTO.getType();
	}

	@Override
	public void setType(String type) throws ClientException {
		String oldType = this.softwareDTO.getType();

		if ((oldType == null && type != null) || (oldType != null && !oldType.equals(type))) {
			this.softwareDTO.setType(type);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getName() {
		return this.softwareDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.softwareDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.softwareDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getVersion() {
		return this.softwareDTO.getVersion();
	}

	@Override
	public void setVersion(String version) throws ClientException {
		String oldVersion = this.softwareDTO.getVersion();

		if ((oldVersion == null && version != null) || (oldVersion != null && !oldVersion.equals(version))) {
			this.softwareDTO.setVersion(version);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.softwareDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.softwareDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.softwareDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public long getLength() {
		return this.softwareDTO.getLength();
	}

	@Override
	public void setLength(long length) throws ClientException {
		long oldLength = this.softwareDTO.getLength();

		if (oldLength != length) {
			this.softwareDTO.setLength(length);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public Date getLastModified() {
		return this.softwareDTO.getLastModified();
	}

	@Override
	public void setLastModified(Date lastModified) throws ClientException {
		Date oldLastModified = this.softwareDTO.getLastModified();

		if ((oldLastModified == null && lastModified != null) || (oldLastModified != null && !oldLastModified.equals(lastModified))) {
			this.softwareDTO.setLastModified(lastModified);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getMd5() {
		return this.softwareDTO.getMd5();
	}

	@Override
	public void setMd5(String md5) throws ClientException {
		String oldMd5 = this.softwareDTO.getMd5();

		if ((oldMd5 == null && md5 != null) || (oldMd5 != null && !oldMd5.equals(md5))) {
			this.softwareDTO.setMd5(md5);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// @Override
	// public String getLocalPath() {
	// return this.softwareDTO.getLocalPath();
	// }

	@Override
	public String getFileName() {
		return this.softwareDTO.getFileName();
	}

	// ------------------------------------------------------------------------------------------
	// File content
	// ------------------------------------------------------------------------------------------
	/**
	 * Tests whether the Software file exists.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public boolean exists() throws ClientException {
		if (getLength() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean uploadSoftware(File srcFile) throws ClientException {
		checkManagement(this.management);
		checkProject(project);

		String projectId = this.project.getId();
		String softwareId = getId();
		return this.management.uploadProjectSoftwareFile(projectId, softwareId, srcFile);
	}

	@Override
	public boolean downloadSoftware(File destFile) throws ClientException {
		checkManagement(this.management);
		checkProject(project);

		String projectId = this.project.getId();
		String softwareId = getId();
		return this.management.downloadProjectSoftwareToFile(projectId, softwareId, destFile);
	}

	@Override
	public boolean downloadSoftware(OutputStream output) throws ClientException {
		checkManagement(this.management);
		checkProject(project);

		String projectId = this.project.getId();
		String softwareId = getId();
		return this.management.downloadProjectSoftwareToOutputStream(projectId, softwareId, output);
	}

	// ------------------------------------------------------------------------------------------
	// Check Management Client
	// ------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param management
	 * @throws ClientException
	 */
	protected void checkManagement(ManagementClient management) throws ClientException {
		if (management == null) {
			throw new ClientException(401, "management is null.", null);
		}
	}

	/**
	 * 
	 * @param project
	 * @throws ClientException
	 */
	protected void checkProject(IProject project) throws ClientException {
		if (project == null) {
			throw new ClientException(401, "project is null.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (SoftwareDTO.class.equals(adapter)) {
			return (T) this.softwareDTO;
		}
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Software(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", type=\"").append(getType()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", version=\"").append(getVersion()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(", length=\"").append(getLength()).append("\"");
		sb.append(", lastModified=\"").append(getLastModified() != null ? getLastModified() : "").append("\"");
		sb.append(", md5=\"").append(getMd5() != null ? getMd5() : "").append("\"");
		sb.append(")");
		return sb.toString();
	}

}
