package org.orbit.platform.runtime.programs;

import java.nio.file.Path;
import java.util.List;

import org.orbit.os.api.apps.ProgramManifest;
import org.origin.common.runtime.Problem;

public interface ProgramsAndFeatures {

	void start() throws ProgramException;

	void stop() throws ProgramException;

	ProgramManifest[] getInstalledApps();

	boolean isInstalled(String appId, String appVersion);

	ProgramManifest getInstalledPrograms(String appId, String appVersion);

	ProgramManifest install(Path appArchivePath) throws ProgramException;

	ProgramManifest uninstall(String appId, String appVersion) throws ProgramException;

	ProgramHandler[] getProgramHandlers();

	ProgramHandler getProgramHandler(String appId, String appVersion);

	List<Problem> getProblems();

}

// AppManifest install(String appId, String appVersion) throws AppException;
