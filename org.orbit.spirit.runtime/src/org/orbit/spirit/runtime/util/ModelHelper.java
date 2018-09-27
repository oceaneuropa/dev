package org.orbit.spirit.runtime.util;

import java.io.IOException;
import java.net.URI;

import org.orbit.spirit.model.userprograms.UserPrograms;
import org.orbit.substance.io.DFS;
import org.orbit.substance.io.DFile;
import org.origin.common.resource.Resource;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;

public class ModelHelper {

	public static ModelHelper INSTANCE = new ModelHelper();

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public DFile getUserProgramsFile(String accessToken) throws IOException {
		DFile file = DFS.getDefault(accessToken).getFile("/config/user_programs.json");
		return file;
	}

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public UserPrograms getUserPrograms(String accessToken) throws IOException {
		UserPrograms userPrograms = null;
		DFile file = getUserProgramsFile(accessToken);
		if (!file.exists()) {
			userPrograms = createUserPrograms(accessToken);
		} else {
			userPrograms = loadUserPrograms(accessToken);
		}
		return userPrograms;
	}

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public UserPrograms createUserPrograms(String accessToken) throws IOException {
		DFile file = getUserProgramsFile(accessToken);
		if (file.exists()) {
			throw new IOException("File already exists. Path is '" + file.getPath().getPathString() + "'.");
		}

		file.createNewFile();
		if (!file.exists()) {
			throw new IOException("Cannot create new file. Path is '" + file.getPath().getPathString() + "'.");
		}

		URI uri = file.toURI();
		WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(uri);
		workingCopy.getContents().add(new UserPrograms());
		workingCopy.save();
		workingCopy.reload();

		UserPrograms userPrograms = workingCopy.getRootElement(UserPrograms.class);
		return userPrograms;
	}

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public UserPrograms loadUserPrograms(String accessToken) throws IOException {
		DFile file = getUserProgramsFile(accessToken);
		if (!file.exists()) {
			throw new IOException("File doesn't exist. Path is '" + file.getPath().getPathString() + "'.");
		}

		URI uri = file.toURI();
		WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(uri);
		UserPrograms userPrograms = workingCopy.getRootElement(UserPrograms.class);
		return userPrograms;
	}

	/**
	 * 
	 * @param accessToken
	 * @param userPrograms
	 * @throws IOException
	 */
	public void saveUserPrograms(String accessToken, UserPrograms userPrograms) throws IOException {
		Resource resource = userPrograms.eResource();
		if (resource == null) {
			throw new IOException("Resource is null.");
		}
		URI uri = resource.getURI();
		if (uri == null) {
			throw new IOException("URI is null.");
		}
		WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(uri);
		workingCopy.save();
	}

}
