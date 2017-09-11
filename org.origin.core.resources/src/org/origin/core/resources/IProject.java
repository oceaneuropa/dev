package org.origin.core.resources;

import java.io.IOException;

public interface IProject extends IFolder {

	boolean create(ProjectDescription desc) throws IOException;

	void setDescription(ProjectDescription desc) throws IOException;

	ProjectDescription getDescription() throws IOException;

}
