package org.origin.common.util;

/**
 * <p>
 * In the constant names, '¢' represents include. 'c' represents exclude.
 * 
 * <p>
 * e.g. [1, 2] is denoted as ¢1_2¢
 * 
 * <p>
 * e.g. (3, 4) is denoted as c3_4c
 * 
 * <p>
 * e.g. (5, 6] is denoted as c5_6¢
 * 
 * <p>
 * e.g. [7, 8) is denoted as ¢7_8c
 * 
 * <p>
 * e.g. [1, 2] is denoted as ¢1_2¢
 * 
 */
public class RangeConstants {

	// (~, 0)
	public static Range<Integer> unlimited_0c = new Range<Integer>().from(true).to(0, false);
	// (~, 0]
	public static Range<Integer> unlimited_0¢ = new Range<Integer>().from(true).to(0, true);
	// [0, 0]
	public static Range<Integer> ¢0_0¢ = new Range<Integer>().from(0, true).to(0, true);
	// [0, ~)
	public static Range<Integer> ¢0_unlimited = new Range<Integer>().from(0, true).to(true);
	// (0, ~)
	public static Range<Integer> c0_unlimited = new Range<Integer>().from(0, false).to(true);

	// [1, 25)
	public static Range<Integer> ¢1_25c = new Range<Integer>().from(1, true).to(25, false);
	// [25, 50)
	public static Range<Integer> ¢25_50c = new Range<Integer>().from(25, true).to(50, false);
	// [50, 100)
	public static Range<Integer> ¢50_100c = new Range<Integer>().from(50, true).to(100, false);
	// [100, 200)
	public static Range<Integer> ¢100_200c = new Range<Integer>().from(100, true).to(200, false);

	// [1, 200)
	public static Range<Integer> ¢1_200c = new Range<Integer>().from(1, true).to(200, false);
	// [200, 400)
	public static Range<Integer> ¢200_400c = new Range<Integer>().from(200, true).to(400, false);
	// [400, 600)
	public static Range<Integer> ¢400_600c = new Range<Integer>().from(400, true).to(600, false);
	// [600, 800)
	public static Range<Integer> ¢600_800c = new Range<Integer>().from(600, true).to(800, false);
	// [800, 1000)
	public static Range<Integer> ¢800_1000c = new Range<Integer>().from(800, true).to(1000, false);
	// [1000, 1200)
	public static Range<Integer> ¢1000_1200c = new Range<Integer>().from(1000, true).to(1200, false);
	// [1200, 1400)
	public static Range<Integer> ¢1200_1400c = new Range<Integer>().from(1200, true).to(1400, false);
	// [1400, 1600)
	public static Range<Integer> ¢1400_1600c = new Range<Integer>().from(1400, true).to(1600, false);
	// [1600, 1800)
	public static Range<Integer> ¢1600_1800c = new Range<Integer>().from(1600, true).to(1800, false);
	// [1800, 2000)
	public static Range<Integer> ¢1800_2000c = new Range<Integer>().from(1800, true).to(2000, false);
	// [2000, 2200)
	public static Range<Integer> ¢2000_2200c = new Range<Integer>().from(2000, true).to(2200, false);
	// [2200, 2400)
	public static Range<Integer> ¢2200_2400c = new Range<Integer>().from(2200, true).to(2400, false);
	// [2400, 2600)
	public static Range<Integer> ¢2400_2600c = new Range<Integer>().from(2400, true).to(2600, false);
	// [2600, 2800)
	public static Range<Integer> ¢2600_2800c = new Range<Integer>().from(2600, true).to(2800, false);
	// [2800, 3000)
	public static Range<Integer> ¢2800_3000c = new Range<Integer>().from(2800, true).to(3000, false);

}
