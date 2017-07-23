package org.orbit.component.server.tier3.transferagent.editpolicy;

import org.orbit.component.server.tier3.transferagent.ws.TransferAgentServiceResource;
import org.origin.common.rest.agent.AbstractWSEditPolicy;
import org.origin.common.rest.agent.EditPolicyFactory;
import org.origin.common.rest.agent.EditPolicyFactoryRegistry;

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
	public AbstractWSEditPolicy createEditPolicy() {
		return new TransferAgentEditPolicy();
	}

}
