package org.orbit.component.runtime.tier2.appstore.ws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.platform.runtime.util.ProgramUtil;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.util.FileUtil;
import org.origin.common.util.IOUtil;

/*-
 * 
 * App resources web service resource.
 * 
 * {contextRoot}: /orbit/v1/appstore
 * 
 * App metadata.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/content/{appId}/{appVersion}/{filePath}
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.APP_STORE_ADMIN })
@Path("/webcontent")
@Produces(MediaType.APPLICATION_JSON)
public class AppStoreWSContentResource extends AbstractWSApplicationResource {

	@Inject
	public AppStoreService service;

	public AppStoreService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("AppStoreService is not available.");
		}
		return this.service;
	}

	/*-
	 * Get app file content from {PLATFORM_HOME}/apps/{appId}_{appVersion}/WEB-INF/{filePath}
	 *
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/content/{appId}/{appVersion}/{filePath}
	 *
	 *           e.g. http://127.0.0.1:8003/orbit/v1/appstore/webcontent/{appId}/{appVersion}/{filePath}
	 *			 e.g. http://127.0.0.1:8003/orbit/v1/appstore/webcontent/org.orbit.app.calculator/1.0.0/images/calculator_64x64.png
	 *			 e.g. http://127.0.0.1:8003/orbit/v1/appstore/webcontent/org.orbit.app.calculator/1.0.0/js/calculator_1.js
	 *
	 *
	 * @param appId
	 * @param appVersion
	 * @param filePath
	 * @return
	 * @see PdfViewerWindowProvider and CubeWSResource
	 * @see https://stackoverflow.com/questions/2291428/jax-rs-pathparam-how-to-pass-a-string-with-slashes-hyphens-equals-too
	 */
	@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT })
	@GET
	@Path("/{appId}/{appVersion}/{filePath:.+}")
	// @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
	// @Produces({ "image/png", MediaType.APPLICATION_OCTET_STREAM })
	// @Produces(MediaType.TEXT_HTML)
	@Produces("image/png")
	// @Produces(MediaType.WILDCARD)
	public Response getWebContent(@PathParam("appId") String appId, @PathParam("appVersion") String appVersion, @PathParam("filePath") String filePath) {
		if (appId == null || appId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("Invalid path. Application id is not set.").build();
		}
		if (appVersion == null || appVersion.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("Invalid path. Application version is not set.").build();
		}
		if (appVersion == null || appVersion.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("Invalid path. File path is not set.").build();
		}

		String platformHome = PlatformSDKActivator.getInstance().getPlatform().getHome();

		// {PLATFORM_HOME}/apps
		File appsFolder = new File(platformHome + File.separator + "apps");

		// {PLATFORM_HOME}/apps/{appId}_{appVersion}
		File appFolder = new File(appsFolder, appId + "_" + appVersion);

		if (!appFolder.exists()) {
			syncAppWebContent(getService(), appId, appVersion, appFolder);
		}

		File webInfFolder = new File(appFolder, "WEB-INF");
		final File file = new File(webInfFolder, filePath);

		// Check the file is inside the app's WEB-INF folder
		boolean withinWebInf = false;
		if (file.exists()) {
			File parent = file.getParentFile();
			while (parent != null) {
				if (webInfFolder.equals(parent)) {
					withinWebInf = true;
					break;
				}
				parent = parent.getParentFile();
			}
		}

		if (withinWebInf) {
			// String fileExtension = null;
			// String fileName = file.getName();
			// int index = fileName.lastIndexOf(".");
			// if (index >= 0 && index < fileName.length() - 1) {
			// fileExtension = fileName.substring(index + 1);
			// }

			StreamingOutput output = new StreamingOutput() {
				@Override
				public void write(OutputStream outputStream) throws IOException, WebApplicationException {
					FileUtil.copyFileToOutputStream(file, outputStream);
					outputStream.flush();
				}
			};

			// e.g.
			// http://127.0.0.1:8003/orbit/v1/appstore/webcontent/org.orbit.app.calculator/1.0.0/images/calculator_64x64.png
			// http://127.0.0.1:8003/orbit/v1/appstore/webcontent/org.orbit.app.calculator/1.0.0/js/calculator_1.js
			// http://127.0.0.1:8003/orbit/v1/appstore/webcontent/org.orbit.app.calculator/1.0.0/images/icon_m.png

			// .header("Content-Disposition", "inline; filename=" + file.getName())
			// .header("Content-Disposition", "attachment; filename=" + file.getName())
			// , "application/" + fileExtension
			return Response.ok(output).header("Content-Disposition", "inline; filename=" + file.getName()).build();
		}

		String path = file.getPath().substring(webInfFolder.getPath().length());
		return Response.ok("File '" + path + "' is not found.").build();
	}

	/**
	 * 
	 * @param service
	 * @param appId
	 * @param appVersion
	 * @param appFolder
	 */
	public void syncAppWebContent(AppStoreService service, String appId, String appVersion, File appFolder) {
		if (appFolder.exists()) {
			appFolder.delete();
		}

		File downloadFolder = ProgramUtil.getDownloadDir();

		File archiveFile = new File(downloadFolder, appId + "_" + appVersion + ".app");
		if (downloadFolder != null) {
		}
		InputStream input = null;
		try {
			if (!downloadFolder.exists()) {
				downloadFolder.mkdirs();
			}
			input = service.getContentInputStream(appId, appVersion);

			if (input != null) {
				if (archiveFile.exists()) {
					archiveFile.delete();
				}
				FileUtil.copyInputStreamToFile(input, archiveFile);
			}
			if (archiveFile.exists()) {
				ProgramUtil.extractToProgramFolder(appFolder, archiveFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input, true);
		}
	}

}
