package org.origin.common.resources.impl.misc;

import java.io.IOException;

import org.origin.common.resources.Constants;
import org.origin.common.resources.FolderDescription;
import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;

public class FolderDescriptionPersistence {

	private static FolderDescriptionPersistence INSTANCE = new FolderDescriptionPersistence();

	public static FolderDescriptionPersistence getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param folder
	 * @return
	 * @throws IOException
	 */
	private IFile getFolderDescriptionFile(IFolder folder) throws IOException {
		IFolder dotMetadataFolder = folder.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		IFile dotFolderFile = dotMetadataFolder.getFile(Constants.DOT_FOLDER_FILE_NAME);
		return dotFolderFile;
	}

	/**
	 * 
	 * @param folder
	 * @return
	 * @throws IOException
	 */
	public FolderDescription load(IFolder folder) throws IOException {
		IFile dotFolderFile = getFolderDescriptionFile(folder);
		FolderDescriptionReader reader = new FolderDescriptionReader();
		return reader.read(dotFolderFile);
	}

	/**
	 * 
	 * @param folder
	 * @param desc
	 * @throws IOException
	 */
	public void save(IFolder folder, FolderDescription desc) throws IOException {
		IFolder dotMetadataFolder = folder.getFolder(Constants.DOT_METADATA_FOLDER_NAME);
		if (!dotMetadataFolder.exists()) {
			dotMetadataFolder.create();
		}
		IFile dotFolderFile = getFolderDescriptionFile(folder);
		if (!dotFolderFile.exists()) {
			dotFolderFile.create();
		}
		FolderDescriptionWriter writer = new FolderDescriptionWriter();
		writer.write(desc, dotFolderFile);
	}

}
