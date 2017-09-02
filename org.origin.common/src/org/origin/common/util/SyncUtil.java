package org.origin.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SyncUtil {

	/**
	 * 
	 * @param sources
	 * @param targets
	 * @param synchronizer
	 * @return
	 */
	public static <SOURCE, TARGET> boolean isSynchronized(List<SOURCE> sources, List<TARGET> targets, Synchronizer<SOURCE, TARGET> synchronizer) {
		if (synchronizer == null) {
			throw new RuntimeException("ListSynchronizer is null.");
		}
		if (sources == null) {
			sources = Collections.emptyList();
		}
		if (targets == null) {
			targets = Collections.emptyList();
		}

		int sourcesSize = sources.size();
		int targetsSize = targets.size();
		if (sourcesSize != targetsSize) {
			// Sources and targets are not the same size
			// - out of sync
			return false;
		}

		Map<TARGET, SOURCE> targetToSource = new HashMap<TARGET, SOURCE>();
		for (SOURCE source : sources) {
			// find sync target for the source
			TARGET syncTarget = null;
			for (TARGET target : targets) {
				if (synchronizer.isSyncTarget(source, target)) {
					syncTarget = target;
					break;
				}
			}

			if (syncTarget == null) {
				// There is no target for the source
				// - out of sync
				return false;
			}

			targetToSource.put(syncTarget, source);

			boolean inSync = synchronizer.isInSync(source, syncTarget);
			if (!inSync) {
				// Source and target are not in sync
				// - out of sync
				return false;
			}
		}

		for (TARGET target : targets) {
			SOURCE source = targetToSource.get(target);
			if (source == null) {
				// There is no source for the target
				// - out of sync
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param sources
	 * @param targets
	 * @param synchronizer
	 * @param addCreatedTargetToTargetsList
	 * @param removeDeletedTargetFromTargetsList
	 */
	public static <SOURCE, TARGET> void synchronize(List<SOURCE> sources, List<TARGET> targets, Synchronizer<SOURCE, TARGET> synchronizer, boolean addCreatedTargetToTargetsList, boolean removeDeletedTargetFromTargetsList) {
		if (synchronizer == null) {
			throw new RuntimeException("ListSynchronizer is null.");
		}
		if (sources == null) {
			sources = Collections.emptyList();
		}
		if (targets == null) {
			targets = Collections.emptyList();
		}

		// 1. Collection information for synchronization
		List<SOURCE> sourcesWithoutTarget = new ArrayList<SOURCE>();
		Map<SOURCE, TARGET> sourceToOutOfSyncTarget = new LinkedHashMap<SOURCE, TARGET>();
		List<TARGET> targetsWithoutSource = new ArrayList<TARGET>();

		Map<TARGET, SOURCE> targetToSource = new HashMap<TARGET, SOURCE>();
		for (SOURCE source : sources) {
			// find sync target for the source
			TARGET syncTarget = null;
			for (TARGET target : targets) {
				if (synchronizer.isSyncTarget(source, target)) {
					syncTarget = target;
					break;
				}
			}

			if (syncTarget != null) {
				targetToSource.put(syncTarget, source);

				boolean inSync = synchronizer.isInSync(source, syncTarget);
				if (!inSync) {
					// Source and target are not in sync
					// - need to sync them
					sourceToOutOfSyncTarget.put(source, syncTarget);
				}

			} else {
				// There is no target for the source
				// - need to create target
				sourcesWithoutTarget.add(source);
			}
		}

		for (TARGET target : targets) {
			SOURCE source = targetToSource.get(target);
			if (source == null) {
				// There is no source for the target
				// - need to delete target
				targetsWithoutSource.add(target);
			}
		}

		// 2. Perform synchronization
		// (1) Delete targets without source
		if (!targetsWithoutSource.isEmpty()) {
			for (TARGET target : targetsWithoutSource) {
				synchronizer.disposeTarget(target);
			}

			if (removeDeletedTargetFromTargetsList) {
				targets.removeAll(targetsWithoutSource);
			}
		}

		// (2) Synchronize targets with sources
		for (Iterator<SOURCE> sourceItor = sourceToOutOfSyncTarget.keySet().iterator(); sourceItor.hasNext();) {
			SOURCE source = sourceItor.next();
			TARGET target = sourceToOutOfSyncTarget.get(source);
			synchronizer.syncTarget(source, target);
		}

		// (3) Create target for source (without target)
		for (SOURCE source : sourcesWithoutTarget) {
			TARGET target = synchronizer.buildTarget(source);

			if (addCreatedTargetToTargetsList) {
				if (target != null) {
					targets.add(target);
				}
			}
		}
	}

}
