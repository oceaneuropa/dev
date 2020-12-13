package org.orbit.infra.model.repo.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.model.repo.RepoConfig;
import org.orbit.infra.model.repo.Repos;

public class ReposImpl implements Repos {

	protected List<RepoConfig> repos = new ArrayList<RepoConfig>();

	@Override
	public synchronized List<RepoConfig> getRepoConfigs() {
		return this.repos;
	}

	@Override
	public synchronized void clear() {
		this.repos.clear();
	}

}
