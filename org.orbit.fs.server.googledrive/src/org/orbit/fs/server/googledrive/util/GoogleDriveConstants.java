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

	// Drive.Files.Get fields:
	// appProperties,capabilities,contentHints,createdTime,description,explicitlyTrashed,fileExtension,folderColorRgb,fullFileExtension,hasThumbnail,headRevisionId,iconLink,id,imageMediaMetadata,isAppAuthorized,kind,lastModifyingUser,md5Checksum,mimeType,modifiedByMe,modifiedByMeTime,modifiedTime,name,originalFilename,ownedByMe,owners,parents,permissions,properties,quotaBytesUsed,shared,sharedWithMeTime,sharingUser,size,spaces,starred,thumbnailLink,thumbnailVersion,trashed,version,videoMediaMetadata,viewedByMe,viewedByMeTime,viewersCanCopyContent,webContentLink,webViewLink,writersCanShare
	// @see https://developers.google.com/apis-explorer/#p/drive/v3/drive.files.get
	public static final String FILE_FIELDS_ID = "id";
	public static final String FILE_FIELDS_ID_PARENTS = "id,parents";
	public static final String FILE_FIELDS_SIMPLE = "id,parents,name,mimeType,trashed";
	public static final String FILE_FIELDS_ALL = "appProperties,capabilities,contentHints,createdTime,description,explicitlyTrashed,fileExtension,folderColorRgb,fullFileExtension,hasThumbnail,headRevisionId,iconLink,id,imageMediaMetadata,isAppAuthorized,kind,lastModifyingUser,md5Checksum,mimeType,modifiedByMe,modifiedByMeTime,modifiedTime,name,originalFilename,ownedByMe,owners,parents,permissions,properties,quotaBytesUsed,shared,sharedWithMeTime,sharingUser,size,spaces,starred,thumbnailLink,thumbnailVersion,trashed,version,videoMediaMetadata,viewedByMe,viewedByMeTime,viewersCanCopyContent,webContentLink,webViewLink,writersCanShare";

}
