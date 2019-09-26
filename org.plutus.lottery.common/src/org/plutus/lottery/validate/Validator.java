package org.plutus.lottery.validate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.plutus.lottery.common.Draw;

public class Validator {

	public static Validator INSTANCE = new Validator();

	/**
	 * 
	 * @param draw
	 * @param conditions
	 */
	public void validate(Draw draw, Condition<?>... conditions) {
		String result = "";
		for (Condition<?> condition : conditions) {
			boolean match = condition.match(draw);
			String currResult = condition + " -> " + match;
			result += currResult + "\t";
		}

		System.out.print(draw.toString() + "\t");
		System.out.println(result);
	}

	public static void main(String[] args) {
		List<Draw> draws = new ArrayList<Draw>();
		Draw draw1 = new Draw(new Date(), 3, 26, 37, 58, 63, 22);
		draws.add(draw1);

		Condition<Integer> avgToMidCondition = new AvgToMidPercentCondition(new Range<Integer>("[", 80, 100, "]"));
		Condition<Integer> diffAvgCondition = new DiffAvgCondition(new Range<Integer>("[", 9, 10, "]"));
		Condition<Long> sumCondition = new SumCondition(new Range<Long>("[", (long) 130, (long) 170, "]"));

		for (Draw draw : draws) {
			Validator.INSTANCE.validate(draw, avgToMidCondition, diffAvgCondition, sumCondition);
		}
	}

}
