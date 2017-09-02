package org.origin.core.resources.node;

import java.io.File;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IRoot;
import org.origin.core.resources.ResourceProvider;
import org.origin.core.resources.ResourceProviderRegistry;
import org.origin.core.resources.node.internal.node.NodeImpl;
import org.origin.core.resources.node.internal.node.NodespaceImpl;
import org.origin.core.resources.node.internal.utils.NodespaceHelper;

public class NodeResourceFsProvider implements ResourceProvider {

	public static NodeResourceFsProvider INSTANCE = new NodeResourceFsProvider();
	public static String ID = "NodeResourceProviderIO";

	public static void register() {
		ResourceProviderRegistry.getInstance().register(INSTANCE);
	}

	public static void unregister() {
		ResourceProviderRegistry.getInstance().unregister(INSTANCE);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean isSupported(IRoot root, IPath fullpath) {
		Object underlyingResource = root.getUnderlyingResource(fullpath);
		if (underlyingResource instanceof File) {
			File fsFile = (File) underlyingResource;
			if (NodespaceHelper.getInstance().isNodespace(fsFile) || NodespaceHelper.getInstance().isNode(fsFile)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IFile newFileInstance(IRoot root, IPath fullpath) {
		return null;
	}

	@Override
	public IFolder newFolderInstance(IRoot root, IPath fullpath) {
		IFolder newFolderInstance = null;
		Object underlyingResource = root.getUnderlyingResource(fullpath);
		if (underlyingResource instanceof File) {
			File fsFile = (File) underlyingResource;

			if (NodespaceHelper.getInstance().isNodespace(fsFile)) {
				newFolderInstance = new NodespaceImpl(root, fullpath);

			} else if (NodespaceHelper.getInstance().isNode(fsFile)) {
				newFolderInstance = new NodeImpl(root, fullpath);
			}
		}
		return newFolderInstance;
	}

	@Override
	public <RESOURCE extends IResource> boolean isSupported(IRoot root, IPath fullpath, Class<RESOURCE> clazz) {
		if (INodespace.class.equals(clazz) || INode.class.equals(clazz)) {
			return true;
		}
		return false;
	}

	@Override
	public <FILE extends IFile> FILE newFileInstance(IRoot root, IPath fullpath, Class<FILE> clazz) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <FOLDER extends IFolder> FOLDER newFolderInstance(IRoot root, IPath fullpath, Class<FOLDER> clazz) {
		FOLDER newFolderInstance = null;
		if (INodespace.class.equals(clazz)) {
			newFolderInstance = (FOLDER) new NodespaceImpl(root, fullpath);

		} else if (INode.class.equals(clazz)) {
			newFolderInstance = (FOLDER) new NodeImpl(root, fullpath);
		}
		return newFolderInstance;
	}

}
