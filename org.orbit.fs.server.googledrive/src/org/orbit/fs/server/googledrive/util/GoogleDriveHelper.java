package org.orbit.fs.server.googledrive.util;

import java.io.IOException;
import java.util.ArrayList;
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

	public static List<String> TEXT_PLAIN_FILE_EXTENSIONS = new ArrayList<String>();
	public static List<String> TEXT_XML_FILE_EXTENSIONS = new ArrayList<String>();
	public static List<String> JSON_FILE_EXTENSIONS = new ArrayList<String>();
	public static List<String> JPEG_FILE_EXTENSIONS = new ArrayList<String>();
	public static List<String> PDF_FILE_EXTENSIONS = new ArrayList<String>();
	public static List<String> ZIP_FILE_EXTENSIONS = new ArrayList<String>();
	public static List<String> DOC_FILE_EXTENSIONS = new ArrayList<String>();

	static {
		TEXT_PLAIN_FILE_EXTENSIONS.add("txt");
		TEXT_PLAIN_FILE_EXTENSIONS.add("log");
		TEXT_PLAIN_FILE_EXTENSIONS.add("properties");

		TEXT_XML_FILE_EXTENSIONS.add("xml");
		TEXT_XML_FILE_EXTENSIONS.add("xsd");
		TEXT_XML_FILE_EXTENSIONS.add("wsdl");

		JSON_FILE_EXTENSIONS.add("json");

		JPEG_FILE_EXTENSIONS.add("jpeg");

		PDF_FILE_EXTENSIONS.add("pdf");

		ZIP_FILE_EXTENSIONS.add("zip");

		DOC_FILE_EXTENSIONS.add("doc");
		DOC_FILE_EXTENSIONS.add("docx");
	}

	/**
	 * Get potential metatypes from file extension.
	 * 
	 * @param fileName
	 * @return
	 */
	public List<String> getCandidateMetaTypes(String fileName) {
		List<String> candidateMetaTypes = new ArrayList<String>();
		int index = fileName.lastIndexOf(".");
		if (index < 0 || index == fileName.length() - 1) {
			// no file extension - consider it as a directory/google doc/google spreadsheet
			candidateMetaTypes.add(GoogleDriveMimeTypes.FOLDER);
			candidateMetaTypes.add(GoogleDriveMimeTypes.GOOGLE_DOC);
			candidateMetaTypes.add(GoogleDriveMimeTypes.GOOGLE_SPREADSHEET);

		} else {
			String fileExtension = fileName.substring(index + 1);
			fileExtension = fileExtension.toLowerCase();
			if (TEXT_PLAIN_FILE_EXTENSIONS.contains(fileExtension)) {
				// txt file
				candidateMetaTypes.add(GoogleDriveMimeTypes.TEXT_PLAIN);

			} else if (TEXT_XML_FILE_EXTENSIONS.contains(fileExtension)) {
				// xml file
				candidateMetaTypes.add(GoogleDriveMimeTypes.TEXT_XML);

			} else if (JPEG_FILE_EXTENSIONS.contains(fileExtension)) {
				// jpeg file
				candidateMetaTypes.add(GoogleDriveMimeTypes.JPEG);

			} else if (JSON_FILE_EXTENSIONS.contains(fileExtension)) {
				// json file
				candidateMetaTypes.add(GoogleDriveMimeTypes.JSON);

			} else if (PDF_FILE_EXTENSIONS.contains(fileExtension)) {
				// pdf file
				candidateMetaTypes.add(GoogleDriveMimeTypes.PDF);

			} else if (ZIP_FILE_EXTENSIONS.contains(fileExtension)) {
				// zip file
				candidateMetaTypes.add(GoogleDriveMimeTypes.ZIP);

			} else if (DOC_FILE_EXTENSIONS.contains(fileExtension)) {
				// doc file
				candidateMetaTypes.add(GoogleDriveMimeTypes.GOOGLE_DOC);

			} else {
				// consider it as google doc/google spreadsheet for now.
				candidateMetaTypes.add(GoogleDriveMimeTypes.GOOGLE_DOC);
				candidateMetaTypes.add(GoogleDriveMimeTypes.GOOGLE_SPREADSHEET);

				System.out.println("### ### ### ### " + getClass().getSimpleName() + ".getCandidateMetaTypes() file extension is not supported: " + fileExtension);
			}
		}
		return candidateMetaTypes;
	}

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
		return GoogleDriveMimeTypes.GOOGLE_SPREADSHEET.equals(file.getMimeType()) ? true : false;
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
