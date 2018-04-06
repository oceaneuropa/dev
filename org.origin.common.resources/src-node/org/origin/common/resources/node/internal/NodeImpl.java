package org.origin.common.resources.node.internal;

import java.io.IOException;

import org.origin.common.resources.IPath;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.impl.FolderImpl;
import org.origin.common.resources.node.INode;
import org.origin.common.resources.node.NodeDescription;
import org.origin.common.resources.node.internal.misc.NodeDescriptionPersistence;

public class NodeImpl extends FolderImpl implements INode {

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
		return create(new NodeDescription(name));
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
		return (NodeDescription) this.desc;
	}

}
