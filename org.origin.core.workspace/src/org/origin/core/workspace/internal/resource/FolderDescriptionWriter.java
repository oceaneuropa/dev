package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.core.workspace.IFolderDescription;

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
public class FolderDescriptionWriter extends DescriptionWriter {

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(FolderDescriptionResource resource, OutputStream output) throws IOException {
		write(resource, output, false);
	}

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @param closeOutputStream
	 * @throws IOException
	 */
	public void write(FolderDescriptionResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = folderDescriptionToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Convert folder description resource to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject folderDescriptionToDocument(FolderDescriptionResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		IFolderDescription folderDesc = resource.getFolderDescription();
		if (folderDesc != null) {
			JSONObject folderDescJSON = folderDescriptionToJSON(folderDesc);
			if (folderDescJSON != null) {
				document.put("FolderDescription", folderDescJSON);
			}
		}

		return document;
	}

	/**
	 * Convert FolderDescription object to JSONObject
	 * 
	 * @param folderDesc
	 * @return
	 */
	protected JSONObject folderDescriptionToJSON(IFolderDescription folderDesc) {
		JSONObject folderDescJSON = new JSONObject();

		// "natureIds" array
		natureIdsToJson(folderDesc, folderDescJSON);

		// "properties" attribute
		propertiesToJson(folderDesc, folderDescJSON);

		return folderDescJSON;
	}

}
