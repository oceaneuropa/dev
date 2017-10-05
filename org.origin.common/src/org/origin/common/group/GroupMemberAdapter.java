package org.origin.common.group;

import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public abstract class GroupMemberAdapter {

	protected ServiceTracker<GroupMember, GroupMember> groupMemberTracker;
	protected Map<ServiceReference<GroupMember>, GroupMember> groupMembersMap;
	protected boolean debug = true;

	public GroupMemberAdapter() {
		this.groupMembersMap = new LinkedHashMap<ServiceReference<GroupMember>, GroupMember>();
	}

	protected String getGroupMemberLabel(GroupMember groupMember) {
		String namespace = groupMember.getNamespace();
		String name = groupMember.getName();
		String type = groupMember.getType();
		return "{" + namespace + "}" + name + "(" + type + ")";
	}

	/**
	 * Start tracking GroupMembers.
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		this.groupMemberTracker = new ServiceTracker<GroupMember, GroupMember>(bundleContext, GroupMember.class, new ServiceTrackerCustomizer<GroupMember, GroupMember>() {
			@Override
			public GroupMember addingService(ServiceReference<GroupMember> reference) {
				GroupMember groupMember = bundleContext.getService(reference);
				if (debug) {
					System.out.println(getGroupMemberLabel(groupMember) + "is online");
				}
				groupMembersMap.put(reference, groupMember);
				online(reference, groupMember);
				return groupMember;
			}

			@Override
			public void modifiedService(ServiceReference<GroupMember> reference, GroupMember groupMember) {
				if (debug) {
					System.out.println(getGroupMemberLabel(groupMember) + "is modified");
				}
			}

			@Override
			public void removedService(ServiceReference<GroupMember> reference, GroupMember groupMember) {
				if (debug) {
					System.out.println(getGroupMemberLabel(groupMember) + "is offline");
				}
				groupMembersMap.remove(reference);
				offline(reference, groupMember);
			}
		});
		this.groupMemberTracker.open();
	}

	/**
	 * Stop tracking GroupMembers.
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		this.groupMemberTracker.close();
	}

	/**
	 * 
	 * @param reference
	 * @param groupMember
	 */
	public abstract void online(ServiceReference<GroupMember> reference, GroupMember groupMember);

	/**
	 * 
	 * @param reference
	 * @param groupMember
	 */
	public abstract void offline(ServiceReference<GroupMember> reference, GroupMember groupMember);

}
