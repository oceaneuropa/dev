package org.origin.common.cluster.group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class GroupDispatcher {

	protected BundleContext bundleContext;
	protected GroupAdapter groupAdapter;
	protected GroupMemberAdapter groupMemberAdapter;
	protected List<GroupMemberReference> groupMemberReferences;
	protected ReadWriteLock lock;
	protected boolean debug = true;

	public GroupDispatcher() {
		this.groupMemberReferences = new ArrayList<GroupMemberReference>();
		this.lock = new ReentrantReadWriteLock();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");
		this.bundleContext = bundleContext;

		// Start tracking GroupMembers
		this.groupMemberAdapter = new GroupMemberAdapter() {
			@Override
			public void online(ServiceReference<GroupMember> reference, GroupMember groupMember) {
				GroupMemberReference groupMemberReference = findGroupMemberReference(groupMember);
				if (groupMemberReference == null) {
					groupMemberReference = new GroupMemberReference(groupMember);
					groupMemberReferences.add(groupMemberReference);
				}
			}

			@Override
			public void offline(ServiceReference<GroupMember> reference, GroupMember groupMember) {

			}
		};
		this.groupMemberAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".stop()");

		// Stop tracking MemberGroups
		if (this.groupMemberAdapter != null) {
			this.groupMemberAdapter.stop(bundleContext);
			this.groupMemberAdapter = null;
		}

		this.groupMemberReferences.clear();
	}

	/**
	 * Find GroupMemberReference
	 * 
	 * @param groupMember
	 * @return
	 */
	protected GroupMemberReference findGroupMemberReference(GroupMember groupMember) {
		GroupMemberReference memberReference = null;
		if (groupMember != null) {
			String namespace = groupMember.getNamespace();
			String name = groupMember.getName();
			String type = groupMember.getType();

			for (GroupMemberReference currGroupMemberReference : this.groupMemberReferences) {
				GroupMember currGroupMember = currGroupMemberReference.getGroupMember();

				if (currGroupMember == groupMember) {
					memberReference = currGroupMemberReference;
					break;

				} else {
					String currNamespace = currGroupMember.getNamespace();
					String currName = currGroupMember.getName();
					String currType = currGroupMember.getType();

					if (namespace != null && namespace.equals(currNamespace) //
							&& name != null && name.equals(currName) //
							&& type != null && type.equals(currType) //
					) {
						memberReference = currGroupMemberReference;
						break;
					}
				}
			}
		}
		return memberReference;
	}

}
