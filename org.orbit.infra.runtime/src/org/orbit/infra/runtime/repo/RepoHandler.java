package org.orbit.infra.runtime.repo;

import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoConfig;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface RepoHandler {

	String getType();

	Repo checkout(RepoConfig repoConfig);

	void checkin(RepoConfig repoConfig);

}
