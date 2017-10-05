package org.origin.core.resources.impl.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.WorkspaceDescription;

public class WorkspaceDatabaseImpl implements IWorkspace {

	protected String id;
	protected ConnectionAware connectionAware;
	protected int workspaceId;

	/**
	 * 
	 * @param id
	 * @param connectionAware
	 */
	public WorkspaceDatabaseImpl(String id, ConnectionAware connectionAware) {
		this.id = id;
		this.connectionAware = connectionAware;
		// this.workspacesTableHandler = new WorkspacesTableHandler(id);
	}

	/**
	 * 
	 * @param id
	 * @param connectionAware
	 * @param workspaceId
	 */
	public WorkspaceDatabaseImpl(String id, ConnectionAware connectionAware, int workspaceId) {
		this.id = id;
		this.connectionAware = connectionAware;
		this.workspaceId = workspaceId;
		// this.workspacesTableHandler = new WorkspacesTableHandler(id);
	}

	public Connection getConnection() {
		return this.connectionAware.getConnection();
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
	public synchronized boolean delete() {
		if (exists()) {

		}
		return false;
	}

	@Override
	public void dispose() {

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public IResource[] getRootMembers() {
		// return this.rootFolder.getMembers();
		return null;
	}

	@Override
	public IResource findRootMember(String name) {
		// return this.rootFolder.findMember(name);
		return null;
	}

	@Override
	public <RESOURCE extends IResource> RESOURCE findRootMember(String name, Class<RESOURCE> clazz) {
		// return this.rootFolder.findMember(name, clazz);
		return null;
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
	public boolean deleteUnderlyingResource(IPath fullpath) {
		return false;
	}

}
