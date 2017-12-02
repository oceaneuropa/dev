package org.origin.core.resources.impl.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.origin.common.io.FileUtil;
import org.origin.common.io.IOUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.ResourcesFactory;
import org.origin.core.resources.WorkspaceDescription;
import org.origin.core.resources.impl.FolderImpl;
import org.origin.core.resources.impl.PathImpl;
import org.origin.core.resources.impl.misc.WorkspaceDescriptionPersistence;

public class WorkspaceLocalImpl implements IWorkspace {

	protected File workspaceFsFolder;
	protected FolderImpl virtualRoot;
	protected WorkspaceDescription desc;
	protected Map<IPath, IResource> allResourcesMap = new TreeMap<IPath, IResource>();

	/**
	 * Constructor for non-existing workspace.
	 */
	public WorkspaceLocalImpl() {
	}

	/**
	 * Constructor for existing workspace.
	 * 
	 * @param workspaceFolder
	 */
	public WorkspaceLocalImpl(File workspaceFolder) {
		this.workspaceFsFolder = workspaceFolder;
		this.virtualRoot = new FolderImpl(this, PathImpl.ROOT);
	}

	@Override
	public boolean create(WorkspaceDescription desc) throws IOException {
		if (exists()) {
			return false;
		}

		// 1. Create workspace folder in file system
		if (this.workspaceFsFolder == null) {
			String workspaceFolderPath = desc.getWorkspaceFolderPath();
			this.workspaceFsFolder = new File(workspaceFolderPath);
			this.virtualRoot = new FolderImpl(this, PathImpl.ROOT);
		}
		if (this.workspaceFsFolder.isFile()) {
			throw new IOException(this.workspaceFsFolder.getAbsolutePath() + " already exists, but is not a directory.");
		}
		if (!this.workspaceFsFolder.exists()) {
			this.workspaceFsFolder.mkdirs();
		}

		// 2. Create {workspace}/.metadata/.workspace file and save WorkspaceDescription to it
		setDescription(desc);

		return exists();
	}

	@Override
	public void setDescription(WorkspaceDescription desc) throws IOException {
		this.desc = desc;
		WorkspaceDescriptionPersistence.getInstance().save(this, desc);
	}

	@Override
	public WorkspaceDescription getDescription() throws IOException {
		if (this.desc == null) {
			this.desc = WorkspaceDescriptionPersistence.getInstance().load(this);
		}
		return this.desc;
	}

	@Override
	public String getName() {
		return this.workspaceFsFolder.getName();
	}

	@Override
	public boolean exists() {
		if (this.workspaceFsFolder != null && this.workspaceFsFolder.isDirectory()) {
			try {
				IFile dotWorkspaceFile = WorkspaceDescriptionPersistence.getInstance().getWorkspaceDescriptionFile(this);
				if (dotWorkspaceFile != null && dotWorkspaceFile.exists()) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public synchronized boolean delete() throws IOException {
		if (exists()) {
			return FileUtil.deleteDirectory(this.workspaceFsFolder);
		}
		return false;
	}

	@Override
	public synchronized void dispose() {
		this.allResourcesMap.clear();
	}

	@Override
	public IFolder getVirtualRoot() {
		return this.virtualRoot;
	}

	@Override
	public IResource[] getRootMembers() {
		return this.virtualRoot.getMembers();
	}

	@Override
	public IResource findRootMember(String name) {
		return this.virtualRoot.findMember(name);
	}

	@Override
	public <RESOURCE extends IResource> RESOURCE findRootMember(String name, Class<RESOURCE> clazz) {
		return this.virtualRoot.findMember(name, clazz);
	}

	@Override
	public IResource[] getMembers(IPath fullpath) {
		List<IResource> members = new ArrayList<IResource>();

		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null) {
			File[] fsMemberFiles = fsFile.listFiles();

			if (fsMemberFiles != null) {
				synchronized (this.allResourcesMap) {
					for (File fsMemberFile : fsMemberFiles) {
						IPath memberFullpath = fullpath.append(fsMemberFile.getName());

						IResource iResource = this.allResourcesMap.get(memberFullpath);
						if (iResource != null) {
							members.add(iResource);

						} else {
							if (fsMemberFile.isFile()) {
								IFile iFile = ResourcesFactory.getInstance().createFile(this, memberFullpath);
								if (iFile != null) {
									this.allResourcesMap.put(memberFullpath, iFile);
									members.add(iFile);
								}

							} else if (fsMemberFile.isDirectory()) {
								IFolder iFolder = ResourcesFactory.getInstance().createFolder(this, memberFullpath);
								if (iFolder != null) {
									this.allResourcesMap.put(memberFullpath, iFolder);
									members.add(iFolder);
								}
							}
						}
					}
				}
			}
		}

		return members.toArray(new IResource[members.size()]);
	}

	@Override
	public IFolder getParent(IPath fullpath) throws IOException {
		IPath parentFullpath = fullpath.getParent();
		IFolder parent = getFolder(parentFullpath);
		return parent;
	}

	@Override
	public IFile getFile(IPath fullpath) throws IOException {
		IFile result = null;

		synchronized (this.allResourcesMap) {
			IResource iResource = this.allResourcesMap.get(fullpath);
			if (iResource != null) {
				// IResource exists
				if (iResource instanceof IFolder) {
					File fsFile = getUnderlyingFileFromFileSystem(fullpath);
					if (fsFile.isDirectory()) {
						throw new IOException(fullpath.getPathString() + " already exists, but is a directory.");
					}

					// clean up invalid record
					// - this should not happen, since resource and fullpath are put to fullpathToResourceTable only when underlying resources are created.
					// - see createUnderlyingFile() and createUnderlyingFolder() methods.
					if (!fsFile.exists()) {
						this.allResourcesMap.remove(fullpath);
					}

				} else if (iResource instanceof IFile) {
					result = (IFile) iResource;
				}
			}

			if (result == null) {
				// - do not put new resource and fullpath to the fullpathToResourceTable
				// - resource and fullpath are put to fullpathToResourceTable only when underlying resources are created.
				// - see createUnderlyingFile() and createUnderlyingFolder() methods.
				result = ResourcesFactory.getInstance().createFile(this, fullpath);
			}
		}

		return result;
	}

	@Override
	public IFolder getFolder(IPath fullpath) throws IOException {
		IFolder result = null;

		synchronized (this.allResourcesMap) {
			IResource iResource = this.allResourcesMap.get(fullpath);

			if (iResource != null) {
				if (iResource instanceof IFolder) {
					result = (IFolder) iResource;

				} else if (iResource instanceof IFile) {
					File fsFile = getUnderlyingFileFromFileSystem(fullpath);
					if (fsFile.isFile()) {
						throw new IOException(fullpath.getPathString() + " already exists, but is a file.");
					}

					// clean up invalid record
					// - this should not happen, since resource and fullpath are put to fullpathToResourceTable only when underlying resources are created.
					// - see createUnderlyingFile() and createUnderlyingFolder() methods.
					if (!fsFile.exists()) {
						this.allResourcesMap.remove(fullpath);
					}
				}
			}

			if (result == null) {
				// - do not put new resource and fullpath to the fullpathToResourceTable
				// - resource and fullpath are put to fullpathToResourceTable only when underlying resources are created.
				// - see createUnderlyingFile() and createUnderlyingFolder() methods.
				result = ResourcesFactory.getInstance().createFolder(this, fullpath);
			}
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <FILE extends IFile> FILE getFile(IPath fullpath, Class<FILE> clazz) throws IOException {
		FILE result = null;

		synchronized (this.allResourcesMap) {
			IResource iResource = this.allResourcesMap.get(fullpath);

			if (iResource != null) {
				if (clazz.isAssignableFrom(iResource.getClass())) {
					result = (FILE) iResource;
				} else {
					throw new IOException("Resource " + fullpath.getPathString() + " (" + iResource.getClass().getName() + ") already exists, but is not type of " + clazz.getName() + ".");
				}
			}

			if (result == null) {
				// - do not put new resource and fullpath to the fullpathToResourceTable
				// - resource and fullpath are put to fullpathToResourceTable only when underlying resources are created.
				// - see createUnderlyingFile() and createUnderlyingFolder() methods.
				result = ResourcesFactory.getInstance().createFile(this, fullpath, clazz);
			}
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <FOLDER extends IFolder> FOLDER getFolder(IPath fullpath, Class<FOLDER> clazz) throws IOException {
		FOLDER result = null;

		synchronized (this.allResourcesMap) {
			IResource iResource = this.allResourcesMap.get(fullpath);

			if (iResource != null) {
				if (clazz.isAssignableFrom(iResource.getClass())) {
					result = (FOLDER) iResource;
				} else {
					throw new IOException("Resource " + fullpath.getPathString() + " (" + iResource.getClass().getName() + ") already exists, but is not type of " + clazz.getName() + ".");
				}
			}

			if (result == null) {
				// - do not put new resource and fullpath to the fullpathToResourceTable
				// - resource and fullpath are put to fullpathToResourceTable only when underlying resources are created.
				// - see createUnderlyingFile() and createUnderlyingFolder() methods.
				result = ResourcesFactory.getInstance().createFolder(this, fullpath, clazz);
			}
		}

		return result;
	}

	@Override
	public InputStream getInputStream(IPath fullpath) throws IOException {
		InputStream input = null;
		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null) {
			input = new FileInputStream(fsFile);
		}
		return input;
	}

	@Override
	public OutputStream getOutputStream(IPath fullpath) throws IOException {
		OutputStream output = null;
		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null) {
			output = new FileOutputStream(fsFile);
		}
		return output;
	}

	@Override
	public boolean underlyingResourceExists(IPath fullpath) {
		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null) {
			return fsFile.exists();
		}
		return false;
	}

	@Override
	public boolean createUnderlyingFile(IFile file) throws IOException {
		IPath fullpath = file.getFullPath();

		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null) {
			if (fsFile.exists()) {
				throw new IOException(fullpath.getPathString() + " already exists.");
			}

			boolean created = fsFile.createNewFile();
			if (created) {
				this.allResourcesMap.put(fullpath, file);
			}
			return created;
		}

		return false;
	}

	@Override
	public boolean createUnderlyingFile(IFile file, InputStream input) throws IOException {
		IPath fullpath = file.getFullPath();

		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null) {
			if (fsFile.exists()) {
				throw new IOException(fullpath.getPathString() + " already exists.");
			}

			// Create file in file system if not exist.
			boolean created = fsFile.createNewFile();
			if (created) {
				this.allResourcesMap.put(fullpath, file);
			}

			// Read from input stream and write it to the file.
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(fsFile);
				IOUtil.copyLarge(input, output);

			} finally {
				IOUtil.closeQuietly(output, true);
			}

			return created;
		}

		return false;
	}

	@Override
	public boolean createUnderlyingFolder(IFolder folder) throws IOException {
		IPath fullpath = folder.getFullPath();

		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null) {
			if (fsFile.exists()) {
				throw new IOException(fullpath.getPathString() + " already exists.");
			}

			boolean created = fsFile.mkdirs();
			if (created) {
				this.allResourcesMap.put(fullpath, folder);
			}
			return created;
		}

		return false;
	}

	@Override
	public boolean deleteUnderlyingResource(IPath fullpath) {
		boolean isDeleted = false;

		synchronized (this.allResourcesMap) {
			this.allResourcesMap.remove(fullpath);
		}

		File fsFile = getUnderlyingFileFromFileSystem(fullpath);
		if (fsFile != null && fsFile.exists()) {
			if (fsFile.isDirectory()) {
				try {
					isDeleted = FileUtil.deleteDirectory(fsFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (fsFile.isFile()) {
				isDeleted = fsFile.delete();
			}
		}

		return isDeleted;
	}

	/**
	 * 
	 * @param fullpath
	 * @return
	 */
	protected File getUnderlyingFileFromFileSystem(IPath fullpath) {
		IPath fsRootAbsolutePath = new PathImpl(this.workspaceFsFolder.getAbsolutePath());
		IPath fsFileAbsolutePath = fsRootAbsolutePath.append(fullpath);

		String pathString = fsFileAbsolutePath.getPathString();
		if (pathString != null && pathString.endsWith(PathImpl.SEPARATOR)) {
			pathString = pathString.substring(0, pathString.length() - 1);
		}

		File fsFile = new File(pathString);
		return fsFile;
	}

}

/// **
// *
// * @param resource
// * @return
// */
// private File getUnderlyingFile(IResource resource) {
// File file = null;
// if (resource != null) {
// IPath fullPath = resource.getFullPath();
// IPath absolutePath = new PathImpl(new PathImpl(this.rootFolder.getAbsolutePath()), fullPath);
// file = new File(absolutePath.toString());
// }
// return file;
// }

// @Override
// public IFile getFile(IFolder parent, IPath path) {
// File file = getUnderlyingFile(parent);
// if (file != null && path != null) {
// File newFile = new File(file, path.toString());
// return newFileInstance(parent, newFile);
// }
// return null;
// }
//
// @Override
// public IFolder getFolder(IFolder parent, IPath path) {
// File file = getUnderlyingFile(parent);
// if (file != null && path != null) {
// File newFile = new File(file, path.toString());
// return newFolderInstance((IFolder) parent, newFile);
// }
// return null;
// }

// @Override
// public boolean exists(IFolder parent, IPath path) {
// if (parent == null || path == null) {
// File file = getUnderlyingFile(parent);
// if (file != null) {
// return file.exists();
// }
// }
// return false;
// }

// @Override
// @SuppressWarnings("unchecked")
// public <RES> RES[] getRootMembers(Class<RES> clazz) {
// List<RES> result = new ArrayList<RES>();
// IResource[] members = getRootMembers();
// for (IResource member : members) {
// if (clazz.isAssignableFrom(member.getClass())) {
// result.add((RES) member);
// }
// }
// return (RES[]) result.toArray(new Object[result.size()]);
// }
//
// @Override
// @SuppressWarnings("unchecked")
// public <RES> RES[] getMembers(IFolder parent, Class<RES> clazz) {
// List<RES> result = new ArrayList<RES>();
// IResource[] members = getMembers(parent);
// for (IResource member : members) {
// if (clazz.isAssignableFrom(member.getClass())) {
// result.add((RES) member);
// }
// }
// return (RES[]) result.toArray(new Object[result.size()]);
// }

// private synchronized IFile getFileInstance(IFolder parent, File file) {
// IPath fullpath = new PathImpl(parent.getFullPath(), file.getName());
// IResource resource = this.fullpathToResourceTable.get(fullpath);
// if (resource instanceof IFile) {
// return (IFile) resource;
// }
// return null;
// }
//
// private synchronized IFolder getFolderInstance(IFolder parent, File file) {
// IPath fullpath = new PathImpl(parent.getFullPath(), file.getName());
// IResource resource = this.fullpathToResourceTable.get(fullpath);
// if (resource instanceof IFolder) {
// return (IFolder) resource;
// }
// return null;
// }

// private synchronized IFile newFileInstance(IPath fullpath) {
// IFile newFile = null;
// ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
// for (ResourceProvider provider : providers) {
// if (provider.isSupported(this, fullpath)) {
// newFile = provider.newFileInstance(this, fullpath);
// if (newFile != null) {
// break;
// }
// }
// }
//
// // New IFile is not created by providers
// // - Create default IFile
// if (newFile == null) {
// newFile = ResourceFactory.getInstance().createFile(this, fullpath);
// }
//
// this.fullpathToResourceTable.put(fullpath, newFile);
// return newFile;
// }
//
// private synchronized IFolder newFolderInstance(IPath fullpath) {
// IFolder newFolder = null;
//
// // Ask providers to create new IFolder
// ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
// for (ResourceProvider provider : providers) {
// if (provider.isSupported(this, fullpath)) {
// newFolder = provider.newFolderInstance(this, fullpath);
// if (newFolder != null) {
// break;
// }
// }
// }
//
// // New IFolder is not created by providers
// // - Create default IFolder
// if (newFolder == null) {
// newFolder = ResourceFactory.getInstance().createFolder(this, fullpath);
// }
//
// this.fullpathToResourceTable.put(fullpath, newFolder);
// return newFolder;
// }
//
// private synchronized <FILE extends IFile> FILE newFileInstance(IPath fullpath, Class<FILE> clazz) {
// FILE newFILE = null;
//
// // Ask providers to create new FILE
// ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
// for (ResourceProvider provider : providers) {
// if (provider.isSupported(this, fullpath, clazz)) {
// newFILE = provider.newFileInstance(this, fullpath, clazz);
// if (newFILE != null) {
// break;
// }
// }
// }
// if (newFILE == null) {
// throw new RuntimeException("Class \"" + clazz.getName() + "\" is not supported by ResourceProviders for creating new file instance.");
// }
//
// this.fullpathToResourceTable.put(fullpath, newFILE);
//
// return newFILE;
// }
//
// private synchronized <FOLDER extends IFolder> FOLDER newFolderInstance(IPath fullpath, Class<FOLDER> clazz) {
// FOLDER newFOLDER = null;
//
// // Ask providers to create new FOLDER
// ResourceProvider[] providers = ResourceProviderRegistry.getInstance().getResourceProviders();
// for (ResourceProvider provider : providers) {
// if (provider.isSupported(this, fullpath, clazz)) {
// newFOLDER = provider.newFolderInstance(this, fullpath, clazz);
// if (newFOLDER != null) {
// break;
// }
// }
// }
// if (newFOLDER == null) {
// throw new RuntimeException("Class \"" + clazz.getName() + "\" is not supported by ResourceProviders for creating new folder instance.");
// }
//
// this.fullpathToResourceTable.put(fullpath, newFOLDER);
//
// return newFOLDER;
// }

// @Override
// public Object getUnderlyingResource(IPath fullpath) {
// return getUnderlyingFsFile(fullpath);
// }
