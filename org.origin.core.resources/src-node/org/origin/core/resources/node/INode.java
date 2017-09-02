package org.origin.core.resources.node;

import java.io.IOException;

import org.origin.core.resources.IFolder;

public interface INode extends IFolder {

	boolean create(NodeDescription desc) throws IOException;

	void setDescription(NodeDescription desc) throws IOException;

	NodeDescription getDescription() throws IOException;

}
