package org.orbit.platform.runtime.extension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.origin.common.env.OS;
import org.origin.common.launch.LaunchConfiguration;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.LaunchHandler;

public class NodeLauncher implements Launcher {

	public static String ID = "org.orbit.platform.runtime.PlatformLauncher";

	@Override
	public void launch(LaunchConfiguration config, LaunchHandler launch) throws IOException {
		Path path = null;

		config.getFile();

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
			Process process = new ProcessBuilder().redirectErrorStream(true).command(command).directory(path.toFile()).start();
		} catch (IOException e) {
			throw e;
		}
	}

}
