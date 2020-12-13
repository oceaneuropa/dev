package org.orbit.infra.model.repo.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.orbit.infra.model.repo.Repos;
import org.orbit.infra.model.repo.util.ReposReader;
import org.orbit.infra.model.repo.util.ReposWriter;
import org.origin.common.resource.impl.ResourceImpl;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ReposResource extends ResourceImpl {

	/**
	 * 
	 * @param file
	 */
	public ReposResource(File file) {
		super(file);
	}

	/**
	 * 
	 * @param uri
	 */
	public ReposResource(URI uri) {
		super(uri);
	}

	public Repos getRepos() {
		Repos repos = null;
		List<Object> contents = getContents();
		for (Object content : contents) {
			if (content instanceof Repos) {
				repos = (Repos) content;
				break;
			}
		}
		return repos;
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		getContents().clear();

		ReposReader reader = new ReposReader();
		reader.read(input);

		Repos repos = reader.getRepos();
		if (repos != null) {
			getContents().add(repos);
		}
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		Repos repos = getRepos();
		if (repos != null) {
			ReposWriter writer = new ReposWriter(repos);
			writer.write(output);
		}
	}

}
