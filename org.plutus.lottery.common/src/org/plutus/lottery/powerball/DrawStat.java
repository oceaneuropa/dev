package org.plutus.lottery.powerball;

import java.util.HashMap;
import java.util.Map;

public class DrawStat {

	// --------------------------------------------------------------------------------------------------
	// Basic
	// --------------------------------------------------------------------------------------------------
	// 最小值
	public static String PROP_MIN = "PROP_MIN";
	// 最大值
	public static String PROP_MAX = "PROP_MAX";
	// 中值
	public static String PROP_MID = "PROP_MID";
	// 总和
	public static String PROP_SUM = "PROP_SUM";
	// 均值
	public static String PROP_AVG = "PROP_AVG";

	// 单个数字的均值和标准差 (standard deviation - SD)
	public static String NUM1_AVG = "NUM1_AVG";
	public static String NUM2_AVG = "NUM2_AVG";
	public static String NUM3_AVG = "NUM3_AVG";
	public static String NUM4_AVG = "NUM4_AVG";
	public static String NUM5_AVG = "NUM5_AVG";
	public static String PB_AVG = "PB_AVG";

	public static String NUM1_SD = "NUM1_SD";
	public static String NUM2_SD = "NUM2_SD";
	public static String NUM3_SD = "NUM3_SD";
	public static String NUM4_SD = "NUM4_SD";
	public static String NUM5_SD = "NUM5_SD";
	public static String PB_SD = "PB_SD";

	// 均值和中值之比
	public static String PROP_AVG_VS_MID = "PROP_AVG_VS_MID";
	// 均值和中值之比 标准化为百分比
	public static String PROP_AVG_VS_MID_BY_PERCENTAGE = "PROP_AVG_VS_MID_BY_PERCENTAGE";
	// 均值和中值之比 标准化为百分比再减去100
	public static String PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED = "PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED";
	// 奇数个数
	public static String PROP_ODD_COUNT = "PROP_ODD_COUNT";
	// 偶数个数
	public static String PROP_EVEN_COUNT = "PROP_EVEN_COUNT";
	// 间距均值
	public static String PROP_DIST_AVG = "PROP_DIST_AVG";

	// 后一组和当前组每个数字差值 (后一组减去当前组对应数字;后一组必须存在;)
	public static String NUM1_DIFF = "NUM1_DIFF";
	public static String NUM2_DIFF = "NUM2_DIFF";
	public static String NUM3_DIFF = "NUM3_DIFF";
	public static String NUM4_DIFF = "NUM4_DIFF";
	public static String NUM5_DIFF = "NUM5_DIFF";
	public static String PB_DIFF = "PB_DIFF";

	public static String NUM1_DIFF_MIN = "NUM1_DIFF_MIN";
	public static String NUM1_DIFF_MAX = "NUM1_DIFF_MAX";

	public static String NUM2_DIFF_MIN = "NUM2_DIFF_MIN";
	public static String NUM2_DIFF_MAX = "NUM2_DIFF_MAX";

	public static String NUM3_DIFF_MIN = "NUM3_DIFF_MIN";
	public static String NUM3_DIFF_MAX = "NUM3_DIFF_MAX";

	public static String NUM4_DIFF_MIN = "NUM4_DIFF_MIN";
	public static String NUM4_DIFF_MAX = "NUM4_DIFF_MAX";

	public static String NUM5_DIFF_MIN = "NUM5_DIFF_MIN";
	public static String NUM5_DIFF_MAX = "NUM5_DIFF_MAX";

	public static String PB_DIFF_MIN = "PB_DIFF_MIN";
	public static String PB_DIFF_MAX = "PB_DIFF_MAX";

	public static String PROP_RANGE_1_TO_100 = "PROP_RANGE_1_100";

	// --------------------------------------------------------------------------------------------------
	// BasicCount
	// --------------------------------------------------------------------------------------------------
	// 平均值的重复次数 以及百分比
	public static String PROP_AVG_COUNT_MAP = "PROP_AVG_COUNT_MAP";
	public static String PROP_AVG_PERCENT_MAP = "PROP_AVG_PERCENT_MAP";
	// 奇数偶数比的重复次数 以及百分比
	public static String PROP_ODD_TO_EVEN_COUNT_MAP = "PROP_ODD_TO_EVEN_COUNT_MAP";
	public static String PROP_ODD_TO_EVEN_PERCENT_MAP = "PROP_ODD_TO_EVEN_PERCENT_MAP";

	// 最近10期重复最多数字(前10位)
	public static String PROP_NUMBER_COUNTS_10DRAWS_FIRST10 = "PROP_NUMBER_COUNTS_10DRAWS_FIRST10";
	// 最近20期重复最多数字(前10位)
	public static String PROP_NUMBER_COUNTS_20DRAWS_FIRST10 = "PROP_NUMBER_COUNTS_20DRAWS_FIRST10";
	// 最近30期重复最多数字(前10位)
	public static String PROP_NUMBER_COUNTS_30DRAWS_FIRST10 = "PROP_NUMBER_COUNTS_30DRAWS_FIRST10";

	// 首位数字组合的计数统计
	public static String PROP_FIRST_DIGITS_TO_COUNT_MAP = "PROP_FIRST_DIGITS_TO_COUNT_MAP";
	public static String PROP_FIRST_DIGITS_TO_COUNT_TREEMAP = "PROP_FIRST_DIGITS_TO_COUNT_TREEMAP";
	public static String PROP_FIRST_DIGITS_TO_COUNT_LIST = "PROP_FIRST_DIGITS_TO_COUNT_LIST";

	// --------------------------------------------------------------------------------------------------
	// Combination
	// --------------------------------------------------------------------------------------------------
	public static String PROP_COMBIN3_NUMS = "PROP_COMBIN3_NUMS";
	public static String PROP_COMBIN4_NUMS = "PROP_COMBIN4_NUMS";
	public static String PROP_COMBIN5_NUMS = "PROP_COMBIN5_NUMS";

	public static String PROP_COMBIN3_REPORTS = "PROP_COMBIN3_REPORTS";
	public static String PROP_COMBIN4_REPORTS = "PROP_COMBIN4_REPORTS";
	public static String PROP_COMBIN5_REPORTS = "PROP_COMBIN5_REPORTS";

	public static String PROP_COMBIN3_AVG_DISTANCE = "PROP_COMBIN3_AVG_DISTANCE";
	public static String PROP_COMBIN4_AVG_DISTANCE = "PROP_COMBIN4_AVG_DISTANCE";
	public static String PROP_COMBIN5_AVG_DISTANCE = "PROP_COMBIN5_AVG_DISTANCE";

	public static String PROP_COMBIN3_MIN_DISTANCE = "PROP_COMBIN3_MIN_DISTANCE";
	public static String PROP_COMBIN4_MIN_DISTANCE = "PROP_COMBIN4_MIN_DISTANCE";
	public static String PROP_COMBIN5_MIN_DISTANCE = "PROP_COMBIN5_MIN_DISTANCE";

	public static String PROP_COMBIN3_MAX_DISTANCE = "PROP_COMBIN3_MAX_DISTANCE";
	public static String PROP_COMBIN4_MAX_DISTANCE = "PROP_COMBIN4_MAX_DISTANCE";
	public static String PROP_COMBIN5_MAX_DISTANCE = "PROP_COMBIN5_MAX_DISTANCE";

	public static String PROP_COMBIN3_RANGE_COUNT_MAP = "PROP_COMBIN3_RANGE_COUNT_MAP";
	public static String PROP_COMBIN4_RANGE_COUNT_MAP = "PROP_COMBIN4_RANGE_COUNT_MAP";
	public static String PROP_COMBIN5_RANGE_COUNT_MAP = "PROP_COMBIN5_RANGE_COUNT_MAP";

	// --------------------------------------------------------------------------------------------------
	// Normal Distribution
	// --------------------------------------------------------------------------------------------------
	// 总和
	// 总和值列表
	public static String PROP_SUM_LIST = "PROP_SUM_LIST";
	// 总和值与其重复次数的Map
	public static String PROP_SUM_TO_COUNT_MAP = "PROP_SUM_TO_COUNT_MAP";
	// 出现次数与总和值列表的Map
	public static String PROP_COUNT_TO_SUM_LIST_MAP = "PROP_COUNT_TO_SUM_LIST_MAP";
	// 所有总和的全部重复次数
	public static String PROP_SUM_TOTAL_COUNT = "PROP_SUM_TOTAL_COUNT";
	// 总和列表的中值索引
	public static String PROP_SUM_MID_INDEX = "PROP_SUM_MID_INDEX";
	// 中值索引向上和向下10%的索引
	public static String PROP_SUM_UP___10_PERCENT_INDEX = "PROP_SUM_UP___10_PERCENT_INDEX";
	public static String PROP_SUM_DOWN_10_PERCENT_INDEX = "PROP_SUM_DOWN_10_PERCENT_INDEX";
	// 中值索引向上和向下20%的索引
	public static String PROP_SUM_UP___20_PERCENT_INDEX = "PROP_SUM_UP___20_PERCENT_INDEX";
	public static String PROP_SUM_DOWN_20_PERCENT_INDEX = "PROP_SUM_DOWN_20_PERCENT_INDEX";
	// 中值索引向上和向下30%的索引
	public static String PROP_SUM_UP___30_PERCENT_INDEX = "PROP_SUM_UP___30_PERCENT_INDEX";
	public static String PROP_SUM_DOWN_30_PERCENT_INDEX = "PROP_SUM_DOWN_30_PERCENT_INDEX";
	// 中值索引向上和向下34%的索引
	public static String PROP_SUM_UP___34_PERCENT_INDEX = "PROP_SUM_UP___34_PERCENT_INDEX";
	public static String PROP_SUM_DOWN_34_PERCENT_INDEX = "PROP_SUM_DOWN_34_PERCENT_INDEX";

	// 平均间距
	// (不重复的)平均间距列表
	public static String PROP_DIST_AVG_LIST = "PROP_DIST_AVG_LIST";
	// 平均间距与其重复次数的Map
	public static String PROP_DIST_AVG_TO_COUNT_MAP = "PROP_DIST_AVG_TO_COUNT_MAP";
	// 出现次数与平均间距列表的Map
	public static String PROP_COUNT_TO_DIST_AVG_LIST_MAP = "PROP_COUNT_TO_DIST_AVG_LIST_MAP";
	// 所有平均间距的全部重复次数
	public static String PROP_DIST_AVG_TOTAL_COUNT = "PROP_DIST_AVG_TOTAL_COUNT";

	protected Map<String, Object> statMap = new HashMap<String, Object>();

	/**
	 * Set property.
	 * 
	 * @param prop
	 * @param value
	 */
	public void put(String prop, Object value) {
		this.statMap.put(prop, value);
	}

	/**
	 * Get property.
	 * 
	 * @param prop
	 * @param valueClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V> V get(String prop, Class<V> valueClass) {
		Object value = this.statMap.get(prop);
		if (value != null && valueClass.isAssignableFrom(value.getClass())) {
			return (V) value;
		}
		return null;
	}

	/**
	 * Check whether contains property.
	 * 
	 * @param prop
	 * @return
	 */
	public boolean contains(String prop) {
		return this.statMap.containsKey(prop);
	}

}
