package org.orbit.infra.runtime.repo;

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
	 * Get Repos object.
	 * 
	 * @return
	 */
	Repos getRepos();

	/**
	 * Get a RepoConfig by id.
	 * 
	 * @param id
	 * @return
	 */
	RepoConfig getRepoConfig(String id);

}
