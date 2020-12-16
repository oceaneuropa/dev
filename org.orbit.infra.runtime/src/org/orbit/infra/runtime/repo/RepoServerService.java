package org.orbit.infra.runtime.repo;

import java.io.IOException;

import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.Repos;
import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.service.WebServiceAware;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface RepoServerService extends WebServiceAware, EditPoliciesAware {

	/**
	 * Get user's Repos.
	 * 
	 * @param username
	 * @return
	 * @throws IOException
	 */
	Repos getRepos(String username) throws IOException;

	/**
	 * Get a user's RepoConfig by id.
	 * 
	 * @param username
	 * @param repoConfigId
	 * @return
	 * @throws IOException
	 */
	RepoConfig getRepoConfig(String username, String repoConfigId) throws IOException;

}
