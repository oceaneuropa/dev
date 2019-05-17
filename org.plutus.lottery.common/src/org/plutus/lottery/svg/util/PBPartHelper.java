package org.plutus.lottery.svg.util;

import org.origin.svg.graphics.Point;
import org.plutus.lottery.svg.control.NumberPart;

public class PBPartHelper {

	public static PBPartHelper INSTANCE = new PBPartHelper();

	/**
	 * 
	 * @param numPart1
	 * @param numPart2
	 * @return
	 */
	public boolean isSameRow(NumberPart numPart1, NumberPart numPart2) {
		if (numPart1 != null && numPart2 != null) {
			Point p1 = numPart1.getIndex();
			Point p2 = numPart2.getIndex();

			if (p1.getY() == p2.getY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param numPart1
	 * @param numPart2
	 * @return
	 */
	public boolean isSameCol(NumberPart numPart1, NumberPart numPart2) {
		if (numPart1 != null && numPart2 != null) {
			Point p1 = numPart1.getIndex();
			Point p2 = numPart2.getIndex();

			if (p1.getX() == p2.getX()) {
				return true;
			}
		}
		return false;
	}

}
