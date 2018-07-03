package org.origin.common.resources.impl.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.WorkspaceDescription;

/*
 * Workspaces table name: "workspaces"
 * -----------------------------------
 * workspaceId 		workspaceName
 * -----------------------------------
 * 1				workspace1
 * 2				workspace2
 * 3				workspace3
 * -----------------------------------
 * 
 * Each workspace has its own table, so that data from different workspaces are separated and won't affect each other.
 * 
 * e.g.
 * workspace1 table name: "workspace_1"
 * workspace2 table name: "workspace_2"
 * workspace3 table name: "workspace_3"
 * 
 */
public class WorkspaceDatabaseImpl implements IWorkspace {

	protected String id;
	protected ConnectionAware connectionAware;
	protected int workspaceName;

	/**
	 * 
	 * @param connectionAware
	 * @param workspaceId
	 */
	public WorkspaceDatabaseImpl(ConnectionAware connectionAware, int workspaceId) {
		this.connectionAware = connectionAware;
		this.workspaceName = workspaceId;
		// TODO
		// derive workspace table name from the workspaceId and create workspace table handler for the workspace table.
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
	public IFolder getVirtualRoot() {
		return null;
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
	public boolean createUnderlyingFile(IFile file) throws IOException {
		return false;
	}

	@Override
	public boolean createUnderlyingFile(IFile file, InputStream input) throws IOException {
		return false;
	}

	@Override
	public boolean createUnderlyingFolder(IFolder folder) throws IOException {
		return false;
	}

	@Override
	public boolean renameUnderlyingResource(IPath fullpath, String newName) throws IOException {
		return false;
	}

	@Override
	public boolean deleteUnderlyingResource(IPath fullpath) {
		return false;
	}

}
