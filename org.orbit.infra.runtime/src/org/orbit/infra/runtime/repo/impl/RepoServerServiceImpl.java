package org.orbit.infra.runtime.repo.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.RepoConstants;
import org.orbit.infra.model.repo.Repos;
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
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies editPolicies;

	protected File rootFolder;
	protected Map<String, Repos> userToReposMap = new HashMap<String, Repos>();

	/**
	 * 
	 * @param rootFolderLocation
	 */
	public RepoServerServiceImpl(String rootFolderLocation) {
		this.rootFolder = new File(rootFolderLocation);
	}

	/** IWebService */
	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getHostURL() {
		return this.hostURL;
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

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.editPolicies;
	}

	/** ILifecycle */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		// Create EditPolicies
		this.editPolicies = new ServiceEditPoliciesImpl(RepoServerService.class, this);

		// Register RepoClientService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(RepoServerService.class, this, props);
	}

	/*-
	 * 
	 * @param repoConfig
	 * @throws IOException
	 *
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
	*/

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// Unregister RepoClientService
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		// Dispose EditPolicies
		this.editPolicies = null;

		// Dispose data
		this.userToReposMap.clear();
	}

	@Override
	public synchronized Repos getRepos(String username) {
		Repos userRepos = this.userToReposMap.get(username);

		if (userRepos == null) {
			File userReposFolder = new File(this.rootFolder, username);
			File userReposFile = new File(userReposFolder, RepoConstants.REPOS_FILE_NAME);
			if (userReposFile.exists()) {
				/*-
				try {
					ReposReader reader = new ReposReader();
					reader.read(userReposFile);
					userRepos = reader.getRepos();
				
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
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
				*/
			}
		}

		return userRepos;
	}

	@Override
	public RepoConfig getRepoConfig(String username, String id) {
		RepoConfig repoConfig = null;
		if (id != null) {
			/*-
			List<RepoConfig> repoConfigs = this.reposObj.getRepoConfigs();
			for (RepoConfig currRepoConfig : repoConfigs) {
				String currId = currRepoConfig.getId();
				if (id.equals(currId)) {
					repoConfig = currRepoConfig;
					break;
				}
			}
			*/
		}
		return repoConfig;
	}

}
