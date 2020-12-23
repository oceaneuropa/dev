package org.orbit.infra.model.subs.impl;

import org.orbit.infra.model.subs.SubsTargetType;
import org.origin.common.util.DateUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsTargetTypeImpl implements SubsTargetType {

	protected Integer id;
	protected String type;
	protected String name;
	protected long dateCreated;
	protected long dateModified;

	public SubsTargetTypeImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param type
	 * @param name
	 * @param dateCreated
	 * @param dateModified
	 */
	public SubsTargetTypeImpl(Integer id, String type, String name, long dateCreated, long dateModified) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
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

	@Override
	public String toString() {
		String dateCreatedStr = DateUtil.toString(this.dateCreated, DateUtil.getJdbcDateFormat());
		String dateModifiedStr = DateUtil.toString(this.dateModified, DateUtil.getJdbcDateFormat());

		return "SubsTargetTypeImpl [id=" + id + ", type=" + type + ", name=" + name + ", dateCreated=" + dateCreatedStr + ", dateModified=" + dateModifiedStr + "]";
	}

}
