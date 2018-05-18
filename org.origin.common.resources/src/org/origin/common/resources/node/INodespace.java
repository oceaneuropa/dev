package org.origin.common.resources.node;

import java.io.IOException;

import org.origin.common.resources.IFolder;

public interface INodespace extends IFolder {

	boolean create(NodespaceDescription desc) throws IOException;

	void setDescription(NodespaceDescription desc) throws IOException;

	NodespaceDescription getDescription() throws IOException;

}
