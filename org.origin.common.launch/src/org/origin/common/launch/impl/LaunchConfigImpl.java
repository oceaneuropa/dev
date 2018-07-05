package org.origin.common.launch.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.LaunchInternalService;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.LaunchType;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.util.LaunchConfigPersistence;
import org.origin.common.launch.util.LaunchExtensionHelper;
import org.origin.common.launch.util.LaunchExtensionHelper.LauncherExtension;

public class LaunchConfigImpl implements LaunchConfig {

	protected LaunchService launchService;
	protected Path configFilePath;
	protected String name;
	protected LaunchConfigData data;

	/**
	 * 
	 * @param launchService
	 * @param typeId
	 * @param configFilePath
	 */
	public LaunchConfigImpl(LaunchService launchService, String typeId, Path configFilePath) {
		this.launchService = launchService;
		this.configFilePath = configFilePath;

		LaunchConfigData data = new LaunchConfigData();
		data.setTypeId(typeId);

		setData(data);
	}

	/**
	 * 
	 * @param launchService
	 * @param configFilePath
	 */
	public LaunchConfigImpl(LaunchService launchService, Path configFilePath) {
		this.launchService = launchService;
		this.configFilePath = configFilePath;

		this.name = getSimpleName();
	}

	/**
	 * 
	 * @return
	 */
	protected synchronized LaunchConfigData getData() {
		if (this.data == null) {
			try {
				this.data = LaunchConfigPersistence.INSTANCE.load(getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.data;
	}

	/**
	 * 
	 * @param data
	 */
	protected synchronized void setData(LaunchConfigData data) {
		this.data = data;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	protected String getSimpleName() {
		String name = this.configFilePath.getFileName().toString();
		if (name.toLowerCase().endsWith(LaunchConstants.DOT_LAUNCH_EXTENSION)) {
			name = name.substring(0, name.lastIndexOf("."));
		}
		return name;
	}

	@Override
	public LaunchService getLaunchService() {
		return this.launchService;
	}

	@Override
	public String getName() {
		return getSimpleName();
	}

	@Override
	public File getFile() {
		return this.configFilePath.toFile();
	}

	@Override
	public boolean exists() {
		return getFile().exists();
	}

	@Override
	public void load() throws IOException {
		getData();
	}

	@Override
	public void save() throws IOException {
		LaunchConfigPersistence.INSTANCE.save(getData(), getFile());
	}

	@Override
	public boolean delete() throws IOException {
		this.data = null;
		return getFile().delete();
	}

	@Override
	public LaunchConfig getCopy(String name) {
		Path newPath = this.configFilePath.getParent().resolve(name);
		LaunchConfigImpl copy = new LaunchConfigImpl(this.launchService, newPath);
		copy.setData(getData().getCopy());
		return copy;
	}

	@Override
	public String getTypeId() {
		return getData().getTypeId();
	}

	@Override
	public boolean getAttribute(String attributeName, boolean defaultValue) throws IOException {
		return getData().getBooleanAttribute(attributeName, defaultValue);
	}

	@Override
	public int getAttribute(String attributeName, int defaultValue) throws IOException {
		return getData().getIntAttribute(attributeName, defaultValue);
	}

	@Override
	public List<String> getAttribute(String attributeName, List<String> defaultValue) throws IOException {
		return getData().getListAttribute(attributeName, defaultValue);
	}

	@Override
	public Set<String> getAttribute(String attributeName, Set<String> defaultValue) throws IOException {
		return getData().getSetAttribute(attributeName, defaultValue);
	}

	@Override
	public Map<String, String> getAttribute(String attributeName, Map<String, String> defaultValue) throws IOException {
		return getData().getMapAttribute(attributeName, defaultValue);
	}

	@Override
	public String getAttribute(String attributeName, String defaultValue) throws IOException {
		return getData().getStringAttribute(attributeName, defaultValue);
	}

	@Override
	public Map<String, Object> getAttributes() throws IOException {
		return getData().getAttributes();
	}

	@Override
	public void setAttribute(String attributeName, int value) {
		getData().setAttribute(attributeName, value);
	}

	@Override
	public void setAttribute(String attributeName, String value) {
		getData().setAttribute(attributeName, value);
	}

	@Override
	public void setAttribute(String attributeName, List<String> value) {
		getData().setAttribute(attributeName, value);
	}

	@Override
	public void setAttribute(String attributeName, Map<String, String> value) {
		getData().setAttribute(attributeName, value);
	}

	@Override
	public void setAttribute(String attributeName, Set<String> value) {
		getData().setAttribute(attributeName, value);
	}

	@Override
	public void setAttribute(String attributeName, boolean value) {
		getData().setAttribute(attributeName, value);
	}

	@Override
	public LaunchInstance launch() throws IOException {
		String typeId = getData().getTypeId();

		LaunchType launchType = this.launchService.getLaunchType(typeId);
		if (launchType == null) {
			throw new IOException("LaunchType is not found for '" + typeId + "'.");
		}

		LauncherExtension[] launcherExtensions = LaunchExtensionHelper.INSTANCE.getLauncherExtensions(typeId);
		if (launcherExtensions == null || launcherExtensions.length == 0) {
			throw new IOException("Launcher extensions are not available for type '" + typeId + "'.");
		}

		LauncherExtension launcherExtension = null;
		String launcherId = getAttribute(LAUNCHER_ID, (String) null);
		if (launcherId != null) {
			for (LauncherExtension currLauncherExtension : launcherExtensions) {
				if (launcherId.equals(currLauncherExtension.getId())) {
					launcherExtension = currLauncherExtension;
					break;
				}
			}
		}
		if (launcherExtension == null) {
			launcherExtension = launcherExtensions[0];
		}

		LaunchInstance launchInstance = createLaunchInstance();

		Launcher launcher = launcherExtension.getLauncher();
		if (launcher == null) {
			throw new IOException("Launcher is null from extension '" + launcherExtension.getId() + "'.");
		}
		launcher.launch(this, launchInstance);

		if (this.launchService instanceof LaunchInternalService) {
			((LaunchInternalService) this.launchService).launchInstanceAdded(launchInstance);
		}

		return launchInstance;
	}

	protected LaunchInstance createLaunchInstance() {
		return new LaunchInstanceImpl(this);
	}

}

// ILaunchService getLaunchService();
// boolean isDirty();

// protected boolean isDirty;

// @Override
// public boolean isDirty() {
// return this.isDirty;
// }
//
// protected void setDirty() {
// this.isDirty = true;
// }

// String[] launcherIds = launchType.getLauncherIds();
// if (launcherIds == null || launcherIds.length == 0) {
// throw new IOException("Launcher Ids are not available from the ILaunchType. Is the 'LAUNCHER_IDS' property set in the extension?");
// }
//
// String theLauncherId = null;
// String configuredLauncherId = getAttribute("LAUNCHER_ID", (String) null);
// if (configuredLauncherId != null) {
// boolean isValid = false;
// for (String currLauncherId : launcherIds) {
// if (currLauncherId.equals(configuredLauncherId)) {
// isValid = true;
// break;
// }
// }
// if (isValid) {
// theLauncherId = configuredLauncherId;
// }
// }
// if (theLauncherId == null) {
// theLauncherId = launcherIds[0];
// }

// setDirty();

// File newFile = new File(getFile().getParent(), name);
