package org.origin.common.workingcopy;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.origin.common.resource.Resource;
import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.impl.ResourceFactoryImpl;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 * @param <RES>
 */
public abstract class AbstractWorkingCopyFactory<RES extends Resource> extends ResourceFactoryImpl<RES> implements ResourceFactory<RES>, WorkingCopyFactory {

	protected Map<URI, WorkingCopy> workingCopyMap = new LinkedHashMap<URI, WorkingCopy>();

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

	@Override
	public synchronized boolean hasWorkingCopy(URI uri) {
		WorkingCopy wc = this.workingCopyMap.get(uri);
		return wc != null ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final synchronized WorkingCopy getWorkingCopy(URI uri) {
		WorkingCopy workingCopy = this.workingCopyMap.get(uri);
		if (workingCopy == null) {
			WorkingCopy newWorkingCopy = createWorkingCopy(uri);
			if (newWorkingCopy instanceof AbstractWorkingCopy) {
				((AbstractWorkingCopy<RES>) newWorkingCopy).setFactory(this);
			} else {
				newWorkingCopy.adapt(WorkingCopyFactory.class, this);
			}
			if (newWorkingCopy != null) {
				workingCopy = newWorkingCopy;
				this.workingCopyMap.put(uri, workingCopy);
			}
		}
		return workingCopy;
	}

	/**
	 * Create the working copy for a URI.
	 * 
	 * @param uri
	 * @return
	 */
	protected abstract WorkingCopy createWorkingCopy(URI uri);

	@Override
	public synchronized WorkingCopy removeWorkingCopy(URI uri) {
		return this.workingCopyMap.remove(uri);
	}

}
