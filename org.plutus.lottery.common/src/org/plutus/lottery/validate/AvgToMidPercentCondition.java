package org.plutus.lottery.validate;

import org.origin.common.util.StatUtil;
import org.plutus.lottery.common.Draw;

public class AvgToMidPercentCondition extends Condition<Integer> {

	public AvgToMidPercentCondition(Range<Integer> range) {
		super("avg/mid (均值/中值)");
		setRange(range);
	}

	@Override
	public boolean match(Draw draw) {
		int n1 = draw.getNum1();
		int n2 = draw.getNum2();
		int n3 = draw.getNum3();
		int n4 = draw.getNum4();
		int n5 = draw.getNum5();
		int mid = n3;

		int avg = (int) StatUtil.avg(2, n1, n2, n3, n4, n5);
		int avgToMidPercent = StatUtil.normalizeByPercentage(avg, mid, 100);

		this.actualValue = avgToMidPercent;

		boolean match = this.range.inRange(avgToMidPercent);
		return match;
	}

}
