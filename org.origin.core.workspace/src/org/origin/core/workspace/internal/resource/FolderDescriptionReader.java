package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.resource.Resource;
import org.origin.core.workspace.IFolderDescription;
import org.origin.core.workspace.impl.FolderDescriptionImpl;

/*

Example:
{project}/.metadata/{folder}.folder.json
-----------------------------------------------------------------------
{
    "ProjectDescription": {
		"natureIds": [
			"org.nb.home.ProjectNature"
		],
		"properties": {
			"key1": "value1",
			"key2": "value2"
		}
	}
}
-----------------------------------------------------------------------

*/
public class FolderDescriptionReader extends DescriptionReader {

	/**
	 * Read from input stream into the given resource.
	 * 
	 * @param resource
	 * @param inputStream
	 * @throws IOException
	 */
	public void read(Resource resource, InputStream inputStream) throws IOException {
		read(resource, inputStream, false);
	}

	/**
	 * Read from input stream into the given resource.
	 * 
	 * @param resource
	 * @param inputStream
	 * @param closeInputStream
	 * @throws IOException
	 */
	public void read(Resource resource, InputStream inputStream, boolean closeInputStream) throws IOException {
		IFolderDescription folderDesc = null;
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			folderDesc = documentToFolderDescription(document);
		}
		if (folderDesc != null) {
			resource.getContents().add(folderDesc);
		}
	}

	/**
	 * Converts an JSON document to a FolderDescription object.
	 * 
	 * @param document
	 * @return
	 */
	protected IFolderDescription documentToFolderDescription(JSONObject document) {
		if (document == null) {
			return null;
		}

		IFolderDescription folderDesc = null;

		if (document.has("FolderDescription")) {
			JSONObject projectDescJSON = document.getJSONObject("FolderDescription");
			if (projectDescJSON != null) {
				folderDesc = jsonToFolderDescription(projectDescJSON);
			}
		}

		return folderDesc;
	}

	/**
	 * Converts JSON object to a FolderDescription object.
	 * 
	 * @param folderDescJSON
	 * @return
	 */
	protected IFolderDescription jsonToFolderDescription(JSONObject folderDescJSON) {
		if (folderDescJSON == null) {
			return null;
		}

		IFolderDescription folderDesc = new FolderDescriptionImpl();

		// "natureIds" string array
		jsonToNatureIds(folderDescJSON, folderDesc);

		// "properties" attribute
		jsonToProperties(folderDescJSON, folderDesc);

		return folderDesc;
	}

}
