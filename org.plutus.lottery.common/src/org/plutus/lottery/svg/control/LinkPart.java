package org.plutus.lottery.svg.control;

import org.origin.svg.widgets.Composite;
import org.origin.svg.widgets.Control;
import org.origin.svg.widgets.Display;

public class LinkPart extends Control {

	protected NumberPart source;
	protected NumberPart target;

	/**
	 * 
	 * @param display
	 * @param source
	 * @param target
	 */
	public LinkPart(Display display, NumberPart source, NumberPart target) {
		super(display);
		checkSource(source);
		checkTarget(target);

		this.source = source;
		this.target = target;
	}

	/**
	 * 
	 * @param parent
	 * @param source
	 * @param target
	 */
	public LinkPart(Composite parent, NumberPart source, NumberPart target) {
		super(parent);
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

}
