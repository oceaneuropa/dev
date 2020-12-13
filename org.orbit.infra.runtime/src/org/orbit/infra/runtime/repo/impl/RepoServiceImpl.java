package org.orbit.infra.runtime.repo.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.RepoConstants;
import org.orbit.infra.model.repo.Repos;
import org.orbit.infra.model.repo.impl.RepoConfigImpl;
import org.orbit.infra.model.repo.impl.ReposImpl;
import org.orbit.infra.model.repo.util.RepoReader;
import org.orbit.infra.model.repo.util.RepoWriter;
import org.orbit.infra.model.repo.util.ReposReader;
import org.orbit.infra.runtime.repo.RepoHandler;
import org.orbit.infra.runtime.repo.RepoHandlerRegistry;
import org.orbit.infra.runtime.repo.RepoService;
import org.origin.common.util.FileUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoServiceImpl implements RepoService {

	protected File reposFolder;
	protected File reposFile;
	protected Repos reposObj;

	/**
	 * 
	 * @param folderLocation
	 */
	public RepoServiceImpl(String folderLocation) {
		this.reposFolder = new File(folderLocation);
		this.reposFile = new File(this.reposFolder, RepoConstants.REPOS_FILE_NAME);
	}

	protected void checkRepos() {
		if (this.reposObj == null) {
			throw new IllegalStateException("Repos is not created. Call RepoService.start() to initialize Repos.");
		}
	}

	@Override
	public synchronized void start() throws IOException {
		// 1. Load repos metadata from {repos_home}/repos.json file
		this.reposObj = null;
		try {
			ReposReader reader = new ReposReader();
			reader.read(this.reposFile);
			this.reposObj = reader.getRepos();

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (this.reposObj == null) {
			this.reposObj = new ReposImpl();
		}

		// 2. Init repos
		IOException exception = null;
		List<RepoConfig> repoConfigs = this.reposObj.getRepoConfigs();
		for (RepoConfig currRepoConfig : repoConfigs) {
			try {
				initRepo(currRepoConfig);
			} catch (IOException e) {
				e.printStackTrace();
				exception = e;
			}
		}
		if (exception != null) {
			throw exception;
		}
	}

	/**
	 * 
	 * @param repoConfig
	 * @throws IOException
	 */
	protected void initRepo(RepoConfig repoConfig) throws IOException {
		String repoId = repoConfig.getId();
		File repoFolder = new File(this.reposFile, repoId);
		File repoFile = new File(repoFolder, RepoConstants.REPO_FILE_NAME);

		repoConfig.setRepoFolder(repoFolder);
		repoConfig.setRepoFile(repoFile);

		// The {repos_home}/{repoId} folder may not exist.
		// If the folder doesn't exist, ite means the repo is not checked out yet.

		// If {repos_home}/{repoId}/repo.json file exists, load repo data from it.
		// If the file doesn't exist, it means the repo is not checked out yet.
		if (repoFile.exists()) {
			RepoReader repoReader = new RepoReader();
			repoReader.read(repoFile);
			Repo repo = repoReader.getRepo();
			if (repo != null) {
				repoConfig.setRepo(repo);
			}
		}
	}

	@Override
	public synchronized void stop() throws IOException {
		if (this.reposObj != null) {
			this.reposObj.clear();
		}
	}

	@Override
	public synchronized Repos getRepos() {
		return this.reposObj;
	}

	@Override
	public RepoConfig getRepoConfig(String id) {
		checkRepos();

		RepoConfig repoConfig = null;
		if (id != null) {
			List<RepoConfig> repoConfigs = this.reposObj.getRepoConfigs();
			for (RepoConfig currRepoConfig : repoConfigs) {
				String currId = currRepoConfig.getId();
				if (id.equals(currId)) {
					repoConfig = currRepoConfig;
					break;
				}
			}
		}
		return repoConfig;
	}

	@Override
	public synchronized RepoConfig create(String type, String serverUrl, String clientUrl, String username, Map<String, String> properties) throws IOException {
		checkRepos();

		// Create new Repo config
		RepoConfig repoConfig = new RepoConfigImpl();
		repoConfig.setType(type);
		repoConfig.setServerUrl(serverUrl);
		repoConfig.setUsername(username);
		if (properties != null) {
			repoConfig.getProperties().putAll(properties);
		}

		int nextId = getNextId();
		repoConfig.setId(String.valueOf(nextId));

		// Create {repos_home}/{repoId} folder
		createRepo(repoConfig);

		// Add to repos
		this.reposObj.getRepoConfigs().add(repoConfig);

		// Data are not checked out at yet.

		return repoConfig;
	}

	protected synchronized int getNextId() {
		int maxId = 0;

		List<RepoConfig> repoList = this.reposObj.getRepoConfigs();
		for (RepoConfig repo : repoList) {
			try {
				int currId = Integer.parseInt(repo.getId());
				if (currId > maxId) {
					maxId = currId;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (maxId + 1);
	}

	/**
	 * 
	 * @param repoConfig
	 * @throws IOException
	 */
	protected void createRepo(RepoConfig repoConfig) throws IOException {
		String id = repoConfig.getId();

		// Purge {repo_home}/{repoId} folder
		File repoFolder = new File(this.reposFolder, id);
		if (repoFolder.exists()) {
			FileUtil.deleteDirectory(repoFolder);
		}

		// Create {repo_home}/{repoId} folder
		if (!repoFolder.exists()) {
			repoFolder.mkdirs();
		}
	}

	/**
	 * 
	 * @param repoConfig
	 */
	protected void checkRepo(RepoConfig repoConfig) {
		String id = repoConfig.getId();

		// Check {repo_home}/{repoId} folder exists
		File repoFolder = new File(this.reposFolder, id);
		if (!repoFolder.exists()) {
			throw new IllegalStateException("Repo folder '" + repoFolder.getAbsolutePath() + "' doesn't exist.");
		}
	}

	protected void checkHandler(String type, RepoHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("RepoHandler for '" + type + "' is not found.");
		}
	}

	@Override
	public synchronized void checkout(RepoConfig repoConfig) throws IOException {
		checkRepo(repoConfig);

		String type = repoConfig.getType();
		RepoHandler handler = RepoHandlerRegistry.getInstsance().getRepoHandler(type);
		checkHandler(type, handler);

		Repo repo = handler.checkout(repoConfig);
		if (repo != null) {
			repoConfig.setRepo(repo);

			// Create {repo_home}/{repoId}/repo.json file
			File repoFolder = repoConfig.getRepoFolder();
			File repoFile = new File(repoFolder, RepoConstants.REPO_FILE_NAME);
			RepoWriter writer = new RepoWriter(repo);
			writer.write(repoFile);
		}
	}

	@Override
	public synchronized void update(RepoConfig repoConfig, String elementId) {
	}

	@Override
	public synchronized void checkin(RepoConfig repoConfig, String elementId) {
		checkRepo(repoConfig);

		String type = repoConfig.getType();
		RepoHandler handler = RepoHandlerRegistry.getInstsance().getRepoHandler(type);
		checkHandler(type, handler);

		handler.checkin(repoConfig);
	}

	@Override
	public synchronized void delete(RepoConfig repoConfig) {
		// Delete {repo_home}/{repoId}/repo.json file and {repo_home}/{repoId} folder.

		// Delete repository configuration from {repo_home}/repos.json file.

		// Save {repo_home}/repos.json file.
	}

	@Override
	public void subscribe(RepoConfig repoConfig) {
	}

	@Override
	public void unsubscribe(RepoConfig repoConfig) {
	}

}
