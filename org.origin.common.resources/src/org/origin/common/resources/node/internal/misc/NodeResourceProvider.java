package org.origin.common.resources.node.internal.misc;

import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.INodespace;
import org.origin.common.resources.node.internal.NodeImpl;
import org.origin.common.resources.node.internal.NodespaceImpl;
import org.origin.common.resources.util.ResourceProvider;
import org.origin.common.resources.util.ResourceProviderExtensionRegistry;

public class NodeResourceProvider implements ResourceProvider {

	public static NodeResourceProvider INSTANCE = new NodeResourceProvider();
	public static String ID = "NodeResourceProvider";

	public static void register() {
		ResourceProviderExtensionRegistry.getInstance().register(INSTANCE);
	}

	public static void unregister() {
		ResourceProviderExtensionRegistry.getInstance().unregister(INSTANCE);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean isSupported(IWorkspace root, IPath fullpath) {
		IPath dotNodespaceFullpath = fullpath.append(".metadata").append(".nodespace");
		if (root.underlyingResourceExists(dotNodespaceFullpath)) {
			return true;
		}

		IPath dotNodeFullpath = fullpath.append(".metadata").append(".node");
		if (root.underlyingResourceExists(dotNodeFullpath)) {
			return true;
		}

		return false;
	}

	@Override
	public IFile newFileInstance(IWorkspace root, IPath fullpath) {
		return null;
	}

	@Override
	public IFolder newFolderInstance(IWorkspace root, IPath fullpath) {
		IFolder newFolderInstance = null;

		IPath dotNodespaceFullpath = fullpath.append(".metadata").append(".nodespace");
		if (root.underlyingResourceExists(dotNodespaceFullpath)) {
			newFolderInstance = new NodespaceImpl(root, fullpath);
		}

		if (newFolderInstance == null) {
			IPath dotNodeFullpath = fullpath.append(".metadata").append(".node");
			if (root.underlyingResourceExists(dotNodeFullpath)) {
				newFolderInstance = new NodeImpl(root, fullpath);
			}
		}

		return newFolderInstance;
	}

	@Override
	public <RESOURCE extends IResource> boolean isSupported(IWorkspace root, IPath fullpath, Class<RESOURCE> clazz) {
		if (INodespace.class.equals(clazz) || INode.class.equals(clazz)) {
			return true;
		}
		return false;
	}

	@Override
	public <FILE extends IFile> FILE newFileInstance(IWorkspace root, IPath fullpath, Class<FILE> clazz) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <FOLDER extends IFolder> FOLDER newFolderInstance(IWorkspace root, IPath fullpath, Class<FOLDER> clazz) {
		FOLDER newFolderInstance = null;

		if (INodespace.class.equals(clazz)) {
			newFolderInstance = (FOLDER) new NodespaceImpl(root, fullpath);
		}

		if (INode.class.equals(clazz)) {
			newFolderInstance = (FOLDER) new NodeImpl(root, fullpath);
		}

		return newFolderInstance;
	}

	@Override
	public boolean isSupported(IWorkspace root, IPath fullpath, String className) {
		if (INodespace.class.getName().equals(className) || NodespaceImpl.class.getName().equals(className)) {
			return true;
		}

		if (INode.class.getName().equals(className) || NodeImpl.class.getName().equals(className)) {
			return true;
		}

		return false;
	}

	@Override
	public IFile newFileInstance(IWorkspace root, IPath fullpath, String className) {
		return null;
	}

	@Override
	public IFolder newFolderInstance(IWorkspace root, IPath fullpath, String className) {
		IFolder newFolderInstance = null;

		if (INodespace.class.getName().equals(className) || NodespaceImpl.class.getName().equals(className)) {
			newFolderInstance = new NodespaceImpl(root, fullpath);
		}

		if (INode.class.getName().equals(className) || NodeImpl.class.getName().equals(className)) {
			newFolderInstance = new NodeImpl(root, fullpath);
		}

		return newFolderInstance;
	}

}

// Object underlyingResource = root.getUnderlyingResource(fullpath);
// if (underlyingResource instanceof File) {
// File fsFile = (File) underlyingResource;
// if (NodespaceHelper.getInstance().isNodespace(fsFile) || NodespaceHelper.getInstance().isNode(fsFile)) {
// return true;
// }
// }

// Object underlyingResource = root.getUnderlyingResource(fullpath);
// if (underlyingResource instanceof File) {
// File fsFile = (File) underlyingResource;
// if (NodespaceHelper.getInstance().isNodespace(fsFile)) {
// newFolderInstance = new NodespaceImpl(root, fullpath);
// } else if (NodespaceHelper.getInstance().isNode(fsFile)) {
// newFolderInstance = new NodeImpl(root, fullpath);
// }
// }