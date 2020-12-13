package org.orbit.infra.runtime.repo;

import java.io.IOException;
import java.util.Map;

import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.Repos;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface RepoService {

	void start() throws IOException;

	void stop() throws IOException;

	Repos getRepos();

	RepoConfig getRepoConfig(String id);

	/**
	 * Create a new repository instance. Add repository configuration to {repo_home}/repos.json file.
	 * 
	 * @param type
	 * @param clientUrl
	 * @param serverUrl
	 * @param username
	 * @param properties
	 * @return
	 */
	RepoConfig create(String type, String serverUrl, String clientUrl, String username, Map<String, String> properties) throws IOException;

	/**
	 * Checkout repo elements. Get elements metadata from remote server and store it to {repo_home}/{repoId}/repo.json file.
	 * 
	 * The domain data of the elements are stored by RepoHandler. Impl of RepoHandler need to provide implementation for it.
	 * 
	 * @param repoConfig
	 */
	void checkout(RepoConfig repoConfig) throws IOException;

	/**
	 * Update repo element. Use remote element data to update local element data. If local element doesn't exist, create local element.
	 * 
	 * Save element metadata to {repo_home}/{repoId}/repo.json file.
	 * 
	 * @param repoConfig
	 * @param elementId
	 */
	void update(RepoConfig repoConfig, String elementId);

	/**
	 * Check in repo element. Use local element data to update remote element data. If remote element doesn't exist, create remote element.
	 * 
	 * @param repoConfig
	 */
	void checkin(RepoConfig repoConfig, String elementId);

	/**
	 * Delete a repository. Delete repository configuration from {repo_home}/repos.json file.
	 * 
	 * Delete {repo_home}/{repoId}/repo.json file and {repo_home}/{repoId} folder.
	 * 
	 * @param repoConfig
	 */
	void delete(RepoConfig repoConfig);

	/**
	 * 
	 * @param repoConfig
	 */
	void subscribe(RepoConfig repoConfig);

	/**
	 * 
	 * @param repoConfig
	 */
	void unsubscribe(RepoConfig repoConfig);

}
