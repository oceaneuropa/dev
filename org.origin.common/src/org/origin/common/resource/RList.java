package org.origin.common.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.origin.common.resource.impl.RObjectImpl;

public class RList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -2237197101138335728L;

	protected Resource resource;
	protected RObject container;
	protected boolean containment;

	/**
	 * 
	 * @param resource
	 */
	public RList(Resource resource) {
		this.resource = resource;
		this.containment = true;
	}

	/**
	 * 
	 * @param container
	 * @param containment
	 */
	public RList(RObject container, boolean containment) {
		this.container = container;
		this.containment = containment;
	}

	public boolean isContainment() {
		return containment;
	}

	public void setContainment(boolean containment) {
		this.containment = containment;
	}

	public Resource eResource() {
		if (this.container != null) {
			Resource resource = this.container.eResource();
			if (resource != null) {
				return resource;
			}
		}
		return this.resource;
	}

	public RObject eContainer() {
		return this.container;
	}

	// ---------------------------------------------------------------------------
	// add
	// ---------------------------------------------------------------------------
	@Override
	public boolean add(E element) {
		boolean succeed = super.add(element);
		if (succeed) {
			applyContainer(element);
		}
		return succeed;
	}

	@Override
	public void add(int index, E element) {
		super.add(index, element);
		applyContainer(element);
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean succeed = super.addAll(collection);
		if (succeed) {
			if (collection != null) {
				for (Iterator<?> itor = collection.iterator(); itor.hasNext();) {
					E element = (E) itor.next();
					applyContainer(element);
				}
			}
		}
		return succeed;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> collection) {
		boolean succeed = super.addAll(index, collection);
		if (succeed) {
			if (collection != null) {
				for (Iterator<?> itor = collection.iterator(); itor.hasNext();) {
					E element = (E) itor.next();
					applyContainer(element);
				}
			}
		}
		return succeed;
	}

	// ---------------------------------------------------------------------------
	// remove
	// ---------------------------------------------------------------------------
	@Override
	public E remove(int index) {
		E element = super.remove(index);
		unapplyContainer(element);
		return element;
	}

	@Override
	public boolean remove(Object o) {
		boolean succeed = super.remove(o);
		if (succeed) {
			unapplyContainer((E) o);
		}
		return succeed;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean succeed = super.removeAll(collection);
		if (succeed) {
			if (collection != null) {
				for (Iterator<?> itor = collection.iterator(); itor.hasNext();) {
					E element = (E) itor.next();
					unapplyContainer(element);
				}
			}
		}
		return succeed;
	}

	@Override
	public void clear() {
		for (Iterator<?> itor = iterator(); itor.hasNext();) {
			E element = (E) itor.next();
			unapplyContainer(element);
		}
		super.clear();
	}

	/**
	 * 
	 * @param element
	 */
	protected void applyContainer(E element) {
		if (element instanceof RObjectImpl) {
			RObjectImpl rObject = (RObjectImpl) element;

			Resource resource = eResource();
			RObject container = eContainer();

			if (container != null) {
				// set container to the RObject
				if (isContainment()) {
					rObject.setContainer(container);
				}
			} else {
				// set Resource to the RObject
				rObject.setResource(resource);
			}
		}
	}

	/**
	 * 
	 * @param element
	 */
	protected void unapplyContainer(E element) {
		if (element instanceof RObjectImpl) {
			RObjectImpl rObject = (RObjectImpl) element;

			// unset container of the RObject
			rObject.setContainer(null);
			// unset Resource of the RObject
			rObject.setResource(null);
		}
	}

}
