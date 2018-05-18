package org.orbit.platform.runtime.extension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.extension.ResourceConfigurator;

public class PlatformNodePreConfigurator implements ResourceConfigurator {

	public static final String ID = "org.orbit.platform.runtime.PlatformNodeConfigurator";

	@Override
	public void configure(IWorkspace workspace, IResource resource) throws IOException {
		if (!(resource instanceof IFolder)) {
			return;
		}
		IPath folderFullPath = resource.getFullPath();

		// Create bin folder and configuration folder
		IPath binPath = folderFullPath.append("bin");
		IFolder binFolder = workspace.getFolder(binPath);
		if (!binFolder.exists()) {
			binFolder.create();
		}

		IPath configPath = folderFullPath.append("configuration");
		IFolder configFolder = workspace.getFolder(configPath);
		if (!configFolder.exists()) {
			configFolder.create();
		}

		IPath start_sh_path = binPath.append("start.sh");
		IFile start_sh_file = workspace.getFile(start_sh_path);
		ByteArrayInputStream input1 = null;
		try {
			String startFileContent = getStartFileContent();
			if (startFileContent != null) {
				input1 = new ByteArrayInputStream(startFileContent.getBytes(StandardCharsets.UTF_8));
			}
			if (input1 != null) {
				if (start_sh_file.exists()) {
					start_sh_file.setContents(input1);
				} else {
					start_sh_file.create(input1);
				}
			}
		} finally {
			if (input1 != null) {
				input1.close();
			}
		}

		IPath config_ini_path = configPath.append("config.ini");
		IFile config_ini_file = workspace.getFile(config_ini_path);
		ByteArrayInputStream input2 = null;
		try {
			String configIniFileContent = getConfigIniFileContent();
			if (configIniFileContent != null) {
				input2 = new ByteArrayInputStream(configIniFileContent.getBytes(StandardCharsets.UTF_8));
			}
			if (input2 != null) {
				if (config_ini_file.exists()) {
					config_ini_file.setContents(input2);
				} else {
					config_ini_file.create(input2);
				}
			}
		} finally {
			if (input2 != null) {
				input2.close();
			}
		}
	}

	protected String getStartFileContent() {
		String text = "";
		text += "PLATFORM_HOME=/Users/yayang/origin/ta1\r\n";
		text += "CURRENT_DIR=$(dirname $0)\r\n";
		text += "PARENT_DIR=\"$(dirname ${CURRENT_DIR})\"\r\n";
		text += "java -jar ${PLATFORM_HOME}/system/org.eclipse.osgi.jar -configuration ${PARENT_DIR}/configuration_v1 -console\r\n";
		return text;
	}

	protected String getConfigIniFileContent() {
		String text = "";
		text += "osgi.bundles=org.eclipse.equinox.console_1.1.100.v20141023-1406.jar@start, \\\r\n";
		text += "org.apache.felix.gogo.command-0.16.0.jar@start, \\\r\n";
		text += "org.apache.felix.gogo.runtime-0.16.2.jar@start, \\\r\n";
		text += "org.apache.felix.gogo.shell-0.12.0.jar@start, \\\r\n";
		text += "org.foo.hello.jar@start, \\\r\n";
		text += "org.foo.hello.client.jar@start\r\n";
		text += "eclipse.ignoreApp=true\r\n";
		text += "org.osgi.service.http.port=12001\r\n";
		text += "TA_HOME=/Users/yayang/origin/ta1\r\n";
		text += "NODE_HOME=/Users/yayang/origin/ta1/nodespaces/node1\r\n";
		return text;
	}

}

// IPath stop_sh_filePath = binPath.append("stop.sh");
// IFile stop_sh_file = workspace.getFile(stop_sh_filePath);
// stop_sh_file.create();
