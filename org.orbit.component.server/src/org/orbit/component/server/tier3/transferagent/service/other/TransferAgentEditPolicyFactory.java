package org.orbit.component.server.tier3.transferagent.service.other;

import org.orbit.component.server.tier3.transferagent.ws.TransferAgentServiceResource;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditPolicyFactory;
import org.origin.common.rest.editpolicy.EditPolicyFactoryRegistry;

/**
 * @see HomeAgentEditPolicyFactory
 *
 */
public class TransferAgentEditPolicyFactory implements EditPolicyFactory {

	public static TransferAgentEditPolicyFactory INSTANCE = new TransferAgentEditPolicyFactory();

	public static void register() {
		EditPolicyFactoryRegistry.INSTANCE.register(TransferAgentServiceResource.class.getName(), TransferAgentEditPolicyFactory.INSTANCE);
	}

	public static void unregister() {
		EditPolicyFactoryRegistry.INSTANCE.unregister(TransferAgentServiceResource.class.getName(), TransferAgentEditPolicyFactory.INSTANCE);
	}

	@Override
	public AbstractWSEditPolicyV1 createEditPolicy() {
		return new TransferAgentEditPolicy();
	}

}
