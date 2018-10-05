package org.orbit.component.webconsole.servlet.tier2.appstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.MimeTypes;
import org.origin.common.util.ServletUtil;

public class AppDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 6580799312190417850L;

	// location to store file to be downloaded
	protected static final String DOWNLOAD_DIRECTORY = "download";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String appStoreUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_APP_STORE_URL);
		String message = "";

		String appId = ServletUtil.getParameter(request, "appId", "");
		String appVersion = ServletUtil.getParameter(request, "appVersion", "");

		if (appId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'appId' parameter is not set.");
		}
		if (appVersion.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'appVersion' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		OutputStream output = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			AppManifest app = ComponentClientsUtil.AppStore.getApp(appStoreUrl, accessToken, appId, appVersion);
			if (app == null) {
				message = MessageHelper.INSTANCE.add(message, "App '" + appId + "' (" + appVersion + ") is not found.");

			} else {
				// Prepare download directory
				String platformHome = PlatformSDKActivator.getInstance().getPlatform().getHome();
				String downloadPath = platformHome + File.separator + DOWNLOAD_DIRECTORY;

				File downloadDir = new File(downloadPath);
				if (!downloadDir.exists()) {
					downloadDir.mkdirs();
				}
				File appDownloadDir = new File(downloadDir, appId + "_" + appVersion);
				if (!appDownloadDir.exists()) {
					appDownloadDir.mkdirs();
				}

				String fileName = app.getFileName();
				File localFile = new File(appDownloadDir, fileName);

				output = new FileOutputStream(localFile);
				ComponentClientsUtil.AppStore.downloadAppFile(appStoreUrl, accessToken, appId, appVersion, output);

				if (localFile.exists()) {
					String fileType = MimeTypes.get().getByFileName(localFile.getName());
					response.setContentType(fileType);
					response.setHeader("Content-disposition", "attachment; filename=" + fileName);

					// ---------------------------------------------------------------
					// Send the file to browser
					// ---------------------------------------------------------------
					FileInputStream input = new FileInputStream(localFile);

					OutputStream out = response.getOutputStream();
					IOUtil.copy(input, out);
					out.flush();
					out.close();

					input.close();
				}
			}

		} catch (ClientException e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();

		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

}
