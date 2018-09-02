package oreilly.sort.median_sort;

public class MedianSort {

	/**
	 * 
	 * @param array
	 * @param leftIndex
	 * @param rightIndex
	 * @param pivotIndex
	 * @return
	 */
	public int partition(int[] array, int leftIndex, int rightIndex, int pivotIndex) {
		int pivotValue = array[pivotIndex];

		int tmp = array[rightIndex];
		array[rightIndex] = pivotValue;
		array[pivotIndex] = tmp;

		int storeIndex = leftIndex;
		for (int i = leftIndex; i < rightIndex; i++) {
			tmp = array[i];
			if (tmp < pivotValue && i != storeIndex) {
				array[i] = array[storeIndex];
				array[storeIndex] = tmp;
				storeIndex++;
			}
		}

		tmp = array[rightIndex];
		array[rightIndex] = array[storeIndex];
		array[storeIndex] = tmp;

		return storeIndex;
	}

}
