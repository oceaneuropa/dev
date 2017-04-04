package org.origin.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合 (Combinations) 与排列 (Permutation) 工具类
 *
 * @see http://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
 */
public class CombinationUtil {

	/**
	 * Prints all combinations of size r in arr[] of size n.
	 * 
	 * @param arr
	 *            Input array
	 * @param n
	 *            Size of input array
	 * @param r
	 *            Size of combination
	 */
	public static List<int[]> getCombinationsV1(int arr[], int n, int r) {
		List<int[]> combinations = new ArrayList<int[]>();

		// Temporary array to store current combination
		int data[] = new int[r];

		// Print all combination using temporary array 'data[]'
		getCombinationsV1(arr, data, 0, n - 1, 0, r, combinations, false);

		return combinations;
	}

	/**
	 * 
	 * @param arr
	 *            Input array
	 * @param data
	 *            Temporary array to store current combination
	 * @param arrayStartIndex
	 *            Staring index in arr array
	 * @param arrayEndIndex
	 *            Ending index in arr array
	 * @param dataIndex
	 *            Current index in data array
	 * @param r
	 *            Size of a combination
	 */
	private static void getCombinationsV1(int arr[], int data[], int arrayStartIndex, int arrayEndIndex, int dataIndex, int r, List<int[]> combinations, boolean print) {
		// Current combination is ready to be printed, print it
		if (dataIndex == r) {
			if (print) {
				print(data, r);
			}

			int[] combination = new int[r];
			System.arraycopy(data, 0, combination, 0, r);
			combinations.add(combination);

			return;
		}

		// Replace index with all possible elements.
		// The condition "end-i+1 >= r-index" makes sure that including one element at index
		// will make a combination with remaining elements at remaining positions.
		for (int i = arrayStartIndex; i <= arrayEndIndex && arrayEndIndex - i + 1 >= r - dataIndex; i++) {
			data[dataIndex] = arr[i];
			getCombinationsV1(arr, data, i + 1, arrayEndIndex, dataIndex + 1, r, combinations, print);
		}
	}

	/**
	 * Prints all combinations of size r in arr[] of size n.
	 * 
	 * @param arr
	 *            Input array
	 * @param n
	 *            Size of input array
	 * @param r
	 *            Size of combination
	 */
	public static List<int[]> getCombinationsV2(int[] arr, int n, int r) {
		List<int[]> combinations = new ArrayList<int[]>();

		// Temporary array to store current combination
		int data[] = new int[r];

		// Print all combination using temporary data array
		getCombinationsV2(arr, n, r, 0, data, 0, combinations, false);

		return combinations;
	}

	/**
	 * 
	 * @param arr
	 *            Input array
	 * @param n
	 *            Size of input array
	 * @param r
	 *            Size of combination
	 * @param dataIndex
	 *            Current index in data array
	 * @param data
	 *            Temporary array to store current combination
	 * @param arrayIndex
	 *            Current index in input array
	 * @param combinations
	 *            Collected combination list
	 * @param print
	 *            print out each combination
	 */
	private static void getCombinationsV2(int[] arr, int n, int r, int dataIndex, int[] data, int arrayIndex, List<int[]> combinations, boolean print) {
		// Current combination is ready to be printed, print it
		if (dataIndex == r) {
			if (print) {
				print(data, r);
			}

			int[] combination = new int[r];
			System.arraycopy(data, 0, combination, 0, r);
			combinations.add(combination);

			return;
		}

		// When no more elements are there to put in data array
		if (arrayIndex >= n) {
			return;
		}

		// current is included, put next at next location
		data[dataIndex] = arr[arrayIndex];
		getCombinationsV2(arr, n, r, dataIndex + 1, data, arrayIndex + 1, combinations, print);

		// current is excluded, replace it with next (Note that i+1 is passed, but index is not changed)
		getCombinationsV2(arr, n, r, dataIndex, data, arrayIndex + 1, combinations, print);
	}

	/**
	 * 
	 * @param r
	 * @param data
	 */
	public static void print(int[] data, int r) {
		for (int j = 0; j < r; j++) {
			System.out.print(data[j] + " ");
		}
		System.out.println("");
	}

	/**
	 * http://www.mathwords.com/c/combination_formula.htm
	 * 
	 * n!/r!(n-r)!
	 * 
	 * @param n
	 * @param r
	 * @return
	 */
	public static long getNumberOfCombinationV1(int n, int r) {
		long v1 = factorial(n);
		long v2 = factorial(r);
		long v3 = factorial(n - r);
		return v1 / (v2 * v3);
	}

	/**
	 * http://www.mathwords.com/c/combination_formula.htm
	 * 
	 * n!/r!(n-r)!
	 * 
	 * @param n
	 * @param r
	 * @return
	 */
	public static long getNumberOfCombinationV2(int n, int r) {
		long v1 = factorial(n, r);
		long v2 = factorial(r);
		return v1 / v2;
	}

	/**
	 * http://stackoverflow.com/questions/891031/is-there-a-method-that-calculates-a-factorial-in-java
	 * 
	 * n (n-1) (n-2) ... 2 1
	 * 
	 * @param n
	 * @return
	 */
	public static long factorial(int n) {
		long fact = 1; // this will be the result
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	/**
	 * n (n-1) (n-2) ... (n-size+1)
	 * 
	 * @param n
	 * @param size
	 * @return
	 */
	public static long factorial(int n, int size) {
		long fact = 1; // this will be the result
		for (int i = 0; i < size; i++) {
			fact *= (n - i);
		}
		return fact;
	}

	/**
	 * Check whether one combination contains all numbers of another combination.
	 * 
	 * @param combinationOfMoreNums
	 * @param combinationOfLessNums
	 * @return
	 */
	public static boolean contains(int[] combinationOfMoreNums, int[] combinationOfLessNums) {
		boolean containsAllNums = false;
		for (int combinationOfLessNum : combinationOfLessNums) {
			boolean containsCurrentNum = false;
			for (int combinationOfMoreNum : combinationOfMoreNums) {
				if (combinationOfMoreNum == combinationOfLessNum) {
					containsCurrentNum = true;
					break;
				}
			}
			if (containsCurrentNum) {
				containsAllNums = true;
			} else {
				containsAllNums = false;
				break;
			}
		}
		return containsAllNums;
	}

	/**
	 * Driver function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// long size1 = getNumberOfCombinationV1(15, 4);
		// long size2 = getNumberOfCombinationV2(15, 4);
		// System.out.println("expected number of combinations 1: " + size1);
		// System.out.println("expected number of combinations 2: " + size2);

		int arr[] = { 1, 2, 3, 4, 5 };
		int r = 3;
		int n = arr.length;
		// printCombinationV1(arr, n, r);
		// printCombinationV2(arr, n, r);

		// long size = getNumberOfCombinationV1(n, r);
		// long size = getNumberOfCombinationV2(n, r);
		// System.out.println("expected size of combinations: " + size);

		List<int[]> combinations = getCombinationsV2(arr, n, r);
		System.out.println("size of combinations: " + combinations.size());
		for (int[] combination : combinations) {
			print(combination, r);
		}
	}

}
