package org.origin.common.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListUtil {

	/**
	 * Merge old list and new list into the result list.
	 * 
	 * @param <T>
	 * 
	 * @param result
	 * @param oldList
	 * @param newList
	 */
	public static <T> List<T> merge(List<T> oldList, List<T> newList, Comparator<T> comparator) {
		List<T> result = new ArrayList<T>();

		int oldSize = oldList.size();
		int newSize = newList.size();

		int oldIndex = 0;
		int newIndex = 0;

		if (oldSize > 0 && newSize > 0) {
			while (true) {
				T oldObj = oldList.get(oldIndex);
				T newObj = newList.get(newIndex);

				if (comparator.compare(oldObj, newObj) == 0) {
					// same obj - use new obj to replace old one.
					result.add(newObj);

					// move new list's index ahead to get next new obj in the next iteration.
					newIndex++;
					// move old list's index ahead to get next old obj in the next iteration.
					oldIndex++;

				} else {
					// different obj - keep old obj.
					result.add(oldObj);

					// move old list's index ahead to get next old obj in the next iteration.
					oldIndex++;
				}

				if (oldIndex >= oldSize || newIndex >= newSize) {
					break;
				}
			}
		}

		if (oldIndex < oldSize) {
			// more old obj - append the remaining of the old list to the result
			List<T> remainingList = oldList.subList(oldIndex, oldSize);
			result.addAll(remainingList);
		}

		if (newIndex < newSize) {
			// more new obj - append the remaining of the new list to the result
			List<T> remainingList = newList.subList(newIndex, newSize);
			result.addAll(remainingList);
		}

		return result;
	}

}
