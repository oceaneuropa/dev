package org.origin.common.rest.editpolicy;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.model.Request;

public class WSEditPoliciesSupport implements WSEditPolicies {

	protected List<WSEditPolicy> editPolicies = new ArrayList<WSEditPolicy>();

	@Override
	public synchronized List<WSEditPolicy> getEditPolicies() {
		return this.editPolicies;
	}

	@Override
	public synchronized boolean installEditPolicy(WSEditPolicy editPolicy) {
		if (editPolicy == null || this.editPolicies.contains(editPolicy)) {
			return false;
		}
		return this.editPolicies.add(editPolicy);
	}

	@Override
	public synchronized boolean uninstallEditPolicy(WSEditPolicy editPolicy) {
		if (editPolicy == null || !this.editPolicies.contains(editPolicy)) {
			return false;
		}
		return this.editPolicies.remove(editPolicy);
	}

	@Override
	public synchronized WSCommand getCommand(Request request) {
		WSCommand command = null;
		for (WSEditPolicy editPolicy : this.editPolicies) {
			WSCommand currCommand = editPolicy.getCommand(request);
			if (currCommand != null) {
				command = currCommand;
				break;
			}
		}
		return command;
	}

}
