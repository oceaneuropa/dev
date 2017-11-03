package org.origin.common.cluster.group;

import java.util.HashSet;
import java.util.Set;

public class GroupMemberReference {

	protected GroupMember groupMember;
	protected Set<Group> joinedGroups = new HashSet<Group>();

	public GroupMemberReference(GroupMember groupMember) {
		this.groupMember = groupMember;
	}

	public GroupMember getGroupMember() {
		return this.groupMember;
	}

	public Set<Group> getJoinedGroups() {
		return this.joinedGroups;
	}

	/**
	 * 
	 * @param group
	 * @return
	 */
	public boolean hasJoinedGroup(Group group) {
		if (group != null && this.joinedGroups.contains(group)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param group
	 */
	public void addToJoinedGroup(Group group) {
		if (group != null) {
			this.joinedGroups.add(group);
		}
	}

	/**
	 * 
	 * @param group
	 */
	public void removeFromJoinedGrouop(Group group) {
		if (group != null && this.joinedGroups.contains(group)) {
			this.joinedGroups.remove(group);
		}
	}

}
