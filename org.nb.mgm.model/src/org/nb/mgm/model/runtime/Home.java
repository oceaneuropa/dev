package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class Home extends ModelObject {

	protected String url;

	protected List<String> joinedMetaSectorIds = new ArrayList<String>();
	protected List<String> joinedMetaSpaceIds = new ArrayList<String>();

	public Home() {
	}

	/**
	 * 
	 * @param machine
	 */
	public Home(Machine machine) {
		super(machine);
	}

	public Machine getMachine() {
		if (this.getParent() instanceof Machine) {
			return (Machine) this.getParent();
		}
		return null;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// joinedMetaSectorIds
	// ----------------------------------------------------------------------------------------------------------------
	public List<String> getJoinedMetaSectorIds() {
		return this.joinedMetaSectorIds;
	}

	public void setJoinedMetaSectorIds(List<String> joinedMetaSectorIds) {
		this.joinedMetaSectorIds = joinedMetaSectorIds;
	}

	public void addJoinedMetaSectorId(String joinedMetaSectorId) {
		if (joinedMetaSectorId != null && !this.joinedMetaSectorIds.contains(joinedMetaSectorId)) {
			this.joinedMetaSectorIds.add(joinedMetaSectorId);
		}
	}

	public void removeJoinedMetaSectorId(String joinedMetaSectorId) {
		if (joinedMetaSectorId != null && this.joinedMetaSectorIds.contains(joinedMetaSectorId)) {
			this.joinedMetaSectorIds.remove(joinedMetaSectorId);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// joinedMetaSpaceIds
	// ----------------------------------------------------------------------------------------------------------------
	public List<String> getJoinedMetaSpaceIds() {
		return this.joinedMetaSpaceIds;
	}

	public void setJoinedMetaSpaceIds(List<String> joinedMetaSpaceIds) {
		this.joinedMetaSpaceIds = joinedMetaSpaceIds;
	}

	public void addJoinedMetaSpaceId(String joinedMetaSpaceId) {
		if (joinedMetaSpaceId != null && !this.joinedMetaSpaceIds.contains(joinedMetaSpaceId)) {
			this.joinedMetaSpaceIds.add(joinedMetaSpaceId);
		}
	}

	public void removeJoinedMetaSpaceId(String joinedMetaSpaceId) {
		if (joinedMetaSpaceId != null && this.joinedMetaSpaceIds.contains(joinedMetaSpaceId)) {
			this.joinedMetaSpaceIds.remove(joinedMetaSpaceId);
		}
	}

}
