package org.origin.common.workingcopy;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.origin.common.resource.AbstractResourceFactory;
import org.origin.common.resource.Resource;
import org.origin.common.resource.ResourceFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 * @param <RES>
 * @param <ELEMENT>
 */
public abstract class AbstractWorkingCopyFactory<RES extends Resource, ELEMENT> extends AbstractResourceFactory<RES> implements ResourceFactory<RES>, WorkingCopyFactory<ELEMENT> {

	protected Map<File, WorkingCopy<ELEMENT>> workingCopyMap = new LinkedHashMap<File, WorkingCopy<ELEMENT>>();

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

	@Override
	public synchronized boolean hasWorkingCopy(File file) {
		WorkingCopy<ELEMENT> wc = this.workingCopyMap.get(file);
		return wc != null ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final synchronized WorkingCopy<ELEMENT> getWorkingCopy(File file) {
		WorkingCopy<ELEMENT> workingCopy = this.workingCopyMap.get(file);
		if (workingCopy == null) {
			WorkingCopy<ELEMENT> newWorkingCopy = createWorkingCopy(file);
			if (newWorkingCopy instanceof AbstractWorkingCopy) {
				((AbstractWorkingCopy<RES, ELEMENT>) newWorkingCopy).setFactory(this);
			} else {
				newWorkingCopy.adapt(WorkingCopyFactory.class, this);
			}
			if (newWorkingCopy != null) {
				workingCopy = newWorkingCopy;
				this.workingCopyMap.put(file, workingCopy);
			}
		}
		return workingCopy;
	}

	/**
	 * Create the working copy for a file.
	 * 
	 * @param file
	 * @return
	 */
	protected abstract WorkingCopy<ELEMENT> createWorkingCopy(File file);

	@Override
	public synchronized WorkingCopy<ELEMENT> removeWorkingCopy(File file) {
		return this.workingCopyMap.remove(file);
	}

}
