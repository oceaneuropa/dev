package org.orbit.infra.model.repo.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.util.RepoReader;
import org.orbit.infra.model.repo.util.RepoWriter;
import org.origin.common.resource.impl.ResourceImpl;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoResource extends ResourceImpl {

	/**
	 * 
	 * @param file
	 */
	public RepoResource(File file) {
		super(file);
	}

	/**
	 * 
	 * @param uri
	 */
	public RepoResource(URI uri) {
		super(uri);
	}

	public Repo getRepo() {
		Repo repo = null;
		List<Object> contents = getContents();
		for (Object content : contents) {
			if (content instanceof Repo) {
				repo = (Repo) content;
				break;
			}
		}
		return repo;
	}

	@Override
	protected void doLoad(InputStream input) throws IOException {
		getContents().clear();

		RepoReader reader = new RepoReader();
		reader.read(input);

		Repo repo = reader.getRepo();
		if (repo != null) {
			getContents().add(repo);
		}
	}

	@Override
	protected void doSave(OutputStream output) throws IOException {
		Repo repo = getRepo();
		if (repo != null) {
			RepoWriter writer = new RepoWriter(repo);
			writer.write(output);
		}
	}

}
