package org.orbit.infra.model.repo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.RepoConstants;
import org.orbit.infra.model.repo.Repos;
import org.orbit.infra.model.repo.impl.RepoConfigImpl;
import org.orbit.infra.model.repo.impl.ReposImpl;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.ReaderImpl;

/**
 * Read repos.json file into Repos model.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ReposReader extends ReaderImpl {

	protected Repos repos = null;

	public ReposReader() {
	}

	public Repos getRepos() {
		return this.repos;
	}

	/**
	 * 
	 * @param inputStream
	 * @throws IOException
	 */
	public void read(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			return;
		}
		JSONObject document = JSONUtil.load(inputStream, false);
		if (document != null) {
			this.repos = documentToContents(document);
		}
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	protected Repos documentToContents(JSONObject document) {
		if (document == null) {
			return null;
		}

		Repos repos = new ReposImpl();

		List<RepoConfig> repoList = repos.getRepoConfigs();

		// "Repos" array
		if (document.has(RepoConstants.REPOS)) {
			JSONArray reposJSONArray = document.getJSONArray(RepoConstants.REPOS);
			if (reposJSONArray != null) {
				int length = reposJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject repoJSONObject = reposJSONArray.getJSONObject(i);
					if (repoJSONObject != null) {
						RepoConfig repo = jsonToRepo(repoJSONObject);
						if (repo != null) {
							repoList.add(repo);
						}
					}
				}
			}
		}

		return repos;
	}

	/**
	 * Converts JSON object to a Repo object.
	 * 
	 * @param repoJSONObject
	 * @return
	 */
	protected RepoConfig jsonToRepo(JSONObject repoJSONObject) {
		if (repoJSONObject == null) {
			return null;
		}

		RepoConfig repo = new RepoConfigImpl();

		// id
		if (repoJSONObject.has(RepoConstants.REPO__ID)) {
			String id = repoJSONObject.getString(RepoConstants.REPO__ID);
			repo.setId(id);
		}

		// type
		if (repoJSONObject.has(RepoConstants.REPO__TYPE)) {
			String type = repoJSONObject.getString(RepoConstants.REPO__TYPE);
			repo.setType(type);
		}

		// server.url
		if (repoJSONObject.has(RepoConstants.REPO__SERVER_URL)) {
			String serverUrl = repoJSONObject.getString(RepoConstants.REPO__SERVER_URL);
			repo.setServerUrl(serverUrl);
		}

		// client.url
		if (repoJSONObject.has(RepoConstants.REPO__CLIENT_URL)) {
			String clientUrl = repoJSONObject.getString(RepoConstants.REPO__CLIENT_URL);
			repo.setClientUrl(clientUrl);
		}

		// username
		if (repoJSONObject.has(RepoConstants.REPO__USERNAME)) {
			String username = repoJSONObject.getString(RepoConstants.REPO__USERNAME);
			repo.setUsername(username);
		}

		// properties
		Map<String, String> properties = new HashMap<String, String>();
		String[] propNames = JSONObject.getNames(repoJSONObject);
		if (propNames != null) {
			for (String propName : propNames) {
				if (RepoConstants.REPO__ID.equals(propName) //
						|| RepoConstants.REPO__TYPE.equals(propName) //
						|| RepoConstants.REPO__SERVER_URL.equals(propName) //
						|| RepoConstants.REPO__CLIENT_URL.equals(propName) //
						|| RepoConstants.REPO__USERNAME.equals(propName) //
				) {
					continue;
				}
				String currPropValue = repoJSONObject.getString(propName);
				if (currPropValue != null) {
					properties.put(propName, currPropValue);
				}
			}
		}
		repo.getProperties().putAll(properties);

		return repo;
	}

}
