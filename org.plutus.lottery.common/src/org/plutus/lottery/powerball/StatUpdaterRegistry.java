package org.plutus.lottery.powerball;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.plutus.lottery.common.Comparators.StatUpdaterComparator;

public class StatUpdaterRegistry {

	private static StatUpdaterRegistry INSTANCE = new StatUpdaterRegistry();

	public static StatUpdaterRegistry getInstance() {
		return INSTANCE;
	}

	public List<StatUpdater> updaters = new ArrayList<StatUpdater>();

	public List<StatUpdater> getUpdaters() {
		return this.updaters;
	}

	public StatUpdater getUpdater(String updaterId) {
		if (updaterId != null) {
			for (StatUpdater updater : this.updaters) {
				if (updaterId.equals(updater.getId())) {
					return updater;
				}
			}
		}
		return null;
	}

	public boolean contains(String updaterId) {
		if (updaterId != null) {
			for (StatUpdater updater : this.updaters) {
				if (updaterId.equals(updater.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean add(StatUpdater updater) {
		if (updater != null && !this.updaters.contains(updater)) {
			this.updaters.add(updater);

			sortUpdatersByPriority();

			return true;
		}
		return false;
	}

	public boolean remove(StatUpdater updater) {
		if (updater != null && this.updaters.contains(updater)) {
			return this.updaters.remove(updater);
		}
		return false;
	}

	public boolean remove(String updaterId) {
		if (updaterId != null) {
			StatUpdater updater = getUpdater(updaterId);
			if (updater != null) {
				return this.updaters.remove(updater);
			}
		}
		return false;
	}

	protected void sortUpdatersByPriority() {
		if (this.updaters.size() > 1) {
			Collections.sort(this.updaters, StatUpdaterComparator.ASC);
		}
	}

}
