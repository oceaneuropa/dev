package org.origin.common.resources.node.internal;

import java.io.IOException;

import org.origin.common.resources.IPath;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.impl.FolderImpl;
import org.origin.common.resources.node.INodespace;
import org.origin.common.resources.node.NodespaceDescription;
import org.origin.common.resources.node.internal.misc.NodespaceDescriptionPersistence;

public class NodespaceImpl extends FolderImpl implements INodespace {

	/**
	 * 
	 * @param parent
	 * @param name
	 */
	public NodespaceImpl(IWorkspace root, IPath fullpath) {
		super(root, fullpath);
	}

	@Override
	public synchronized boolean create() throws IOException {
		String name = getName();
		return create(new NodespaceDescription(name, name));
	}

	@Override
	public synchronized boolean create(NodespaceDescription desc) throws IOException {
		if (exists()) {
			return false;
		}
		boolean succeed = super.create();
		if (succeed) {
			setDescription(desc);
		}
		return succeed;
	}

	@Override
	public synchronized void setDescription(NodespaceDescription desc) throws IOException {
		this.desc = desc;
		NodespaceDescriptionPersistence.getInstance().save(this, desc);
	}

	@Override
	public synchronized NodespaceDescription getDescription() throws IOException {
		if (this.desc == null) {
			this.desc = NodespaceDescriptionPersistence.getInstance().load(this);
		}
		return (NodespaceDescription) this.desc;
	}

}
