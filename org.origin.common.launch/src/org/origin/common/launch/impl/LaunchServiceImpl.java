package org.origin.common.launch.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.origin.common.launch.LaunchActivator;
import org.origin.common.launch.LaunchConfiguration;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchHandler;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.LaunchType;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.ProcessHandler;
import org.origin.common.launch.util.LaunchExtensionHelper;
import org.origin.common.launch.util.Platform;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaunchServiceImpl implements LaunchInternalService {

	private static Object lock = new Object[0];
	private static LaunchServiceImpl instance = null;

	public static LaunchServiceImpl getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new LaunchServiceImpl();
				}
			}
		}
		return instance;
	}

	protected static Logger LOG = LoggerFactory.getLogger(LaunchServiceImpl.class);

	protected BundleContext context;
	protected ServiceRegistration<?> serviceRegistration;
	protected Map<Object, Object> properties;

	protected Path launchConfigDir;
	protected List<LaunchConfiguration> launchConfigs;
	protected List<LaunchHandler> launchHandlers = new ArrayList<LaunchHandler>();

	public LaunchServiceImpl() {
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		this.context = bundleContext;

		// load properties
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, LaunchConstants.PLATFORM_HOME);
		this.properties = properties;

		String homeLocation = getProperty(LaunchConstants.PLATFORM_HOME);
		String bundleName = LaunchActivator.getDefault().getBundleName();

		LOG.info(LaunchConstants.PLATFORM_HOME + " = " + homeLocation);
		LOG.info("bundleName = " + bundleName);

		if (homeLocation != null) {
			// Activator.getDefault().get
			String launchConfigDirLocation = homeLocation + "/.metadata/" + bundleName + "/launchconfigs";
			LOG.info("launchConfigDirLocation = " + launchConfigDirLocation);

			Path launchConfigDir = FileSystems.getDefault().getPath(launchConfigDirLocation);
			if (!Files.exists(launchConfigDir)) {
				try {
					Files.createDirectories(launchConfigDir);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			setLaunchConfigDir(launchConfigDir);
		}

		// Register ILaunchService.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(LaunchService.class, this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		// Unregister ILaunchService.
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		this.context = null;
	}

	protected String getProperty(String key) {
		return PropertyUtil.getProperty(this.properties, key, String.class);
	}

	public Path getLaunchConfigDir() {
		return this.launchConfigDir;
	}

	public void setLaunchConfigDir(Path launchConfigDir) {
		this.launchConfigDir = launchConfigDir;
	}

	protected List<LaunchConfiguration> loadLaunchConfigs() {
		List<LaunchConfiguration> launchConfigs = new ArrayList<LaunchConfiguration>();

		DirectoryStream.Filter<Path> filter = new Filter<Path>() {
			@Override
			public boolean accept(Path entry) throws IOException {
				String fileName = entry.getFileName().toString();
				return fileName != null && fileName.endsWith(LaunchConstants.DOT_LAUNCH_EXTENSION);
			}
		};

		Path directoryPath = getLaunchConfigDir();
		if (Files.isDirectory(directoryPath)) {
			try (DirectoryStream<Path> paths = Files.newDirectoryStream(directoryPath, filter)) {
				for (Path path : paths) {
					LaunchConfiguration launchConfig = new LaunchConfigurationImpl(this, path);
					launchConfigs.add(launchConfig);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return launchConfigs;
	}

	@Override
	public synchronized LaunchType[] getLaunchTypes() {
		return LaunchExtensionHelper.INSTANCE.getLaunchTypes();
	}

	@Override
	public synchronized LaunchType getLaunchType(String typeId) {
		return LaunchExtensionHelper.INSTANCE.getLaunchType(typeId);
	}

	@Override
	public Launcher[] getLaunchers() {
		return LaunchExtensionHelper.INSTANCE.getLaunchers();
	}

	@Override
	public Launcher[] getLaunchers(String typeId) {
		return LaunchExtensionHelper.INSTANCE.getLaunchers(typeId);
	}

	protected synchronized List<LaunchConfiguration> getLaunchConfigs() {
		if (this.launchConfigs == null) {
			this.launchConfigs = loadLaunchConfigs();
		}
		return this.launchConfigs;
	}

	@Override
	public synchronized LaunchConfiguration[] getLaunchConfigurations() throws IOException {
		if (this.launchConfigs == null) {
			this.launchConfigs = loadLaunchConfigs();
		}
		return this.launchConfigs.toArray(new LaunchConfiguration[this.launchConfigs.size()]);
	}

	@Override
	public synchronized LaunchConfiguration[] getLaunchConfigurations(String typeId) throws IOException {
		List<LaunchConfiguration> configs = new ArrayList<LaunchConfiguration>();
		for (LaunchConfiguration config : getLaunchConfigurations()) {
			if (typeId.equals(config.getTypeId())) {
				configs.add(config);
			}
		}
		return configs.toArray(new LaunchConfiguration[configs.size()]);
	}

	@Override
	public synchronized LaunchConfiguration getLaunchConfiguration(String typeId, String name) throws IOException {
		if (typeId == null || typeId.isEmpty()) {
			throw new IOException("typeId is null");
		}
		if (name == null || name.isEmpty()) {
			throw new IOException("name is null");
		}
		LaunchConfiguration config = null;
		if (this.launchConfigs == null) {
			this.launchConfigs = loadLaunchConfigs();
		}
		for (LaunchConfiguration currConfig : getLaunchConfigurations()) {
			if (typeId.equals(currConfig.getTypeId()) && name.equals(currConfig.getName())) {
				config = currConfig;
				break;
			}
		}
		return config;
	}

	@Override
	public LaunchConfiguration createLaunchConfiguration(String typeId, String name) throws IOException {
		if (typeId == null || typeId.isEmpty()) {
			throw new IOException("typeId is null");
		}
		if (name == null || name.isEmpty()) {
			throw new IOException("name is null");
		}
		if (launchConfigurationExists(name)) {
			throw new IOException("name already exits");
		}

		Path configFilePath = getLaunchConfigDir().resolve(name + LaunchConstants.DOT_LAUNCH_EXTENSION);
		LaunchConfigurationImpl launchConfig = new LaunchConfigurationImpl(this, typeId, configFilePath);
		launchConfig.save();

		launchConfigurationAdded(launchConfig);

		return launchConfig;
	}

	@Override
	public boolean launchConfigurationExists(String name) {
		boolean exists = false;
		List<LaunchConfiguration> launchConfigs = getLaunchConfigs();
		for (LaunchConfiguration currLaunchConfig : launchConfigs) {
			String currName = currLaunchConfig.getName();
			if (name != null && name.equals(currName)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	@Override
	public LaunchHandler[] getLaunchHandlers() {
		synchronized (this.launchHandlers) {
			return this.launchHandlers.toArray(new LaunchHandler[this.launchHandlers.size()]);
		}
	}

	@Override
	public LaunchHandler getLaunchHandler(String runtimeLaunchId) {
		LaunchHandler runtimeLaunche = null;
		if (runtimeLaunchId != null) {
			for (LaunchHandler currRuntimeLaunche : this.launchHandlers) {
				String currRuntimeLaunchId = currRuntimeLaunche.getAttribute(LaunchConstants.RUNTIME_LAUNCH_ID);
				if (runtimeLaunchId.equals(currRuntimeLaunchId)) {
					runtimeLaunche = currRuntimeLaunche;
					break;
				}
			}
		}
		return runtimeLaunche;
	}

	@Override
	public ProcessHandler[] getProcessHandlers() {
		List<ProcessHandler> allProcesses = new ArrayList<ProcessHandler>();
		synchronized (this.launchHandlers) {
			for (LaunchHandler launch : this.launchHandlers) {
				ProcessHandler[] processes = launch.getProcesses();
				if (processes != null) {
					for (int i = 0; i < processes.length; i++) {
						allProcesses.add(processes[i]);
					}
				}
			}
		}
		return allProcesses.toArray(new ProcessHandler[allProcesses.size()]);
	}

	@Override
	public synchronized boolean launchConfigurationAdded(LaunchConfiguration launchConfig) throws IOException {
		boolean succeed = false;
		List<LaunchConfiguration> launchConfigs = getLaunchConfigs();
		if (launchConfig != null) {
			if (launchConfigs.contains(launchConfig) || launchConfigurationExists(launchConfig.getName())) {
				throw new IOException("Launch configuration already exists.");
			}
			succeed = launchConfigs.add(launchConfig);
		}
		return succeed;
	}

	@Override
	public synchronized boolean launchConfigurationRemoved(LaunchConfiguration launchConfig) {
		boolean succeed = false;
		List<LaunchConfiguration> launchConfigs = getLaunchConfigs();
		if (launchConfig != null) {
			if (launchConfigs.contains(launchConfig)) {
				succeed = launchConfigs.remove(launchConfig);
			}
		}
		return succeed;
	}

	@Override
	public synchronized boolean launchAdded(LaunchHandler launchHandler) {
		boolean succeed = false;
		synchronized (this.launchHandlers) {
			if (launchHandler != null && !this.launchHandlers.contains(launchHandler)) {
				// Assign unique runtime launch id
				String runtimeLaunchId = getRuntimeLaunchId(launchHandler);
				launchHandler.setAttribute(LaunchConstants.RUNTIME_LAUNCH_ID, runtimeLaunchId);
				succeed = this.launchHandlers.add(launchHandler);
			}
		}
		return succeed;
	}

	/**
	 * 
	 * @param launchHandler
	 * @return
	 */
	protected String getRuntimeLaunchId(LaunchHandler launchHandler) {
		String name = launchHandler.getLaunchConfiguration().getName();
		int index = 1;
		while (true) {
			String id = name + "_" + index;
			boolean exists = false;
			for (LaunchHandler currRuntimeLaunch : this.launchHandlers) {
				String currId = currRuntimeLaunch.getAttribute(LaunchConstants.RUNTIME_LAUNCH_ID);
				if (id.equals(currId)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				return id;
			}
			index++;
		}
	}

	@Override
	public synchronized boolean launchRemoved(LaunchHandler launchHandler) {
		boolean succeed = false;
		synchronized (this.launchHandlers) {
			if (launchHandler != null && this.launchHandlers.contains(launchHandler)) {
				succeed = this.launchHandlers.remove(launchHandler);
			}
		}
		return succeed;
	}

	public static final String ATTR_ENVIRONMENT_VARIABLES = LaunchActivator.getDefault().getUniqueIdentifier() + ".environmentVariables"; //$NON-NLS-1$
	public static final String ATTR_APPEND_ENVIRONMENT_VARIABLES = LaunchActivator.getDefault().getUniqueIdentifier() + ".appendEnvironmentVariables"; //$NON-NLS-1$

	@Override
	public String[] getEnvironment(LaunchConfiguration configuration) throws IOException {
		Map<String, String> configEnv = configuration.getAttribute(ATTR_ENVIRONMENT_VARIABLES, (Map<String, String>) null);
		if (configEnv == null) {
			return null;
		}
		Map<String, String> env = new HashMap<String, String>();
		// build base environment
		boolean append = configuration.getAttribute(ATTR_APPEND_ENVIRONMENT_VARIABLES, true);
		if (append) {
			env.putAll(getNativeEnvironmentCasePreserved());
		}

		// Add variables from config
		boolean win32 = Platform.getOS().equals(LaunchConstants.OS_WIN32);
		String key = null;
		String value = null;
		Object nativeValue = null;
		String nativeKey = null;
		for (Entry<String, String> entry : configEnv.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			// translate any string substitution variables
			if (value != null) {
				// value = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(value);
			}
			boolean added = false;
			if (win32) {
				// First, check if the key is an exact match for an existing key.
				nativeValue = env.get(key);
				if (nativeValue != null) {
					// If an exact match is found, just replace the value
					env.put(key, value);
				} else {
					// Win32 variables are case-insensitive. If an exact match isn't found, iterate to
					// check for a case-insensitive match. We maintain the key's case (see bug 86725),
					// but do a case-insensitive comparison (for example, "pAtH" will still override "PATH").
					for (Entry<String, String> nativeEntry : env.entrySet()) {
						nativeKey = (nativeEntry).getKey();
						if (nativeKey.equalsIgnoreCase(key)) {
							nativeEntry.setValue(value);
							added = true;
							break;
						}
					}
				}
			}
			if (!added) {
				env.put(key, value);
			}
		}
		List<String> strings = new ArrayList<String>(env.size());
		StringBuffer buffer = null;
		for (Entry<String, String> entry : env.entrySet()) {
			buffer = new StringBuffer(entry.getKey());
			buffer.append('=').append(entry.getValue());
			strings.add(buffer.toString());
		}
		return strings.toArray(new String[strings.size()]);
	}

	private static HashMap<String, String> fgNativeEnvCasePreserved = null;

	public synchronized Map<String, String> getNativeEnvironmentCasePreserved() {
		if (fgNativeEnvCasePreserved == null) {
			fgNativeEnvCasePreserved = new HashMap<String, String>();
			cacheNativeEnvironment(fgNativeEnvCasePreserved);
		}
		return new HashMap<String, String>(fgNativeEnvCasePreserved);
	}

	private void cacheNativeEnvironment(Map<String, String> cache) {
		try {
			String nativeCommand = null;
			boolean isWin9xME = false; // see bug 50567
			String fileName = null;
			if (Platform.getOS().equals(LaunchConstants.OS_WIN32)) {
				String osName = System.getProperty("os.name"); //$NON-NLS-1$
				isWin9xME = osName != null && (osName.startsWith("Windows 9") || osName.startsWith("Windows ME")); //$NON-NLS-1$ //$NON-NLS-2$
				if (isWin9xME) {
					// Win 95, 98, and ME
					// SET might not return therefore we pipe into a file
					// IPath stateLocation = DebugPlugin.getDefault().getStateLocation();
					// fileName = stateLocation.toOSString() + File.separator + "env.txt"; //$NON-NLS-1$
					// nativeCommand = "command.com /C set > " + fileName; //$NON-NLS-1$
				} else {
					// Win NT, 2K, XP
					nativeCommand = "cmd.exe /C set"; //$NON-NLS-1$
				}
			} else if (!Platform.getOS().equals(LaunchConstants.OS_UNKNOWN)) {
				nativeCommand = "env"; //$NON-NLS-1$
			}
			if (nativeCommand == null) {
				return;
			}
			Process process = Runtime.getRuntime().exec(nativeCommand);
			if (isWin9xME) {
				// read piped data on Win 95, 98, and ME
				Properties p = new Properties();
				File file = new File(fileName);
				InputStream stream = new BufferedInputStream(new FileInputStream(file));
				p.load(stream);
				stream.close();
				if (!file.delete()) {
					file.deleteOnExit(); // if delete() fails try again on VM close
				}
				for (Entry<Object, Object> entry : p.entrySet()) {
					// Win32's environment variables are case insensitive. Put everything
					// to uppercase so that (for example) the "PATH" variable will match
					// "pAtH" correctly on Windows.
					String key = (String) entry.getKey();
					// no need to cast value
					cache.put(key, (String) p.get(key));
				}
			} else {
				// read process directly on other platforms
				// we need to parse out matching '{' and '}' for function declarations in .bash environments
				// pattern is [func name]=() { and we must find the '}' on its own line with no trailing ';'
				InputStream stream = process.getInputStream();
				InputStreamReader isreader = new InputStreamReader(stream);
				BufferedReader reader = new BufferedReader(isreader);
				String line = reader.readLine();
				String key = null;
				String value = null;
				String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
				while (line != null) {
					int func = line.indexOf("=()"); //$NON-NLS-1$
					if (func > 0) {
						key = line.substring(0, func);
						// scan until we find the closing '}' with no following chars
						value = line.substring(func + 1);
						while (line != null && !line.equals("}")) { //$NON-NLS-1$
							line = reader.readLine();
							if (line != null) {
								value += newLine + line;
							}
						}
						line = reader.readLine();
					} else {
						int separator = line.indexOf('=');
						if (separator > 0) {
							key = line.substring(0, separator);
							value = line.substring(separator + 1);
							line = reader.readLine();
							if (line != null) {
								// this line has a '=' read ahead to check next line for '=', might be broken on more than one line
								// also if line starts with non-identifier - it is remainder of previous variable
								while (line.indexOf('=') < 0 || (line.length() > 0 && !Character.isJavaIdentifierStart(line.charAt(0)))) {
									value += newLine + line;
									line = reader.readLine();
									if (line == null) {
										// if next line read is the end of the file quit the loop
										break;
									}
								}
							}
						}
					}
					if (key != null) {
						cache.put(key, value);
						key = null;
						value = null;
					} else {
						line = reader.readLine();
					}
				}
				reader.close();
			}
		} catch (IOException e) {
			// Native environment-fetching code failed.
			// This can easily happen and is not useful to log.
		}
	}

}

// @Override
// public void addLaunchListener(ILaunchListener listener) {
//
// }

// @Override
// public void removeLaunchListener(ILaunchListener listener) {
//
// }

// final File dir = getLaunchConfigDir().toFile();
// if (dir.isDirectory()) {
// FilenameFilter filter = new FilenameFilter() {
// @Override
// public boolean accept(File dir, String name) {
// return dir.equals(dir) && name.endsWith(LaunchConstants.DOT_LAUNCH_EXTENSION);
// }
// };
// File[] launchFiles = dir.listFiles(filter);
// for (File launchFile : launchFiles) {
// LaunchConfiguration launchConfig = new LaunchConfigurationImpl(this, launchFile);
// launchConfigs.add(launchConfig);
// }
// }
