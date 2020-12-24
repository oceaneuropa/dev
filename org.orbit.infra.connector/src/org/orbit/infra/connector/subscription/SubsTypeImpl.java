package org.orbit.infra.connector.subscription;

import org.orbit.infra.api.subscription.SubsServerAPI;
import org.orbit.infra.api.subscription.SubsType;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsTypeImpl implements SubsType {

	protected SubsServerAPI api;
	protected Integer id;
	protected String type;
	protected String name;
	protected long dateCreated;
	protected long dateModified;

	public SubsTypeImpl() {
	}

	public SubsServerAPI getAPI() {
		return this.api;
	}

	public void setAPI(SubsServerAPI api) {
		this.api = api;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public long getDateCreated() {
		return this.dateCreated;
	}

	@Override
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public long getDateModified() {
		return this.dateModified;
	}

	@Override
	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
