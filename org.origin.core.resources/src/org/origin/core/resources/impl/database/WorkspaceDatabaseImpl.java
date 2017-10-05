package org.origin.core.resources.impl.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.WorkspaceDescription;
import org.origin.core.resources.impl.FolderImpl;
import org.origin.core.resources.impl.PathImpl;

public class WorkspaceDBImpl implements IWorkspace {

	private String id;
	private String name;
	private FolderImpl rootFolder;

	/**
	 * 
	 * @param id
	 * @param name
	 */
	public WorkspaceDBImpl(String name) {
		this(name, name);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 */
	public WorkspaceDBImpl(String id, String name) {
		this.id = id;
		this.name = name;
		this.rootFolder = new FolderImpl(this, PathImpl.ROOT);
	}

	@Override
	public boolean create(WorkspaceDescription desc) throws IOException {
		return false;
	}

	@Override
	public void setDescription(WorkspaceDescription desc) throws IOException {

	}

	@Override
	public WorkspaceDescription getDescription() throws IOException {
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public IResource[] getRootMembers() {
		return this.rootFolder.getMembers();
	}

	@Override
	public IResource findRootMember(String name) {
		return this.rootFolder.findMember(name);
	}

	@Override
	public <RESOURCE extends IResource> RESOURCE findRootMember(String name, Class<RESOURCE> clazz) {
		return this.rootFolder.findMember(name, clazz);
	}

	@Override
	public IResource[] getMembers(IPath fullpath) {
		List<IResource> members = new ArrayList<IResource>();

		String[] pathSegments = fullpath.getSegments();

		return members.toArray(new IResource[members.size()]);
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
	public InputStream getInputStream(IPath fullpath) throws IOException {
		return null;
	}

	@Override
	public OutputStream getOutputStream(IPath fullpath) throws IOException {
		return null;
	}

	@Override
	public boolean delete(IPath fullpath) {
		return false;
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
	public synchronized boolean delete() {
		if (exists()) {

		}
		return false;
	}

	@Override
	public void dispose() {

	}

}
