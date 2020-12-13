package org.orbit.infra.model.repo.impl;

import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoElement;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoElementImpl implements RepoElement {

	protected Repo repo;
	protected String parentId;
	protected String id;
	protected long revision;

	public RepoElementImpl() {
	}

	/**
	 * 
	 * @param repo
	 */
	public RepoElementImpl(Repo repo) {
		this.repo = repo;
	}

	@Override
	public Repo getRepo() {
		return this.repo;
	}

	@Override
	public void setRepo(Repo repo) {
		this.repo = repo;
	}

	@Override
	public String getParentId() {
		return this.parentId;
	}

	@Override
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public long getRevision() {
		return this.revision;
	}

	@Override
	public void setRevision(long revision) {
		this.revision = revision;
	}

	@Override
	public String toString() {
		return "RepoElementImpl [parentId=" + parentId + ", id=" + id + ", revision=" + revision + "]";
	}

}
