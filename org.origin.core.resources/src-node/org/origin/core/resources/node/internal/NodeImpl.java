package org.origin.core.resources.node.internal;

import java.io.IOException;

import org.origin.core.resources.IPath;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.impl.FolderImpl;
import org.origin.core.resources.node.INode;
import org.origin.core.resources.node.NodeDescription;
import org.origin.core.resources.node.internal.misc.NodeDescriptionPersistence;

public class NodeImpl extends FolderImpl implements INode {

	protected NodeDescription desc;

	/**
	 * 
	 * @param root
	 * @param fullpath
	 */
	public NodeImpl(IWorkspace root, IPath fullpath) {
		super(root, fullpath);
	}

	@Override
	public synchronized boolean create() throws IOException {
		String name = getName();
		return create(new NodeDescription(name, name));
	}

	@Override
	public synchronized boolean create(NodeDescription desc) throws IOException {
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
	public synchronized void setDescription(NodeDescription desc) throws IOException {
		this.desc = desc;
		NodeDescriptionPersistence.getInstance().save(this, desc);
	}

	@Override
	public synchronized NodeDescription getDescription() throws IOException {
		if (this.desc == null) {
			this.desc = NodeDescriptionPersistence.getInstance().load(this);
		}
		return this.desc;
	}

}
