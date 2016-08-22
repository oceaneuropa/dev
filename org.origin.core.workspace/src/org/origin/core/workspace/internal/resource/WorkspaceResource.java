package org.origin.core.workspace.internal.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.origin.common.resource.AbstractResource;

public class WorkspaceResource extends AbstractResource {

	/**
	 * 
	 * @param uri
	 */
	public WorkspaceResource(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		WorkspaceReader reader = new WorkspaceReader();
		reader.read(this, input);
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		WorkspaceWriter writer = new WorkspaceWriter();
		writer.write(this, output);
	}

	public WorkspaceDescription getWorkspace() {
		return !getContents().isEmpty() && getContents().get(0) instanceof WorkspaceDescription ? (WorkspaceDescription) getContents().get(0) : null;
	}

}
