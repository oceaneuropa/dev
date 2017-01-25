package org.orbit.fs.server.googledrive.util;

import java.util.Arrays;
import java.util.List;

import com.google.api.services.drive.DriveScopes;

public class GoogleDriveConstants {

	// Application name for testing
	public static final String APPLICATION_NAME = "Drive API Java Quickstart";

	// If modifying these scopes, delete your previously saved credentials at ~/.credentials/drive-java-quickstart.json
	public static final List<String> READ_ONLY_SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE);
	public static final List<String> READ_WRITE_SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE);

}
