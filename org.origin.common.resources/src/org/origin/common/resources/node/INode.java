package org.origin.common.resources.node;

import java.io.IOException;

import org.origin.common.resources.IFolder;

public interface INode extends IFolder {

	 boolean create(NodeDescription desc) throws IOException;
	
	 void setDescription(NodeDescription desc) throws IOException;
	
	 NodeDescription getDescription() throws IOException;

}
