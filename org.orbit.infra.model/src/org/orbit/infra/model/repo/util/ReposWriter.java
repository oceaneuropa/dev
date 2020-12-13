package org.orbit.infra.model.repo.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.RepoConstants;
import org.orbit.infra.model.repo.Repos;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.WriterImpl;

/**
 * Write Repos model into output.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ReposWriter extends WriterImpl {

	protected Repos repos;

	/**
	 * 
	 * @param repos
	 */
	public ReposWriter(Repos repos) {
		this.repos = repos;
	}

	public Repos getRepos() {
		return this.repos;
	}

	public void setRepos(Repos repos) {
		this.repos = repos;
	}

	protected void checkData() {
		if (this.repos == null) {
			throw new IllegalArgumentException("Repos is null.");
		}
	}

	/**
	 * 
	 * @param output
	 * @throws IOException
	 */
	@Override
	public void write(OutputStream output) throws IOException {
		if (output == null) {
			throw new IllegalArgumentException("OutputStream is null.");
		}
		checkData();

		JSONObject document = contentsToDocument(this.repos);
		if (document != null) {
			JSONUtil.save(document, output, false);
		}
	}

	/**
	 * 
	 * @param repoService
	 * @return
	 */
	protected JSONObject contentsToDocument(Repos repos) {
		JSONObject document = new JSONObject();

		// "Repos" array
		JSONArray reposJSONArray = new JSONArray();

		int index = 0;
		List<RepoConfig> repoList = repos.getRepoConfigs();
		if (repoList != null) {
			for (RepoConfig repo : repoList) {
				JSONObject repoJSONObject = repoToJSON(repo);
				if (repoJSONObject != null) {
					reposJSONArray.put(index++, repoJSONObject);
				}
			}
		}

		document.put(RepoConstants.REPOS, reposJSONArray);

		return document;
	}

	/**
	 * 
	 * @param repo
	 * @return
	 */
	protected JSONObject repoToJSON(RepoConfig repo) {
		if (repo == null) {
			return null;
		}

		JSONObject repoJSON = new JSONObject();

		// id
		String id = repo.getId();
		if (id != null) {
			repoJSON.put(RepoConstants.REPO__ID, id);
		}

		// type
		String type = repo.getType();
		if (type != null) {
			repoJSON.put(RepoConstants.REPO__TYPE, type);
		}

		// server.url
		String serverUrl = repo.getServerUrl();
		if (serverUrl != null) {
			repoJSON.put(RepoConstants.REPO__SERVER_URL, serverUrl);
		}

		// client.url
		String clientUrl = repo.getClientUrl();
		if (clientUrl != null) {
			repoJSON.put(RepoConstants.REPO__CLIENT_URL, clientUrl);
		}

		// username
		String username = repo.getUsername();
		if (username != null) {
			repoJSON.put(RepoConstants.REPO__USERNAME, username);
		}

		// properties
		Map<String, String> properties = repo.getProperties();
		if (properties != null) {
			Iterator<String> itor = properties.keySet().iterator();
			while (itor.hasNext()) {
				String propName = itor.next();
				String propValue = properties.get(propName);

				if (RepoConstants.REPO__ID.equals(propName) //
						|| RepoConstants.REPO__TYPE.equals(propName) //
						|| RepoConstants.REPO__SERVER_URL.equals(propName) //
						|| RepoConstants.REPO__CLIENT_URL.equals(propName) //
						|| RepoConstants.REPO__USERNAME.equals(propName) //
				) {
					continue;
				}

				repoJSON.put(propName, propValue);
			}
		}
		return repoJSON;
	}

}
