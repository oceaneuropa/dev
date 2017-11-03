package org.origin.common.cluster.group;

import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class GroupAdapter {

	protected ServiceTracker<Group, Group> groupTracker;
	protected Map<ServiceReference<Group>, Group> groupsMap;
	protected boolean debug = true;

	public GroupAdapter() {
		this.groupsMap = new LinkedHashMap<ServiceReference<Group>, Group>();
	}

	protected String getGroupLabel(Group group) {
		return group.getName();
	}

	/**
	 * Start tracking Groups.
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.groupTracker = new ServiceTracker<Group, Group>(bundleContext, Group.class, new ServiceTrackerCustomizer<Group, Group>() {
			@Override
			public Group addingService(ServiceReference<Group> reference) {
				Group group = bundleContext.getService(reference);
				if (debug) {
					System.out.println(getGroupLabel(group) + "is online");
				}
				groupsMap.put(reference, group);
				online(reference, group);
				return group;
			}

			@Override
			public void modifiedService(ServiceReference<Group> reference, Group group) {
			}

			@Override
			public void removedService(ServiceReference<Group> reference, Group group) {
				if (debug) {
					System.out.println(getGroupLabel(group) + "is offline");
				}
				groupsMap.remove(reference);
				offline(reference, group);
			}
		});
		this.groupTracker.open();
	}

	/**
	 * Stop tracking Groups.
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		this.groupTracker.close();
	}

	/**
	 * 
	 * @param reference
	 * @param group
	 */
	public abstract void online(ServiceReference<Group> reference, Group group);

	/**
	 * 
	 * @param reference
	 * @param group
	 */
	public abstract void offline(ServiceReference<Group> reference, Group group);

}
