package org.orbit.component.runtime.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.IPlatform;
import org.origin.common.launch.LaunchActivator;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.LaunchType;
import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.node.INode;

public class LaunchServiceHelper {

	public static LaunchServiceHelper INSTANCE = new LaunchServiceHelper();

	/**
	 * Create {node_path}/bin/start_node.sh file.
	 * 
	 * @param node
	 * @return
	 * @throws IOException
	 */
	public boolean generateStartNodeScript(INode node) throws IOException {
		if (node == null) {
			return false;
		}

		// Example start_node.sh
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// BIN_DIR="$(cd "$(dirname "$0")"; pwd -P)"
		// NODE_DIR="$(dirname "$BIN_DIR")"
		// NODESPACES_DIR="$(dirname "$NODE_DIR")"
		//
		// ROOT_DIR="$(dirname "$NODESPACES_DIR")"
		//
		// OSGI_JAR="org.eclipse.osgi_3.10.101.v20150820-1432.jar"
		// java -jar ${ROOT_DIR}/plugins/${OSGI_JAR} -configuration ${NODE_DIR}/configuration -console
		// -----------------------------------------------------------------------------------------------------------------------------------------

		StringBuilder content = new StringBuilder();
		content.append("BIN_DIR=\"$(cd \"$(dirname \"$0\")\"; pwd -P)\"").append("\n");
		content.append("NODE_DIR=\"$(dirname \"$BIN_DIR\")\"").append("\n");
		content.append("NODESPACES_DIR=\"$(dirname \"$NODE_DIR\")\"").append("\n");
		content.append("ROOT_DIR=\"$(dirname \"$NODESPACES_DIR\")\"").append("\n");
		content.append("OSGI_JAR=\"org.eclipse.osgi_3.10.101.v20150820-1432.jar\"").append("\n");
		content.append("java -jar ${ROOT_DIR}/plugins/${OSGI_JAR} -configuration ${NODE_DIR}/configuration -console").append("\n");

		byte[] bytes = content.toString().getBytes();

		IFolder binFolder = node.getFolder("bin");
		if (!binFolder.exists()) {
			binFolder.create();
		}

		IFile scriptFile = binFolder.getFile("start_node.sh");
		if (!scriptFile.exists()) {
			scriptFile.create(bytes);
		} else {
			scriptFile.setContents(bytes);
		}

		try {
			Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
			perms.add(PosixFilePermission.OWNER_EXECUTE);
			perms.add(PosixFilePermission.OWNER_READ);
			perms.add(PosixFilePermission.OWNER_WRITE);
			perms.add(PosixFilePermission.GROUP_EXECUTE);
			perms.add(PosixFilePermission.GROUP_READ);
			perms.add(PosixFilePermission.OTHERS_READ);
			scriptFile.setFilePermissions(perms);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Create {node_path}/configuration/config.ini file.
	 * 
	 * @param node
	 * @return
	 * @throws IOException
	 */
	public boolean generateConfigIni(INode node, Map<Object, Object> properties) throws IOException {
		if (node == null) {
			return false;
		}

		// Example config.ini
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// eclipse.ignoreApp=true
		// osgi.noShutdown=true
		// osgi.bundles=reference\:file\:eclipse.equinox/org.eclipse.equinox.simpleconfigurator_1.1.0.v20131217-1203.jar@1\:start
		// org.eclipse.equinox.simpleconfigurator.configUrl=file\:../../../configurations/node/org.eclipse.equinox.simpleconfigurator/bundles.info
		//
		// org.osgi.service.http.port=9003
		// logback.configurationFile=file\:/Users/username/origin/logback/orbit0.xml
		// sun.net.http.allowRestrictedHeaders=true
		//
		// orbit.index_service.url=http://127.0.0.1:8000/orbit/v1/indexservice
		// orbit.host.url=http://127.0.0.1:9003
		//
		// platform.parent.id=p101
		// platform.id=node3
		// platform.type=node
		// platform.name=node3
		// platform.version=1.0.0
		// platform.context_root=/orbit/v1/platform
		// platform.home=/Users/username/origin/ta1/nodespace/node3
		// -----------------------------------------------------------------------------------------------------------------------------------------

		// Example Node attributes
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// eclipse.ignoreApp=true
		// osgi.noShutdown=true
		// osgi.bundles=reference:file:eclipse.equinox/org.eclipse.equinox.simpleconfigurator_1.1.0.v20131217-1203.jar@1:start
		// org.eclipse.equinox.simpleconfigurator.configUrl=file:../../../configurations/node/org.eclipse.equinox.simpleconfigurator/bundles.info
		// org.osgi.service.http.port=9003
		//
		// logback.configurationFile=/Users/username/origin/logback/orbit0.xml
		//
		// orbit.index_service.url=http://127.0.0.1:8000/orbit/v1/indexservice
		// orbit.host.url=http://127.0.0.1:9003
		//
		// platform.parent.id=p101
		// platform.id=node3
		// platform.type=node
		// platform.name=node3
		// platform.version=1.0.0
		// platform.context_root=/orbit/v1/platform
		// platform.home=/Users/username/origin/ta1/nodespace/node3
		// -----------------------------------------------------------------------------------------------------------------------------------------

		// Example Node attributes (for node2)
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// org.osgi.service.http.port=9002
		// orbit.index_service.url=http://127.0.0.1:8000/orbit/v1/indexservice
		// orbit.host.url=http://127.0.0.1:9002
		// -----------------------------------------------------------------------------------------------------------------------------------------

		// Example Node attributes (for node3)
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// org.osgi.service.http.port=9003
		// orbit.index_service.url=http://127.0.0.1:8000/orbit/v1/indexservice
		// orbit.host.url=http://127.0.0.1:9003
		// -----------------------------------------------------------------------------------------------------------------------------------------

		// Example Node attributes (for node4)
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// org.osgi.service.http.port=9004
		// orbit.host.url=http://127.0.0.1:9004
		// -----------------------------------------------------------------------------------------------------------------------------------------

		// Example Node attributes (for node5)
		// -----------------------------------------------------------------------------------------------------------------------------------------
		// org.osgi.service.http.port=9005
		// orbit.host.url=http://127.0.0.1:9005
		// -----------------------------------------------------------------------------------------------------------------------------------------
		IPlatform platform = PlatformSDKActivator.getInstance().getPlatform();
		String platformId = platform.getId();
		String platformHome = platform.getHome();

		Map<String, String> allConfigs = new TreeMap<String, String>();

		if (properties != null) {
			for (Iterator<Object> itor = properties.keySet().iterator(); itor.hasNext();) {
				Object propName = itor.next();
				Object propValue = properties.get(propName);
				if (propName instanceof String && propValue != null) {
					// configuration for the components of the server platform should not be used by the node
					if (((String) propName).startsWith("component.")) {
						continue;
					}
					// Need to validate required properties in config.ini attribute. If not set, node should not be started.
					if ("org.osgi.service.http.port".equals((String) propName) //
							|| "orbit.host.url".equals((String) propName)) //
					{
						continue;
					}
					allConfigs.put((String) propName, propValue.toString());
				}
			}
		}

		allConfigs.put("eclipse.ignoreApp", "true");
		allConfigs.put("osgi.noShutdown", "true");
		allConfigs.put("osgi.bundles", "reference\\:file\\:eclipse.equinox/org.eclipse.equinox.simpleconfigurator_1.1.0.v20131217-1203.jar@1\\:start");
		allConfigs.put("org.eclipse.equinox.simpleconfigurator.configUrl", "file\\:../../../configurations/node/org.eclipse.equinox.simpleconfigurator/bundles.info");
		allConfigs.put("logback.configurationFile", "file\\:" + platformHome + "/log/logback/orbit0.xml");

		allConfigs.put("platform.parent.id", platformId);
		allConfigs.put("platform.id", node.getDescription().getId());
		allConfigs.put("platform.type", "node");
		allConfigs.put("platform.name", node.getName());
		allConfigs.put("platform.version", "1.0.0");
		allConfigs.put("platform.context_root", "/orbit/v1/platform");

		Object underlyingResource = node.getWorkspace().getUnderlyingResource(node.getFullPath());
		if (underlyingResource instanceof File) {
			File fsFile = (File) underlyingResource;
			allConfigs.put("platform.home", fsFile.getAbsolutePath());
		}

		String attributes = node.getDescription().getStringAttribute("config.ini");
		if (attributes != null) {
			String[] segments = attributes.split("\r\n");
			if (segments != null) {
				for (String segment : segments) {
					int index = segment.indexOf("=");
					if (index > 0) {
						String propName = segment.substring(0, index).trim();
						String propValue = "";
						if (index < segment.length() - 1) {
							propValue = segment.substring(index + 1).trim();
						}
						allConfigs.put(propName, propValue);
					}
				}
			}
		}

		StringBuilder content = new StringBuilder();
		for (Iterator<String> itor = allConfigs.keySet().iterator(); itor.hasNext();) {
			String propName = itor.next();
			String propValue = allConfigs.get(propName);
			if (!propName.isEmpty()) {
				content.append(propName + "=" + propValue).append("\n");
			}
		}
		byte[] bytes = content.toString().getBytes();

		IFolder configurationFolder = node.getFolder("configuration");
		if (!configurationFolder.exists()) {
			configurationFolder.create();
		}

		IFile configIniFile = configurationFolder.getFile("config.ini");
		if (!configIniFile.exists()) {
			configIniFile.create(bytes);
		} else {
			configIniFile.setContents(bytes);
		}

		try {
			Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
			perms.add(PosixFilePermission.OWNER_EXECUTE);
			perms.add(PosixFilePermission.OWNER_READ);
			perms.add(PosixFilePermission.OWNER_WRITE);
			perms.add(PosixFilePermission.GROUP_EXECUTE);
			perms.add(PosixFilePermission.GROUP_READ);
			perms.add(PosixFilePermission.OTHERS_READ);
			configIniFile.setFilePermissions(perms);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}

		return true;
	}

	public LaunchService getLaunchService() {
		LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
		return launchService;
	}

	public String getLaunchTypeId() {
		return LaunchType.Types.NODE.getId();
	}

	public String getLaunchConfigName(String nodeId) {
		String launchConfigName = "node_launch_config_" + nodeId;
		return launchConfigName;
	}

}
