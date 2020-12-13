package org.orbit.infra.model.repo.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoConstants;
import org.orbit.infra.model.repo.RepoElement;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.WriterImpl;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoWriter extends WriterImpl {

	protected Repo repo;

	/**
	 * 
	 * @param repo
	 */
	public RepoWriter(Repo repo) {
		this.repo = repo;
	}

	public Repo getRepo() {
		return this.repo;
	}

	public void setRepo(Repo repo) {
		this.repo = repo;
	}

	protected void checkData() {
		if (this.repo == null) {
			throw new IllegalArgumentException("Repo is null.");
		}
	}

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	public void write(OutputStream output) throws IOException {
		if (output == null) {
			throw new IllegalArgumentException("OutputStream is null.");
		}
		checkData();

		JSONObject document = contentsToDocument(this.repo);
		if (document != null) {
			JSONUtil.save(document, output, false);
		}
	}

	/**
	 * 
	 * @param repo
	 * @return
	 */
	protected JSONObject contentsToDocument(Repo repo) {
		JSONObject document = new JSONObject();

		// "Elements" array
		JSONArray repoElementsJSONArray = new JSONArray();

		int index = 0;
		List<RepoElement> elements = repo.getElements();
		if (elements != null) {
			for (RepoElement element : elements) {
				JSONObject repoElementJSONObject = repoElementToJSON(element);
				if (repoElementJSONObject != null) {
					repoElementsJSONArray.put(index++, repoElementJSONObject);
				}
			}
		}

		document.put(RepoConstants.REPO_ELEMENTS, repoElementsJSONArray);

		return document;
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	protected JSONObject repoElementToJSON(RepoElement element) {
		if (element == null) {
			return null;
		}

		JSONObject repoJSON = new JSONObject();

		// parentId
		String parentId = element.getParentId();
		if (parentId != null) {
			repoJSON.put(RepoConstants.REPO_ELEMENT__PARENT_ID, parentId);
		}

		// id
		String id = element.getId();
		if (id != null) {
			repoJSON.put(RepoConstants.REPO_ELEMENT__ID, id);
		}

		// revision
		long revision = element.getRevision();
		repoJSON.put(RepoConstants.REPO_ELEMENT__REVISION, revision);

		return repoJSON;
	}

}
