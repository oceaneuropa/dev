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
 * ArtifactInstaller for configurations.
 * 
 * TODO: This service lifecycle should be bound to the ConfigurationAdmin service lifecycle.
 * 
 * @see http://felix.apache.org/documentation/subprojects/apache-felix-config-admin.html
 * @see http://blog.osgi.org/2010/06/how-to-use-config-admin.html
 */
public class ConfigInstaller implements ArtifactInstaller, ConfigurationListener {

	protected BundleContext context;
	protected ConfigurationAdmin configAdmin;
	protected FileInstall fileInstall;
	protected ServiceRegistration registration;

	/**
	 * 
	 * @param context
	 * @param configAdmin
	 * @param fileInstall
	 */
	public ConfigInstaller(BundleContext context, ConfigurationAdmin configAdmin, FileInstall fileInstall) {
		this.context = context;
		this.configAdmin = configAdmin;
		this.fileInstall = fileInstall;
		init();
	}

	public void init() {
		println("ConfigInstaller.init()");
		registration = this.context.registerService(new String[] { ConfigurationListener.class.getName(), ArtifactListener.class.getName(), ArtifactInstaller.class.getName() }, this, null);
	}

	public void destroy() {
		println("ConfigInstaller.destroy()");
		registration.unregister();
	}

	/** ArtifactListener */
	@Override
	public boolean canHandle(File artifact) {
		println("ConfigInstaller.canHandle()");
		println("\tartifact = " + artifact.getAbsolutePath());

		return artifact.getName().endsWith(".cfg") || artifact.getName().endsWith(".config");
	}

	/** ArtifactInstaller */
	@Override
	public void install(File artifact) throws Exception {
		println("ConfigInstaller.install()");
		println("\tartifact = " + artifact.getAbsolutePath());

		setConfig(artifact);
	}

	@Override
	public void update(File artifact) throws Exception {
		println("ConfigInstaller.update()");
		println("\tartifact = " + artifact.getAbsolutePath());

		setConfig(artifact);
	}

	@Override
	public void uninstall(File artifact) throws Exception {
		println("ConfigInstaller.uninstall()");
		println("\tartifact = " + artifact.getAbsolutePath());

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
				Configuration config = getConfigurationAdmin().getConfiguration(configurationEvent.getPid(), configurationEvent.getFactoryPid());
				Dictionary dict = config.getProperties();

				String fileName = (String) dict.get(DirectoryWatcher.FILENAME);
				File file = fileName != null ? fromConfigKey(fileName) : null;

				if (file != null && file.isFile()) {
					if (fileName.endsWith(".cfg")) {
						org.apache.felix.utils.properties.Properties props = new org.apache.felix.utils.properties.Properties(file, context);
						for (Enumeration e = dict.keys(); e.hasMoreElements();) {
							String key = e.nextElement().toString();
							if (!Constants.SERVICE_PID.equals(key) && !ConfigurationAdmin.SERVICE_FACTORYPID.equals(key) && !DirectoryWatcher.FILENAME.equals(key)) {
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
							if (!Constants.SERVICE_PID.equals(key) && !ConfigurationAdmin.SERVICE_FACTORYPID.equals(key) && !DirectoryWatcher.FILENAME.equals(key)) {
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
				Util.log(context, Logger.LOG_INFO, "Unable to save configuration", e);
			}
		}
	}

	protected boolean shouldSaveConfig() {
		String str = this.context.getProperty(DirectoryWatcher.ENABLE_CONFIG_SAVE);
		if (str == null) {
			str = this.context.getProperty(DirectoryWatcher.DISABLE_CONFIG_SAVE);
		}
		if (str != null) {
			return Boolean.valueOf(str);
		}
		return true;
	}

	protected ConfigurationAdmin getConfigurationAdmin() {
		return configAdmin;
	}

	/**
	 * Set the configuration based on the config file.
	 *
	 * @param file
	 *            Configuration file
	 * @return <code>true</code> if the configuration has been updated
	 * @throws Exception
	 */
	protected boolean setConfig(final File file) throws Exception {
		Hashtable<String, Object> map = new Hashtable<String, Object>();
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

				InterpolationHelper.performSubstitution(strMap, context);
				map.putAll(strMap);

			} else if (file.getName().endsWith(".config")) {
				final Dictionary config = ConfigurationHandler.read(in);
				final Enumeration i = config.keys();
				while (i.hasMoreElements()) {
					Object key = i.nextElement();
					map.put(key.toString(), config.get(key));
				}
			}
		} finally {
			in.close();
		}

		String pid[] = parsePid(file.getName());

		Configuration config = getConfiguration(toConfigKey(file), pid[0], pid[1]);

		Dictionary<String, Object> props = config.getProperties();
		Hashtable<String, Object> old = props != null ? new Hashtable<String, Object>(new DictionaryAsMap<String, Object>(props)) : null;
		if (old != null) {
			old.remove(DirectoryWatcher.FILENAME);
			old.remove(Constants.SERVICE_PID);
			old.remove(ConfigurationAdmin.SERVICE_FACTORYPID);
		}

		if (!map.equals(old)) {
			map.put(DirectoryWatcher.FILENAME, toConfigKey(file));
			if (old == null) {
				Util.log(context, Logger.LOG_INFO, "Creating configuration from " + pid[0] + (pid[1] == null ? "" : "-" + pid[1]) + ".cfg", null);
			} else {
				Util.log(context, Logger.LOG_INFO, "Updating configuration from " + pid[0] + (pid[1] == null ? "" : "-" + pid[1]) + ".cfg", null);
			}
			config.update(map);

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
		Util.log(context, Logger.LOG_INFO, "Deleting configuration from " + pid[0] + (pid[1] == null ? "" : "-" + pid[1]) + ".cfg", null);

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

	protected Configuration getConfiguration(String fileName, String pid, String factoryPid) throws Exception {
		Configuration oldConfiguration = findExistingConfiguration(fileName);
		if (oldConfiguration != null) {
			return oldConfiguration;
		} else {
			Configuration newConfiguration;
			if (factoryPid != null) {
				newConfiguration = getConfigurationAdmin().createFactoryConfiguration(pid, null);
			} else {
				newConfiguration = getConfigurationAdmin().getConfiguration(pid, null);
			}
			return newConfiguration;
		}
	}

	protected Configuration findExistingConfiguration(String fileName) throws Exception {
		String filter = "(" + DirectoryWatcher.FILENAME + "=" + escapeFilterValue(fileName) + ")";
		Configuration[] configurations = getConfigurationAdmin().listConfigurations(filter);
		if (configurations != null && configurations.length > 0) {
			return configurations[0];
		} else {
			return null;
		}
	}

	protected String escapeFilterValue(String s) {
		return s.replaceAll("[(]", "\\\\(").replaceAll("[)]", "\\\\)").replaceAll("[=]", "\\\\=").replaceAll("[\\*]", "\\\\*");
	}

	protected void println(String message) {
		System.out.println(message);
	}

}
