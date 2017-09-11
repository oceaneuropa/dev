package org.origin.core.resources.node.internal;

import java.io.IOException;

import org.origin.core.resources.IPath;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.impl.FolderImpl;
import org.origin.core.resources.node.INodespace;
import org.origin.core.resources.node.NodespaceDescription;
import org.origin.core.resources.node.internal.misc.NodespaceDescriptionPersistence;

public class NodespaceImpl extends FolderImpl implements INodespace {

	protected NodespaceDescription desc;

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
		return this.desc;
	}

}
