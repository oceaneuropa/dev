package org.orbit.infra.model.repo.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.model.repo.Repo;
import org.orbit.infra.model.repo.RepoElement;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoImpl implements Repo {

	protected List<RepoElement> elements = new ArrayList<RepoElement>();

	@Override
	public List<RepoElement> getElements() {
		return this.elements;
	}

	@Override
	public synchronized RepoElement getElement(String elementId) {
		RepoElement element = null;
		if (elementId != null) {
			for (RepoElement currElement : this.elements) {
				String currElementId = currElement.getId();
				if (elementId.equals(currElementId)) {
					element = currElement;
					break;
				}
			}
		}
		return element;
	}

	@Override
	public synchronized boolean addElement(RepoElement element) {
		boolean succeed = false;
		if (element != null && !this.elements.contains(element)) {
			succeed = this.elements.add(element);
			if (succeed) {
				Repo prevRepo = element.getRepo();
				if (prevRepo != null) {
					prevRepo.removeElement(element);
				}
				element.setRepo(this);
			}
		}
		return succeed;
	}

	@Override
	public boolean addElements(List<RepoElement> elements) {
		boolean succeed = false;
		if (elements != null) {
			for (RepoElement element : elements) {
				if (!this.elements.contains(element)) {
					boolean currSucceed = this.elements.add(element);
					if (currSucceed) {
						succeed = true;
						Repo prevRepo = element.getRepo();
						if (prevRepo != null) {
							prevRepo.removeElement(element);
						}
						element.setRepo(this);
					}
				}
			}
		}
		return succeed;
	}

	@Override
	public synchronized boolean removeElement(RepoElement element) {
		boolean succeed = false;
		if (element != null && this.elements.contains(element)) {
			succeed = this.elements.remove(element);
			if (succeed) {
				element.setRepo(null);
			}
		}
		return succeed;
	}

	@Override
	public void clear() {
		this.elements.clear();
	}

}
