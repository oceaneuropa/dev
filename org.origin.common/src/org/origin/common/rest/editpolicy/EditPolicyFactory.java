package org.origin.common.rest.editpolicy;

public interface EditPolicyFactory {

	/**
	 * Create new EditPolicy instance.
	 * 
	 * @return
	 */
	public AbstractWSEditPolicyV1 createEditPolicy();

}
