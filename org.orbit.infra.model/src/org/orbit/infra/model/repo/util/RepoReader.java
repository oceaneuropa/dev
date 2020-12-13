package org.orbit.infra.model.repo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoConstants;
import org.orbit.infra.model.repo.RepoElement;
import org.orbit.infra.model.repo.impl.RepoElementImpl;
import org.orbit.infra.model.repo.impl.RepoImpl;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.ReaderImpl;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoReader extends ReaderImpl {

	protected Repo repo;

	public RepoReader() {
	}

	public Repo getRepo() {
		return this.repo;
	}

	/**
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	@Override
	public void read(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return;
		}
		JSONObject document = JSONUtil.load(inputStream, false);
		if (document != null) {
			this.repo = documentToContents(document);
		}
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	protected Repo documentToContents(JSONObject document) {
		if (document == null) {
			return null;
		}

		Repo repo = new RepoImpl();

		List<RepoElement> repoElements = repo.getElements();

		// "Elements" array
		if (document.has(RepoConstants.REPO_ELEMENTS)) {
			JSONArray repoElementsJSONArray = document.getJSONArray(RepoConstants.REPO_ELEMENTS);
			if (repoElementsJSONArray != null) {
				int length = repoElementsJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject repoElementJSONObject = repoElementsJSONArray.getJSONObject(i);
					if (repoElementJSONObject != null) {
						RepoElement repoElement = jsonToRepoElement(repoElementJSONObject);
						if (repoElement != null) {
							repoElements.add(repoElement);
						}
					}
				}
			}
		}

		return repo;
	}

	/**
	 * 
	 * @param repoElementJSONObject
	 * @return
	 */
	protected RepoElement jsonToRepoElement(JSONObject repoElementJSONObject) {
		if (repoElementJSONObject == null) {
			return null;
		}

		RepoElement repoElement = new RepoElementImpl();

		// parentId
		if (repoElementJSONObject.has(RepoConstants.REPO_ELEMENT__PARENT_ID)) {
			String parentId = repoElementJSONObject.getString(RepoConstants.REPO_ELEMENT__PARENT_ID);
			repoElement.setParentId(parentId);
		}

		// id
		if (repoElementJSONObject.has(RepoConstants.REPO_ELEMENT__ID)) {
			String id = repoElementJSONObject.getString(RepoConstants.REPO_ELEMENT__ID);
			repoElement.setId(id);
		}

		// revision
		if (repoElementJSONObject.has(RepoConstants.REPO_ELEMENT__REVISION)) {
			long revision = repoElementJSONObject.getLong(RepoConstants.REPO_ELEMENT__REVISION);
			repoElement.setRevision(revision);
		}

		return repoElement;
	}

}
