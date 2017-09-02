package org.origin.core.resources.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IRoot;

public class FsRootNio implements IRoot {

	@Override
	public IResource[] getRootMembers() {
		return null;
	}

	@Override
	public IResource findRootMember(String name) {
		return null;
	}

	@Override
	public <RESOURCE extends IResource> RESOURCE findRootMember(String name, Class<RESOURCE> clazz) {
		return null;
	}

	@Override
	public IResource[] getMembers(IPath fullpath) {
		return null;
	}

	@Override
	public IFolder getParent(IPath fullpath) throws IOException {
		return null;
	}

	@Override
	public IFile getFile(IPath fullpath) throws IOException {
		return null;
	}

	@Override
	public IFolder getFolder(IPath fullpath) throws IOException {
		return null;
	}

	@Override
	public <FILE extends IFile> FILE getFile(IPath fullpath, Class<FILE> clazz) throws IOException {
		return null;
	}

	@Override
	public <FOLDER extends IFolder> FOLDER getFolder(IPath fullpath, Class<FOLDER> clazz) throws IOException {
		return null;
	}

	@Override
	public boolean underlyingResourceExists(IPath fullpath) {
		return false;
	}

	@Override
	public boolean createUnderlyingFile(IPath fullpath) throws IOException {
		return false;
	}

	@Override
	public boolean createUnderlyingFile(IPath fullpath, InputStream input) throws IOException {
		return false;
	}

	@Override
	public boolean createUnderlyingFolder(IPath fullpath) throws IOException {
		return false;
	}

	@Override
	public Object getUnderlyingResource(IPath fullpath) {
		return null;
	}

	@Override
	public boolean delete(IPath fullpath) {
		return false;
	}

	@Override
	public InputStream getInputStream(IPath fullpath) throws IOException {
		return null;
	}

	@Override
	public OutputStream getOutputStream(IPath fullpath) throws IOException {
		return null;
	}

	@Override
	public void dispose() {
	}

}
