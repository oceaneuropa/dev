package org.plutus.lottery.validate;

import org.origin.common.util.StatUtil;
import org.plutus.lottery.common.Draw;

public class DiffAvgCondition extends Condition<Integer> {

	public DiffAvgCondition(Range<Integer> range) {
		super("avg diff (平均间距)");
		setRange(range);
	}

	@Override
	public boolean match(Draw draw) {
		int n1 = draw.getNum1();
		int n2 = draw.getNum2();
		int n3 = draw.getNum3();
		int n4 = draw.getNum4();
		int n5 = draw.getNum5();

		int dist_avg = (int) StatUtil.diff_avg(2, n1, n2, n3, n4, n5);

		this.actualValue = dist_avg;

		boolean match = this.range.inRange(dist_avg);
		return match;
	}

}
