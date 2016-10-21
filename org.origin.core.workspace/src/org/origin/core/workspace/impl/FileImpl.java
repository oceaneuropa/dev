package org.origin.core.workspace.impl;

import java.io.File;

import org.origin.core.workspace.IResource;

public class FileImpl extends ResourceImpl {

	/**
	 * 
	 * @param file
	 */
	public FileImpl(File file) {
		super(file);
	}

	@Override
	public int getType() {
		return IResource.FILE;
	}

	@Override
	public File getDescriptionFile() {
		return null;
	}

	@Override
	public void load() {
	}

	@Override
	public void save() {
	}

}
