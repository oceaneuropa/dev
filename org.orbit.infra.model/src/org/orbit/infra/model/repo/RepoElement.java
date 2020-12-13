package org.orbit.infra.model.repo;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface RepoElement {

	void setRepo(Repo repo);

	Repo getRepo();

	void setParentId(String parentId);

	String getParentId();

	void setId(String id);

	String getId();

	void setRevision(long revision);

	long getRevision();

}
