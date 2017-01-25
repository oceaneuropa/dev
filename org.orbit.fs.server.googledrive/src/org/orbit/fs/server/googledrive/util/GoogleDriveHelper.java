package org.orbit.fs.server.googledrive.util;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import org.orbit.fs.server.googledrive.GoogleDriveClient;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.util.Clock;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class GoogleDriveHelper {

	public static GoogleDriveHelper INSTANCE = new GoogleDriveHelper();

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isDirectory(File file) {
		return GoogleDriveMimeTypes.FOLDER.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isTextPlain(File file) {
		return GoogleDriveMimeTypes.TEXT_PLAIN.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isTextXml(File file) {
		return GoogleDriveMimeTypes.TEXT_XML.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isJson(File file) {
		return GoogleDriveMimeTypes.JSON.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isJpeg(File file) {
		return GoogleDriveMimeTypes.JPEG.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isPdf(File file) {
		return GoogleDriveMimeTypes.PDF.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isZip(File file) {
		return GoogleDriveMimeTypes.ZIP.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isGoogleDoc(File file) {
		return GoogleDriveMimeTypes.GOOGLE_DOC.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean isGoogleSpreadsheet(File file) {
		return GoogleDriveMimeTypes.GOOGLE_SPREAD_SHEET.equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param credential
	 */
	public void print(Credential credential) {
		String accessToken = credential.getAccessToken();
		String refreshToken = credential.getRefreshToken();
		Long expiresInSeconds = credential.getExpiresInSeconds();
		Clock clock = credential.getClock();
		AccessMethod accessMethod = credential.getMethod();

		System.out.println("============ Credential ============");
		System.out.println("accessToken = " + accessToken);
		System.out.println("refreshToken = " + refreshToken);
		System.out.println("expiresInSeconds = " + expiresInSeconds);
		System.out.println("clock = " + clock.currentTimeMillis());
		System.out.println("accessMethod = " + accessMethod);
		System.out.println("====================================\r\n");
	}

	/**
	 * 
	 * @param drive
	 */
	public void print(Drive drive) {
		String applicationName = drive.getApplicationName();
		String baseUrl = drive.getBaseUrl();
		String rootUrl = drive.getRootUrl();
		String servicePath = drive.getServicePath();

		System.out.println("============== Drive ===============");
		System.out.println("applicationName = " + applicationName);
		System.out.println("baseUrl = " + baseUrl);
		System.out.println("rootUrl = " + rootUrl);
		System.out.println("servicePath = " + servicePath);
		System.out.println("====================================\r\n");
	}

	/**
	 * 
	 * @param file
	 */
	public void print(File file) {
		// if (!GoogleDriveUtil.isFolder(file)) {
		// continue;
		// }
		// if (!GoogleDriveUtil.isTextPlain(file)) {
		// continue;
		// }
		// System.out.printf("%s (%s)\n", file.getTitle(), file.getId());

		String id = file.getId();
		String mimeType = file.getMimeType();
		String kind = file.getKind();
		String name = file.getName();
		String desc = file.getDescription();
		DateTime createdTime = file.getCreatedTime();
		boolean trashed = file.getTrashed();
		// String prettyStr = null;
		// try {
		// prettyStr = file.toPrettyString();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// System.out.printf("%s (%s)\n", name, mimeType);

		boolean isDir = isDirectory(file);
		String suffix = "";
		if (isDir) {
			suffix += "dir ";
		}
		if (trashed) {
			suffix += "trashed ";
		}
		suffix = suffix.trim();

		System.out.println("============== File ===============");
		System.out.println("id = " + id);
		System.out.println("mimeType = " + mimeType);
		System.out.println("kind = " + kind);
		System.out.println("name = " + name + " (" + trashed + ")");
		System.out.println("desc = " + desc);
		System.out.println("createdTime = " + createdTime);
		System.out.println("====================================\r\n");
	}

	/**
	 * <name> (<dir> <trashed>)
	 * 
	 * @param file
	 * @return
	 */
	public String getSimpleFileName1(File file) {
		String name = file.getName();

		boolean isDir = isDirectory(file);
		boolean trashed = false;
		try {
			trashed = file.getTrashed();
		} catch (Exception e) {
		}

		String suffix = "";
		if (isDir) {
			suffix += "dir ";
		}
		if (trashed) {
			suffix += "trashed ";
		}
		suffix = suffix.trim();

		if (suffix.isEmpty()) {
			return name;
		}
		return name + " (" + suffix + ")";
	}

	/**
	 * (<id>) <name> (<dir> <trashed>)
	 * 
	 * @param file
	 * @return
	 */
	public String getSimpleFileName2(File file) {
		String id = file.getId();
		String name = file.getName();
		// String fileExt = file.getFileExtension(); // always returns null

		boolean isDir = isDirectory(file);
		boolean trashed = false;
		try {
			trashed = file.getTrashed();
		} catch (Exception e) {
			// e.printStackTrace(); // NPE is always thrown
		}

		String suffix = "";
		if (isDir) {
			suffix += "dir ";
		}
		if (trashed) {
			suffix += "trashed ";
		}
		suffix = suffix.trim();

		String fullName = "(" + id + ") " + name;
		if (!suffix.isEmpty()) {
			fullName += " (" + suffix + ")";
		}
		return fullName;
	}

	/**
	 * (<id>) <name> (<dir> <trashed>)
	 * 
	 * @param file
	 * @return
	 */
	public String getSimpleFileName3(File file) {
		String id = file.getId();
		String name = file.getName();
		boolean isDir = isDirectory(file);
		boolean trashed = false;
		try {
			trashed = file.getTrashed();
		} catch (Exception e) {
		}

		String suffix = "";
		if (isDir) {
			suffix += "dir ";
		}
		if (trashed) {
			suffix += "trashed ";
		}
		suffix = suffix.trim();

		String result = null;
		if (suffix.isEmpty()) {
			result = String.format("(%s)    %s", id, name);
		} else {
			result = String.format("(%s)    %s    (%s)", id, name, suffix);
		}
		return result;
	}

	/**
	 * 
	 * @param client
	 * @param file
	 * @param comparator
	 * @throws IOException
	 */
	public void walkthrough(GoogleDriveClient client, File file, Comparator<File> comparator) throws IOException {
		walkthrough(client, file, comparator, 0);
	}

	/**
	 * 
	 * @param client
	 * @param file
	 * @param comparator
	 * @param level
	 * @throws IOException
	 */
	public void walkthrough(GoogleDriveClient client, File file, Comparator<File> comparator, int level) throws IOException {
		System.out.println(getSpaces(level) + getSimpleFileName1(file));

		if (isDirectory(file)) {
			int deeperLevel = level + 1;
			List<File> subFiles = client.getFiles(file, comparator);
			for (File subFile : subFiles) {
				walkthrough(client, subFile, comparator, deeperLevel);
			}
		}
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	protected String getSpaces(int level) {
		String spaces = ""; // $NON-NLS-1$
		for (int i = 0; i < level; i++) {
			spaces += "    "; // $NON-NLS-1$
		}
		return spaces;
	}

}
