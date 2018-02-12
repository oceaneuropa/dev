package org.plutus.lottery.validate;

import org.origin.common.util.StatUtil;
import org.plutus.lottery.powerball.Draw;

public class SumCondition extends Condition<Long> {

	public SumCondition(Range<Long> range) {
		super("sum (总和)");
		setRange(range);
	}

	@Override
	public boolean match(Draw draw) {
		int n1 = draw.getNum1();
		int n2 = draw.getNum2();
		int n3 = draw.getNum3();
		int n4 = draw.getNum4();
		int n5 = draw.getNum5();

		long sum = StatUtil.sum(n1, n2, n3, n4, n5);

		this.actualValue = sum;

		boolean match = this.range.inRange(sum);
		return match;
	}

}
