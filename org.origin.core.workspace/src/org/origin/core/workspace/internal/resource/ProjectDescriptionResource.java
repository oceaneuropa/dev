package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.origin.common.resource.AbstractResource;
import org.origin.core.workspace.IProjectDescription;

public class ProjectDescriptionResource extends AbstractResource {

	/**
	 * 
	 * @param uri
	 */
	public ProjectDescriptionResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		ProjectDescriptionReader reader = new ProjectDescriptionReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		ProjectDescriptionWriter writer = new ProjectDescriptionWriter();
		writer.write(this, output);
	}

	public IProjectDescription getProjectDescription() {
		return !getContents().isEmpty() && getContents().get(0) instanceof IProjectDescription ? (IProjectDescription) getContents().get(0) : null;
	}

}
