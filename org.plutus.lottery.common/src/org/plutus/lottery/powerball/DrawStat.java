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
	public static String PROP_DISTANCE_AVG = "PROP_DISTANCE_AVG"; // 间距均值

	// Normal Distribution
	public static String PROP_SUM_TO_COUNT_MAP = "PROP_SUM_TO_COUNT_MAP"; // 总和与其出现次数Map
	public static String PROP_SUM_MIN_COUNT = "PROP_SUM_MIN_COUNT"; // 总和最小值
	public static String PROP_SUM_MAX_COUNT = "PROP_SUM_MAX_COUNT"; // 总和最大值

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
