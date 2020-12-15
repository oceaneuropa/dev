package org.orbit.infra.runtime.repo.impl;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.RepoConstants;
import org.orbit.infra.model.repo.Repos;
import org.orbit.infra.model.repo.impl.ReposImpl;
import org.orbit.infra.model.repo.util.RepoReader;
import org.orbit.infra.model.repo.util.ReposReader;
import org.orbit.infra.runtime.repo.RepoServerService;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoServerServiceImpl implements RepoServerService {

	protected String name;
	protected String hostURL;
	protected String contextRoot;

	protected File reposFolder;
	protected File reposFile;
	protected Repos reposObj;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;

	/**
	 * 
	 * @param folderLocation
	 */
	public RepoServerServiceImpl(String folderLocation) {
		this.reposFolder = new File(folderLocation);
		this.reposFile = new File(this.reposFolder, RepoConstants.REPOS_FILE_NAME);
	}

	protected void checkRepos() {
		if (this.reposObj == null) {
			throw new IllegalStateException("Repos is not created. Call RepoService.start() to initialize Repos.");
		}
	}

	/** WebServiceAware interface */
	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	@Override
	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	/** EditPoliciesAwareService interface */
	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	/** LifecycleAware interface */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
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

		// 3. Create EditPolicies
		this.wsEditPolicies = new ServiceEditPoliciesImpl(RepoServerService.class, this);

		// 4. Register RepoClientService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(RepoServerService.class, this, props);
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
	public void stop(BundleContext bundleContext) throws Exception {
		// Unregister RepoClientService
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		// Dispose EditPolicies
		this.wsEditPolicies = null;

		// Dispose data
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

}
