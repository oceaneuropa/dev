package org.plutus.lottery.svg.control;

import java.util.ArrayList;
import java.util.List;

import org.origin.svg.graphics.Rectangle;
import org.origin.svg.widgets.Composite;
import org.origin.svg.widgets.Display;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.svg.PB;

public class YearPart extends Composite {

	protected Integer year;
	protected List<Draw> draws;
	protected List<DrawPart> drawParts = new ArrayList<DrawPart>();

	/**
	 * 
	 * @param display
	 * @param draw
	 */
	public YearPart(Display display, Integer year, List<Draw> draws) {
		super(display);
		this.year = year;
		this.draws = (draws != null) ? draws : new ArrayList<Draw>();
	}

	/**
	 * 
	 * @param parent
	 * @param year
	 * @param draws
	 */
	public YearPart(Composite parent, Integer year, List<Draw> draws) {
		super(parent);
		this.year = year;
		this.draws = (draws != null) ? draws : new ArrayList<Draw>();
	}

	public Integer getYear() {
		return this.year;
	}

	public List<Draw> getDraws() {
		return this.draws;
	}

	public List<DrawPart> getDrawParts() {
		return this.drawParts;
	}

	public void createContents() {
		Rectangle bounds = this.getBounds();
		int width = bounds.getWidth();

		int draw_x = 0;
		int draw_y = 0 + 10; // 10 is title height
		int draw_w = 140;
		int draw_h = 50;

		for (Draw draw : this.draws) {
			Rectangle currBounds = new Rectangle(draw_x, draw_y, draw_w, draw_h);

			DrawPart drawPart = new DrawPart(this, draw, PB.DRAW_SQUARE_14x05);
			drawPart.setBounds(currBounds);
			drawPart.createContents();
			this.drawParts.add(drawPart);

			draw_x += draw_w + 10;
			if (draw_x + draw_w > width) {
				draw_x = 0 + 10; // 10 is for top shift
				draw_y += draw_h + 10 + 4; // 4 is for distance between rows
			}
		}
	}

}
