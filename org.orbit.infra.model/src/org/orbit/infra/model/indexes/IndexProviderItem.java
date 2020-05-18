package org.orbit.infra.model.indexes;

import java.util.ArrayList;
import java.util.List;

/**
 * Index provider item model.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexProviderItem {

	protected String id;
	protected String name;
	protected String description;
	protected long dateCreated;
	protected long dateModified;

	protected List<IndexItem> indexItems = new ArrayList<IndexItem>();
	protected Integer maxIndexItemid = null;

	/**
	 * 
	 * @param id
	 */
	public IndexProviderItem(String id) {
		this.dateCreated = System.currentTimeMillis();
		this.dateModified = this.dateCreated;
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 */
	public IndexProviderItem(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.dateCreated = System.currentTimeMillis();
		this.dateModified = this.dateCreated;
	}

	protected void updateDataModified() {
		this.dateModified = System.currentTimeMillis();
	}

	/**
	 * 
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	public boolean isChanged(Object oldValue, Object newValue) {
		if ((oldValue == null && newValue != null) || (oldValue != null && !oldValue.equals(newValue))) {
			return true;
		}
		return false;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String indexProviderId) {
		this.id = indexProviderId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;

		if (isChanged(oldValue, name)) {
			updateDataModified();
		}
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		String oldValue = this.description;
		this.description = description;

		if (isChanged(oldValue, description)) {
			updateDataModified();
		}
	}

	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	public List<IndexItem> getIndexItems() {
		return this.indexItems;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexProviderItem other = (IndexProviderItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<IndexItem> getIndexItems(String type) {
		List<IndexItem> theIndexItem = new ArrayList<IndexItem>();
		if (type != null) {
			for (IndexItem currIndexItem : this.indexItems) {
				if (type.equals(currIndexItem.getType())) {
					theIndexItem.add(currIndexItem);
				}
			}
		}
		return theIndexItem;
	}

	public void setIndexItems(List<IndexItem> indexItems) {
		this.indexItems = indexItems;
	}

	public synchronized Integer getNextIndexItemId() {
		Integer nextIndexItemId = new Integer(getMaxIndexItemId().intValue() + 1);
		this.maxIndexItemid = nextIndexItemId;
		return nextIndexItemId;
	}

	protected synchronized Integer getMaxIndexItemId() {
		if (this.maxIndexItemid == null) {
			this.maxIndexItemid = new Integer(1);
			for (IndexItem currIndexItem : this.indexItems) {
				if (currIndexItem.getIndexItemId() > this.maxIndexItemid) {
					this.maxIndexItemid = currIndexItem.getIndexItemId();
				}
			}
		}
		return this.maxIndexItemid;
	}

	public synchronized IndexItem getIndexItem(Integer indexItemId) {
		IndexItem indexItem = null;
		if (indexItemId != null) {
			for (IndexItem currIndexItem : this.indexItems) {
				if (indexItemId.equals(currIndexItem.getIndexItemId())) {
					indexItem = currIndexItem;
					break;
				}
			}
		}
		return indexItem;
	}

	public synchronized IndexItem getIndexItem(String name) {
		IndexItem indexItem = null;
		if (name != null) {
			for (IndexItem currIndexItem : this.indexItems) {
				if (name.equals(currIndexItem.getName())) {
					indexItem = currIndexItem;
					break;
				}
			}
		}
		return indexItem;
	}

	public synchronized IndexItem getIndexItem(String type, String name) {
		IndexItem indexItem = null;
		if (type != null && name != null) {
			for (IndexItem currIndexItem : this.indexItems) {
				if (type.equals(currIndexItem.getType()) && name.equals(currIndexItem.getName())) {
					indexItem = currIndexItem;
					break;
				}
			}
		}
		return indexItem;
	}

	public synchronized boolean addIndexItem(IndexItem indexItem) {
		if (indexItem != null && !this.indexItems.contains(indexItem)) {
			return this.indexItems.add(indexItem);
		}
		return false;
	}

	public synchronized boolean removeIndexItem(IndexItem indexItem) {
		if (indexItem != null) {
			return this.indexItems.remove(indexItem);
		}
		return false;
	}

	public synchronized boolean removeIndexItem(Integer indexItemId) {
		if (indexItemId != null) {
			for (IndexItem currIndexItem : this.indexItems) {
				if (indexItemId.equals(currIndexItem.getIndexItemId())) {
					return this.indexItems.remove(currIndexItem);
				}
			}
		}
		return false;
	}

}
