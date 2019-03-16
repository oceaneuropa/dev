package org.orbit.infra.api.indexes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexItemUpdaterSupport {

	protected List<IndexItemUpdater> updaters = new ArrayList<IndexItemUpdater>();

	public List<IndexItemUpdater> getUpdaters() {
		return this.updaters;
	}

	public void addUpdater(IndexItemUpdater updater) {
		if (updater != null && !this.updaters.contains(updater)) {
			this.updaters.add(updater);
		}
	}

	public void removeUpdater(IndexItemUpdater updater) {
		if (updater != null && this.updaters.contains(updater)) {
			this.updaters.remove(updater);
		}
	}

	public void addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) {
		for (IndexItemUpdater updater : getUpdaters()) {
			updater.addIndexItem(indexProviderId, type, name, properties);
		}
	}

	public void setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) {
		for (IndexItemUpdater updater : getUpdaters()) {
			updater.setProperties(indexProviderId, indexItemId, properties);
		}
	}

}
