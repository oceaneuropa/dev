package org.origin.common.extensions.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Conditions implements ICondition {

	public enum OPERATOR {
		AND("AND"), //
		OR("OR"); //

		protected String operator;

		OPERATOR(String operator) {
			this.operator = operator;
		}

		public String getOperator() {
			return this.operator;
		}
	}

	protected List<ICondition> children = new ArrayList<ICondition>();

	protected OPERATOR operator;

	/**
	 * 
	 * @param operator
	 */
	public Conditions(OPERATOR operator) {
		this.operator = operator;
	}

	public ICondition[] getChildren() {
		return this.children.toArray(new ICondition[this.children.size()]);
	}

	/**
	 * 
	 * @param child
	 * @return
	 */
	public boolean add(ICondition child) {
		if (child != null && !this.children.contains(child)) {
			return this.children.add(child);
		}
		return false;
	}

	/**
	 * 
	 * @param child
	 * @return
	 */
	public boolean remove(ICondition child) {
		if (child != null && this.children.contains(child)) {
			return this.children.remove(child);
		}
		return false;
	}

	@Override
	public boolean evaluate(Object context, Object source, Object target, Map<String, Object> args) {
		if (OPERATOR.AND.equals(this.operator)) {
			// 'and' operator
			boolean hasTrue = false;
			boolean hasFalse = false;

			for (ICondition child : getChildren()) {
				try {
					if (child.evaluate(context, source, target, args)) {
						hasTrue = true;
					} else {
						hasFalse = true;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (hasTrue && !hasFalse) {
				return true;
			}

		} else if (OPERATOR.OR.equals(this.operator)) {
			// 'or' operator
			boolean hasTrue = false;

			for (ICondition child : getChildren()) {
				try {
					if (child.evaluate(context, source, target, args)) {
						hasTrue = true;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (hasTrue) {
				return true;
			}

		} else {
			// unrecognized/unspported operator
			System.err.println(getClass().getName() + " unspported operation: " + this.operator);

			ICondition[] children = getChildren();
			if (children != null && children.length == 1) {
				if (children[0].evaluate(context, source, target, args)) {
					return true;
				}
			}
		}

		return false;
	}

}
