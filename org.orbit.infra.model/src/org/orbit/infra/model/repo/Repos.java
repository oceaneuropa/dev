package org.orbit.infra.model.repo;

import java.util.List;

public interface Repos {

	List<RepoConfig> getRepoConfigs();

	void clear();

}
