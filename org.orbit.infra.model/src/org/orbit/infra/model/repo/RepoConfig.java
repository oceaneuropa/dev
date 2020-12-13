package org.orbit.infra.model.repo;

import java.io.File;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface RepoConfig {

	void setRepoFolder(File repoFolder);

	File getRepoFolder();

	void setRepoFile(File repoFile);

	File getRepoFile();

	void setId(String id);

	String getId();

	void setType(String type);

	String getType();

	void setServerUrl(String serverUrl);

	String getServerUrl();

	void setClientUrl(String clientUrl);

	String getClientUrl();

	void setUsername(String username);

	String getUsername();

	Map<String, String> getProperties();

	void setRepo(Repo repo);

	Repo getRepo();

}