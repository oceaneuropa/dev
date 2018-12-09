package org.plutus.lottery.svg;

import java.util.ArrayList;
import java.util.List;

import org.origin.svg.graphics.Rectangle;
import org.origin.svg.widgets.Composite;
import org.origin.svg.widgets.Display;
import org.plutus.lottery.powerball.Draw;

public class Board extends Composite {

	protected Draw draw;
	protected List<Number> numbers = new ArrayList<Number>();

	/**
	 * 
	 * @param display
	 * @param draw
	 */
	public Board(Display display, Draw draw) {
		super(display);
		this.draw = draw;
	}

	public void createContents() {
		int number_x = 0;
		int number_y = 0 + 10; // 10 is title height
		int number_w = 10;
		int number_h = 10;

		for (int i = 1; i <= 69; i++) {
			Rectangle numBounds = new Rectangle(number_x, number_y, number_w, number_h);
			Number number = new Number(this, i, this.draw.numContains(i));
			number.setBounds(numBounds);
			number.createContents();
			this.numbers.add(number);

			number_x += number_w;
			if (i == 14 || i == 28 || i == 42 || i == 56) {
				number_x = 0;
				number_y += number_h;
			}
		}
	}

	public Draw getDraw() {
		return this.draw;
	}

	public List<Number> getNumbers() {
		return this.numbers;
	}

}
