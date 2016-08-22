package org.nb.home.model.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.nb.home.model.runtime.config.ProjectConfig;
import org.origin.common.resource.AbstractResource;

public class ProjectResource extends AbstractResource {

	/**
	 * 
	 * @param uri
	 */
	public ProjectResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		ProjectReader reader = new ProjectReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		ProjectWriter writer = new ProjectWriter();
		writer.write(this, output);
	}

	public ProjectConfig getProject() {
		return !getContents().isEmpty() && getContents().get(0) instanceof ProjectConfig ? (ProjectConfig) getContents().get(0) : null;
	}

}
