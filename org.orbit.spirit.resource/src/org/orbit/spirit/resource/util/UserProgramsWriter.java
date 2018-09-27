package org.orbit.spirit.resource.util;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.spirit.model.userprograms.UserProgram;
import org.orbit.spirit.model.userprograms.UserPrograms;
import org.orbit.spirit.resource.userprograms.UserProgramsResource;
import org.origin.common.json.JSONUtil;

/*
Example:
-----------------------------------------------------------------------
{
    "UserProgram": [
    	{
    		"id": "foo",
			"version": "1.0.0",
		},
    	{
    		"id": "bar",
			"version": "2.0.0",
		}
	]
}
-----------------------------------------------------------------------
*/
public class UserProgramsWriter {

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(UserProgramsResource resource, OutputStream output) throws IOException {
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
	public void write(UserProgramsResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = contentsToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Write UserProgramsResource contents to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject contentsToDocument(UserProgramsResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		// "UserProgram" array
		JSONArray userProgramJSONArray = new JSONArray();

		int index = 0;
		UserPrograms userPrograms = resource.getUserPrograms();
		if (userPrograms != null) {
			for (UserProgram userProgram : userPrograms.getChildren()) {
				JSONObject userProgramJSONObject = userProgramToJSON(userProgram);
				if (userProgramJSONObject != null) {
					userProgramJSONArray.put(index++, userProgramJSONObject);
				}
			}
		}
		document.put("UserProgram", userProgramJSONArray);

		return document;
	}

	/**
	 * Convert UserProgram object to JSONObject.
	 * 
	 * @param userProgram
	 * @return
	 */
	protected JSONObject userProgramToJSON(UserProgram userProgram) {
		JSONObject userProgramJSON = new JSONObject();

		// "id" attribute
		String id = userProgram.getId();
		if (id != null) {
			userProgramJSON.put("id", id);
		}

		// "version" attribute
		String version = userProgram.getVersion();
		if (version != null) {
			userProgramJSON.put("version", version);
		}

		return userProgramJSON;
	}

}
