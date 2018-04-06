package org.orbit.platform.runtime.extension.launch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.orbit.platform.sdk.extensions.LaunchConfiguration;
import org.orbit.platform.sdk.extensions.Launcher;
import org.origin.common.env.OS;

public class PlatformLauncher implements Launcher {

	@Override
	public Object launch(LaunchConfiguration config) throws IOException {
		Process proc = null;
		if (config instanceof PlatformLaunchConfiguration) {
			// PlatformLaunchConfiguration platformConfig = (PlatformLaunchConfiguration) config;
			// String name = config.getName();
			Path path = config.getPath();

			String[] command = null;
			if (OS.isWindows()) {
				Path startFile = path.resolve("start.bat");
				command = new String[] { "cmd.exe", "/C", startFile.toAbsolutePath().toString() };
			} else {
				Path startFile = path.resolve("start.sh");
				if (!Files.isExecutable(startFile)) {
					throw new IOException("\"" + startFile.toAbsolutePath() + "\" is not executable!");
				}
				command = new String[] { startFile.toAbsolutePath().toString() };
			}

			try {
				proc = new ProcessBuilder().redirectErrorStream(true).command(command).directory(path.toFile()).start();
			} catch (IOException e) {
				throw e;
			}
		}
		return proc;
	}

}
