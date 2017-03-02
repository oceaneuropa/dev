package org.plutus.lottery.powerball;

import java.util.HashMap;
import java.util.Map;

public class DrawStat {

	// Basic
	public static String PROP_MIN = "PROP_MIN"; // 最小值
	public static String PROP_MAX = "PROP_MAX"; // 最大值
	public static String PROP_MID = "PROP_MID"; // 中值
	public static String PROP_SUM = "PROP_SUM"; // 总和
	public static String PROP_AVG = "PROP_AVG"; // 均值
	public static String PROP_AVG_VS_MID = "PROP_AVG_VS_MID"; // 均值和中值之比
	public static String PROP_AVG_VS_MID_BY_PERCENTAGE = "PROP_AVG_VS_MID_BY_PERCENTAGE"; // 均值和中值之比 标准化为百分比
	public static String PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED = "PROP_AVG_VS_MID_BY_PERCENTAGE_ZERO_BASED"; // 均值和中值之比 标准化为百分比再减去100
	public static String PROP_ODD_COUNT = "PROP_ODD_COUNT"; // 奇数个数
	public static String PROP_EVEN_COUNT = "PROP_EVEN_COUNT"; // 偶数个数
	public static String PROP_DIST_AVG = "PROP_DIST_AVG"; // 间距均值

	// Normal Distribution
	// 总和
	public static String PROP_SUM_LIST = "PROP_SUM_LIST"; // (不重复的)总和值列表
	public static String PROP_SUM_TO_COUNT_MAP = "PROP_SUM_TO_COUNT_MAP"; // 总和值与其重复次数的Map
	public static String PROP_COUNT_TO_SUM_LIST_MAP = "PROP_COUNT_TO_SUM_LIST_MAP"; // 出现次数与总和值列表的Map
	public static String PROP_SUM_TOTAL_COUNT = "PROP_SUM_TOTAL_COUNT"; // 所有总和的全部重复次数

	public static String PROP_SUM_MID_INDEX = "PROP_SUM_MID_INDEX"; // 总和列表的中值索引
	public static String PROP_SUM_UP___10_PERCENT_INDEX = "PROP_SUM_UP___10_PERCENT_INDEX"; // 中值索引向上10%的索引
	public static String PROP_SUM_DOWN_10_PERCENT_INDEX = "PROP_SUM_DOWN_10_PERCENT_INDEX"; // 中值索引向下10%的索引
	public static String PROP_SUM_UP___20_PERCENT_INDEX = "PROP_SUM_UP___20_PERCENT_INDEX"; // 中值索引向上20%的索引
	public static String PROP_SUM_DOWN_20_PERCENT_INDEX = "PROP_SUM_DOWN_20_PERCENT_INDEX"; // 中值索引向下20%的索引
	public static String PROP_SUM_UP___30_PERCENT_INDEX = "PROP_SUM_UP___30_PERCENT_INDEX"; // 中值索引向上30%的索引
	public static String PROP_SUM_DOWN_30_PERCENT_INDEX = "PROP_SUM_DOWN_30_PERCENT_INDEX"; // 中值索引向下30%的索引
	public static String PROP_SUM_UP___34_PERCENT_INDEX = "PROP_SUM_UP___34_PERCENT_INDEX"; // 中值索引向上34%的索引
	public static String PROP_SUM_DOWN_34_PERCENT_INDEX = "PROP_SUM_DOWN_34_PERCENT_INDEX"; // 中值索引向下34%的索引
	// 平均间距
	public static String PROP_DIST_AVG_LIST = "PROP_DIST_AVG_LIST"; // (不重复的)平均间距列表
	public static String PROP_DIST_AVG_TO_COUNT_MAP = "PROP_DIST_AVG_TO_COUNT_MAP"; // 平均间距与其重复次数的Map
	public static String PROP_COUNT_TO_DIST_AVG_LIST_MAP = "PROP_COUNT_TO_DIST_AVG_LIST_MAP"; // 出现次数与平均间距列表的Map
	public static String PROP_DIST_AVG_TOTAL_COUNT = "PROP_DIST_AVG_TOTAL_COUNT"; // 所有平均间距的全部重复次数

	protected Map<String, Object> statMap = new HashMap<String, Object>();

	public void set(String prop, Object value) {
		this.statMap.put(prop, value);
	}

	@SuppressWarnings("unchecked")
	public <V> V get(String prop, Class<V> valueClass) {
		Object value = this.statMap.get(prop);
		if (value != null && valueClass.isAssignableFrom(value.getClass())) {
			return (V) value;
		}
		return null;
	}

	public boolean contains(String prop) {
		return this.statMap.containsKey(prop);
	}

}
