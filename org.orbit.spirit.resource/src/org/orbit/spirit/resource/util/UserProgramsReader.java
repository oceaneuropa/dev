package org.orbit.spirit.resource.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
public class UserProgramsReader {

	/**
	 * Read contents of the resource from input stream.
	 * 
	 * @param resource
	 * @param inputStream
	 * @throws IOException
	 */
	public void read(UserProgramsResource resource, InputStream inputStream) throws IOException {
		read(resource, inputStream, false);
	}

	/**
	 * Read contents of the resource from input stream.
	 * 
	 * @param resource
	 * @param inputStream
	 * @param closeInputStream
	 * @throws IOException
	 */
	public void read(UserProgramsResource resource, InputStream inputStream, boolean closeInputStream) throws IOException {
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			UserPrograms userPrograms = documentToContents(document);
			if (userPrograms != null) {
				resource.getContents().clear();
				resource.getContents().add(userPrograms);
			}
		}
	}

	/**
	 * Read an JSON document to get the contents for UserProgramsResource.
	 * 
	 * @param document
	 * @return
	 */
	protected UserPrograms documentToContents(JSONObject document) {
		if (document == null) {
			return null;
		}

		List<UserProgram> userProgramList = new ArrayList<UserProgram>();

		// "UserProgram" array
		if (document.has("UserProgram")) {
			JSONArray userProgramJSONArray = document.getJSONArray("UserProgram");
			if (userProgramJSONArray != null) {
				int length = userProgramJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject userProgramJSONObject = userProgramJSONArray.getJSONObject(i);
					if (userProgramJSONObject != null) {
						UserProgram userProgram = jsonToUserProgram(userProgramJSONObject);
						if (userProgram != null) {
							userProgramList.add(userProgram);
						}
					}
				}
			}
		}

		UserPrograms userProgramsObj = new UserPrograms();
		userProgramsObj.getChildren().addAll(userProgramList);

		return userProgramsObj;
	}

	/**
	 * Converts JSON object to a UserProgram object.
	 * 
	 * @param userProgramJSONObject
	 * @return
	 */
	protected UserProgram jsonToUserProgram(JSONObject userProgramJSONObject) {
		if (userProgramJSONObject == null) {
			return null;
		}

		UserProgram userProgram = new UserProgram();

		// "id" attribute
		if (userProgramJSONObject.has("id")) {
			String id = userProgramJSONObject.getString("id");
			userProgram.setId(id);
		}

		// "version" attribute
		if (userProgramJSONObject.has("version")) {
			String version = userProgramJSONObject.getString("version");
			userProgram.setVersion(version);
		}

		return userProgram;
	}

}
