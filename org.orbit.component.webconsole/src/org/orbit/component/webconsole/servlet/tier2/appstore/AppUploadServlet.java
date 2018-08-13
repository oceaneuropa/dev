package org.orbit.component.webconsole.servlet.tier2.appstore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.MessageHelper;
import org.orbit.platform.sdk.PlatformSDKActivator;

/**
 * Upon receiving file upload submission, parses the request to read upload data and saves the file on disk.
 * 
 * @see org.apache.felix.webconsole.internal.core.BundlesServlet
 */
public class AppUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -6442023711681879436L;

	// location to store file uploaded
	protected static final String UPLOAD_DIRECTORY = "upload";

	// upload settings
	protected static final int MEMORY_THRESHOLD = 10 * 1024 * 1024; // 10MB
	protected static final int MAX_FILE_SIZE = 250 * 1024 * 1024; // 250MB
	protected static final int MAX_REQUEST_SIZE = 260 * 1024 * 1024; // 260MB

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String appStoreUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_APP_STORE_URL);

		String message = "";

		// 1. Check multipart form
		boolean isMultipartForm = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartForm) {
			message = MessageHelper.INSTANCE.add(message, "Error: Form must has enctype=multipart/form-data.");
		}

		if (isMultipartForm) {
			File appUploadDir = null;
			try {
				// 2. Prepare upload directory
				String platformHome = PlatformSDKActivator.getInstance().getPlatform().getHome();
				String uploadPath = platformHome + File.separator + UPLOAD_DIRECTORY;

				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}

				// 3. Configure upload settings
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(MEMORY_THRESHOLD); // sets memory threshold - beyond which files are stored in disk
				factory.setRepository(new File(System.getProperty("java.io.tmpdir"))); // sets temporary location to store files

				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setFileSizeMax(MAX_FILE_SIZE);
				upload.setSizeMax(MAX_REQUEST_SIZE);

				// 4. Parse request
				List<FileItem> fileItems = upload.parseRequest(request);
				if (fileItems == null) {
					fileItems = new ArrayList<FileItem>();
				}

				// 5. get id, appId, appVersion
				int id = -1;
				String appId = null;
				String appVersion = null;
				for (FileItem fileItem : fileItems) {
					if (fileItem.isFormField()) {
						String currFieldName = fileItem.getFieldName();
						String currFieldValue = fileItem.getString();
						if ("id".equals(currFieldName)) {
							try {
								id = Integer.parseInt(currFieldValue);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if ("appId".equals(currFieldName)) {
							appId = currFieldValue;
						} else if ("appVersion".equals(currFieldName)) {
							appVersion = currFieldValue;
						}
					}
				}
				if (id == -1) {
					message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
				}
				if (appId == null || appId.isEmpty()) {
					message = MessageHelper.INSTANCE.add(message, "'appId' parameter is not set.");
				}
				if (appVersion == null || appVersion.isEmpty()) {
					message = MessageHelper.INSTANCE.add(message, "'appVersion' parameter is not set.");
				}

				if (id != -1 && (appId != null && !appId.isEmpty()) && (appVersion != null && !appVersion.isEmpty())) {
					// 6. Prepare app upload directory
					appUploadDir = new File(uploadDir, appId + "_" + appVersion);
					if (!appUploadDir.exists()) {
						appUploadDir.mkdirs();
					}

					// 7. Upload file to local folder (of the web server)
					List<File> localFiles = new ArrayList<File>();
					for (FileItem fileItem : fileItems) {
						if (!fileItem.isFormField()) {
							String fileName = fileItem.getName();
							try {
								File localFile = new File(appUploadDir, fileName);
								fileItem.write(localFile);
								localFiles.add(localFile);

								message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is uploaded to web server.");

							} catch (Exception e) {
								message = MessageHelper.INSTANCE.add(message, "File '" + fileName + "' is not uploaded to web server. " + e.getMessage());
							}
						}
					}

					// 8. Upload file to app store
					boolean succeed = OrbitComponentHelper.AppStore.uploadAppFile(appStoreUrl, id, appId, appVersion, localFiles);
					if (succeed) {
						message = MessageHelper.INSTANCE.add(message, "App file is uploaded to app store successfully!");
					} else {
						message = MessageHelper.INSTANCE.add(message, "App file is not uploaded to app store.");
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();

			} finally {
				// 9. Clean up
				if (appUploadDir != null && appUploadDir.exists()) {
					try {
						// appDir.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/appstore/apps");
	}

}
