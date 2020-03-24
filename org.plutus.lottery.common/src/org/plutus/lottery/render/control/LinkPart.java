package org.plutus.lottery.render.control;

import org.origin.wwt.widgets.WebWidget;
import org.origin.wwt.widgets.layout.WWT;

public class LinkPart extends WebWidget {

	protected NumberPart source;
	protected NumberPart target;
	protected boolean isPredicted;
	protected String strokeColor;

	// /**
	// *
	// * @param display
	// * @param source
	// * @param target
	// */
	// public LinkPart(Display display, NumberPart source, NumberPart target) {
	// super(display);
	// checkSource(source);
	// checkTarget(target);
	//
	// this.source = source;
	// this.target = target;
	// }

	/**
	 * 
	 * @param parent
	 * @param source
	 * @param target
	 */
	public LinkPart(WebWidget parent, NumberPart source, NumberPart target) {
		super(parent, WWT.NONE);
		checkSource(source);
		checkTarget(target);

		this.source = source;
		this.target = target;
	}

	protected void checkSource(NumberPart source) {
		if (source == null) {
			throw new IllegalArgumentException("source is null");
		}
	}

	protected void checkTarget(NumberPart target) {
		if (target == null) {
			throw new IllegalArgumentException("target is null");
		}
	}

	public void createContents() {
	}

	public NumberPart getSource() {
		return this.source;
	}

	public void setSource(NumberPart source) {
		this.source = source;
	}

	public NumberPart getTarget() {
		return this.target;
	}

	public void setTarget(NumberPart target) {
		this.target = target;
	}

	public boolean isPredicted() {
		return this.isPredicted;
	}

	public void setPredicted(boolean isPredicted) {
		this.isPredicted = isPredicted;
	}

	public String getStrokeColor() {
		return this.strokeColor;
	}

	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

}
