package org.orbit.fs.server.googledrive.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Mime types
 * 
 * @see https://developers.google.com/drive/v3/web/manage-downloads
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */

public class GoogleDriveMimeTypes {

	// Directories
	public static final String FOLDER = "application/vnd.google-apps.folder";

	// Documents
	public static final String HTML = "text/html";
	public static final String PLAIN_TEXT = "text/plain";
	public static final String RICH_TEXT = "application/rtf";
	public static final String XML = "text/xml";
	public static final String JSON = "application/json";
	public static final String PDF = "application/pdf";
	public static final String MS_WORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String OPEN_OFFICE_DOC = "application/vnd.oasis.opendocument.text";
	public static final String GOOGLE_DOC = "application/vnd.google-apps.document";

	// Spreadsheets
	public static final String MS_EXCEL = "application/x-vnd.oasis.opendocument.spreadsheet";
	public static final String OPEN_OFFICE_SHEET = "application/x-vnd.oasis.opendocument.spreadsheet";
	public static final String CSV = "text/csv";
	public static final String GOOGLE_SPREADSHEET = "application/vnd.google-apps.spreadsheet";

	// Drawings
	public static final String JPEG = "image/jpeg";
	public static final String PNG = "image/png";
	public static final String SVG = "image/svg+xml";

	// Presentations
	public static final String MS_POWER_POINT = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

	// Apps Scripts
	public static final String JSON_SCRIPT = "application/vnd.google-apps.script+json";

	// Archives
	public static final String ZIP = "application/zip";
	public static final String OCTET_STREAM = "application/octet-stream";

	public static final Map<String, List<String>> MimeType_To_FileExtensions_Map = new LinkedHashMap<String, List<String>>();

	/**
	 * 
	 * @param mimeType
	 * @param fileExtensions
	 */
	protected static void put(String mimeType, String... fileExtensions) {
		List<String> fileExtensionList = new ArrayList<String>();
		for (String fileExtension : fileExtensions) {
			fileExtensionList.add(fileExtension);
		}
		MimeType_To_FileExtensions_Map.put(mimeType, fileExtensionList);
	}

	// put mimeType and its possible file extensions in map
	static {
		// Documents
		put(HTML, "html", "htm");
		put(PLAIN_TEXT, "txt");
		put(RICH_TEXT, "rtf");
		put(XML, "xml", "xsd", "wsdl");
		put(JSON, "json");
		put(PDF, "pdf");
		put(MS_WORD, "doc", "docx");
		put(OPEN_OFFICE_DOC, "text");
		put(GOOGLE_DOC, "doc");

		// Spreadsheets
		put(MS_EXCEL, "xlt", "xlsx");
		put(OPEN_OFFICE_SHEET, "spreadsheet");
		put(CSV, "csv");
		put(GOOGLE_SPREADSHEET, "spreadsheet");

		// Drawings
		put(JPEG, "jpeg");
		put(PNG, "png");
		put(SVG, "svg");

		// Presentations
		put(MS_POWER_POINT, "presentation");

		// Apps Scripts
		put(JSON_SCRIPT, "json");

		// Archives
		put(ZIP, "zip", "rar");
	}

}
