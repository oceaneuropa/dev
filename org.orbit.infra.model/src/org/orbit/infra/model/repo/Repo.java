package org.orbit.infra.model.repo;

import java.util.List;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface Repo {

	List<RepoElement> getElements();

	RepoElement getElement(String elementId);

	boolean addElements(List<RepoElement> elements);

	boolean addElement(RepoElement element);

	boolean removeElement(RepoElement element);

	void clear();

}
