/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.fileinstall.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.felix.cm.file.ConfigurationHandler;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.apache.felix.fileinstall.ArtifactListener;
import org.apache.felix.fileinstall.internal.Util.Logger;
import org.apache.felix.utils.collections.DictionaryAsMap;
import org.apache.felix.utils.properties.InterpolationHelper;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;

/**
 * ArtifactInstaller for cif (or config) file.
 * 
 * @see http://felix.apache.org/documentation/subprojects/apache-felix-config-admin.html
 * @see http://blog.osgi.org/2010/06/how-to-use-config-admin.html
 */
public class ConfigInstaller implements ArtifactInstaller, ConfigurationListener {

	protected BundleContext bundleContext;
	protected ConfigurationAdmin configAdmin;
	protected FileInstall fileInstall;

	protected ServiceRegistration<?> registration;

	/**
	 * 
	 * @param bundleContext
	 * @param configAdmin
	 * @param fileInstall
	 */
	public ConfigInstaller(BundleContext bundleContext, ConfigurationAdmin configAdmin, FileInstall fileInstall) {
		this.bundleContext = bundleContext;
		this.configAdmin = configAdmin;
		this.fileInstall = fileInstall;
	}

	public void start() {
		Printer.pl("ConfigInstaller.start()");
		registration = this.bundleContext.registerService(new String[] { ConfigurationListener.class.getName(), ArtifactListener.class.getName(), ArtifactInstaller.class.getName() }, this, null);
	}

	public void stop() {
		Printer.pl("ConfigInstaller.stop()");
		registration.unregister();
	}

	/** ArtifactListener */
	@Override
	public boolean canHandle(File artifact) {
		boolean canHandle = artifact.getName().endsWith(".cfg") || artifact.getName().endsWith(".config");
		if (canHandle) {
			Printer.pl("ConfigInstaller.canHandle()");
			Printer.pl("\tartifact = " + artifact.getAbsolutePath());
		}
		return canHandle;
	}

	/** ArtifactInstaller */
	@Override
	public void install(File artifact) throws Exception {
		Printer.pl("ConfigInstaller.install()");
		Printer.pl("\tartifact = " + artifact.getAbsolutePath());

		setConfig(artifact);
	}

	@Override
	public void update(File artifact) throws Exception {
		Printer.pl("ConfigInstaller.update()");
		Printer.pl("\tartifact = " + artifact.getAbsolutePath());

		setConfig(artifact);
	}

	@Override
	public void uninstall(File artifact) throws Exception {
		Printer.pl("ConfigInstaller.uninstall()");
		Printer.pl("\tartifact = " + artifact.getAbsolutePath());

		deleteConfig(artifact);
	}

	/** ConfigurationListener */
	@Override
	public void configurationEvent(ConfigurationEvent configurationEvent) {
		// Check if writing back configurations has been disabled.
		if (!shouldSaveConfig()) {
			return;
		}

		if (configurationEvent.getType() == ConfigurationEvent.CM_UPDATED) {
			try {
				Configuration config = configAdmin.getConfiguration(configurationEvent.getPid(), configurationEvent.getFactoryPid());
				Dictionary<?, ?> dict = config.getProperties();

				String fileName = (String) dict.get(DirectoryProcessor.FILENAME);
				File file = fileName != null ? fromConfigKey(fileName) : null;

				if (file != null && file.isFile()) {
					if (fileName.endsWith(".cfg")) {
						org.apache.felix.utils.properties.Properties props = new org.apache.felix.utils.properties.Properties(file, bundleContext);
						for (Enumeration e = dict.keys(); e.hasMoreElements();) {
							String key = e.nextElement().toString();
							if (!Constants.SERVICE_PID.equals(key) && !ConfigurationAdmin.SERVICE_FACTORYPID.equals(key) && !DirectoryProcessor.FILENAME.equals(key)) {
								String val = dict.get(key).toString();
								props.put(key, val);
							}
						}
						props.save();

					} else if (fileName.endsWith(".config")) {
						OutputStream fos = new FileOutputStream(file);
						Properties props = new Properties();
						for (Enumeration e = dict.keys(); e.hasMoreElements();) {
							String key = e.nextElement().toString();
							if (!Constants.SERVICE_PID.equals(key) && !ConfigurationAdmin.SERVICE_FACTORYPID.equals(key) && !DirectoryProcessor.FILENAME.equals(key)) {
								props.put(key, dict.get(key));
							}
						}
						try {
							ConfigurationHandler.write(fos, props);
						} finally {
							fos.close();
						}
					}

					// we're just writing out what's already loaded into ConfigAdmin, so
					// update file checksum since lastModified gets updated when writing
					fileInstall.updateChecksum(file);
				}

			} catch (Exception e) {
				Util.log(bundleContext, Logger.LOG_INFO, "Unable to save configuration", e);
			}
		}
	}

	protected boolean shouldSaveConfig() {
		String str = this.bundleContext.getProperty(DirectoryProcessor.ENABLE_CONFIG_SAVE);
		if (str == null) {
			str = this.bundleContext.getProperty(DirectoryProcessor.DISABLE_CONFIG_SAVE);
		}
		if (str != null) {
			return Boolean.valueOf(str);
		}
		return true;
	}

	/**
	 * Set the configuration based on the config file.
	 *
	 * @param file
	 *            Configuration file
	 * @return <code>true</code> if the configuration has been updated
	 * @throws Exception
	 */
	protected boolean setConfig(File file) throws Exception {
		Hashtable<String, Object> newProps = new Hashtable<String, Object>();

		InputStream in = new BufferedInputStream(new FileInputStream(file));
		try {
			if (file.getName().endsWith(".cfg")) {
				Properties p = new Properties();
				in.mark(1);

				boolean isXml = in.read() == '<';

				in.reset();

				if (isXml) {
					p.loadFromXML(in);
				} else {
					p.load(in);
				}

				Map<String, String> strMap = new HashMap<String, String>();
				for (Object k : p.keySet()) {
					strMap.put(k.toString(), p.getProperty(k.toString()));
				}

				InterpolationHelper.performSubstitution(strMap, bundleContext);
				newProps.putAll(strMap);

			} else if (file.getName().endsWith(".config")) {
				Dictionary<?, ?> config = ConfigurationHandler.read(in);
				for (Enumeration<?> keysItor = config.keys(); keysItor.hasMoreElements();) {
					Object key = keysItor.nextElement();
					newProps.put(key.toString(), config.get(key));
				}
			}
		} finally {
			in.close();
		}

		String pid[] = parsePid(file.getName());

		Printer.pl("ConfigInstaller.setConfig()");
		Printer.pl("\t file = " + file.getAbsolutePath());
		Printer.pl("\t newProps = ");
		Printer.pl(newProps);

		String fileName = toConfigKey(file);

		String servicePid = pid[0];
		String factoryPid = pid[1];
		Configuration config = getConfiguration(fileName, servicePid, factoryPid);

		Dictionary<String, Object> props = config.getProperties();
		Printer.pl("\t props = ");
		Printer.pl(props);

		Hashtable<String, Object> originalProps = props != null ? new Hashtable<String, Object>(new DictionaryAsMap<String, Object>(props)) : null;
		if (originalProps != null) {
			originalProps.remove(DirectoryProcessor.FILENAME);
			originalProps.remove(Constants.SERVICE_PID);
			originalProps.remove(ConfigurationAdmin.SERVICE_FACTORYPID);
		}

		if (!newProps.equals(originalProps)) {
			newProps.put(DirectoryProcessor.FILENAME, fileName);

			if (originalProps == null) {
				Util.log(bundleContext, Logger.LOG_INFO, "Creating configuration from " + pid[0] + (pid[1] == null ? "" : "-" + pid[1]) + ".cfg", null);
			} else {
				Util.log(bundleContext, Logger.LOG_INFO, "Updating configuration from " + pid[0] + (pid[1] == null ? "" : "-" + pid[1]) + ".cfg", null);
			}
			config.update(newProps);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Remove the configuration.
	 *
	 * @param file
	 *            File where the configuration in was defined.
	 * @return <code>true</code>
	 * @throws Exception
	 */
	protected boolean deleteConfig(File file) throws Exception {
		String pid[] = parsePid(file.getName());
		Util.log(bundleContext, Logger.LOG_INFO, "Deleting configuration from " + pid[0] + (pid[1] == null ? "" : "-" + pid[1]) + ".cfg", null);

		Configuration config = getConfiguration(toConfigKey(file), pid[0], pid[1]);
		config.delete();

		return true;
	}

	protected String toConfigKey(File file) {
		return file.getAbsoluteFile().toURI().toString();
	}

	protected File fromConfigKey(String key) {
		return new File(URI.create(key));
	}

	protected String[] parsePid(String path) {
		String pid = path.substring(0, path.lastIndexOf('.'));
		int n = pid.indexOf('-');

		if (n > 0) {
			String factoryPid = pid.substring(n + 1);
			pid = pid.substring(0, n);
			return new String[] { pid, factoryPid };
		} else {
			return new String[] { pid, null };
		}
	}

	/**
	 * 
	 * @param fileName
	 * @param pid
	 * @param factoryPid
	 * @return
	 * @throws Exception
	 */
	protected Configuration getConfiguration(String fileName, String pid, String factoryPid) throws Exception {
		Configuration existingConfiguration = null;
		String filter = "(" + DirectoryProcessor.FILENAME + "=" + escapeFilterValue(fileName) + ")";
		Configuration[] configurations = configAdmin.listConfigurations(filter);
		if (configurations != null && configurations.length > 0) {
			existingConfiguration = configurations[0];
		}

		if (existingConfiguration != null) {
			return existingConfiguration;
		} else {
			Configuration newConfiguration;
			if (factoryPid != null) {
				newConfiguration = configAdmin.createFactoryConfiguration(pid, null);
			} else {
				newConfiguration = configAdmin.getConfiguration(pid, null);
			}
			return newConfiguration;
		}
	}

	protected String escapeFilterValue(String s) {
		return s.replaceAll("[(]", "\\\\(").replaceAll("[)]", "\\\\)").replaceAll("[=]", "\\\\=").replaceAll("[\\*]", "\\\\*");
	}

}
