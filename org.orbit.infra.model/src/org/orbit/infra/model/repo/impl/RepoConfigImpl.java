package org.orbit.infra.model.repo.impl;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoConfig;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoConfigImpl implements RepoConfig {

	/*-
	{
		"type": "ConfigRegistry",
		"id": "",
		"name": "",
		"rootElementId": "",
		"server.url": "",
		"client.url": "",
		"username": "",
	}	
	 */

	protected File repoFolder;
	protected File repoFile;
	protected String id;
	protected String type;
	// protected String serverUrl;
	// protected String clientUrl;
	// protected String username;
	protected Map<String, String> properties = new LinkedHashMap<String, String>();
	protected Repo repo;

	public RepoConfigImpl() {
	}

	@Override
	public File getRepoFolder() {
		return this.repoFolder;
	}

	@Override
	public void setRepoFolder(File repoFolder) {
		this.repoFolder = repoFolder;
	}

	@Override
	public File getRepoFile() {
		return this.repoFile;
	}

	@Override
	public void setRepoFile(File repoFile) {
		this.repoFile = repoFile;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	/*-
	@Override
	public String getServerUrl() {
		return this.serverUrl;
	}
	
	@Override
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	@Override
	public String getClientUrl() {
		return this.clientUrl;
	}
	
	@Override
	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	*/

	@Override
	public Map<String, String> getProperties() {
		return this.properties;
	}

	@Override
	public Repo getRepo() {
		return this.repo;
	}

	@Override
	public void setRepo(Repo repo) {
		this.repo = repo;
	}

}
