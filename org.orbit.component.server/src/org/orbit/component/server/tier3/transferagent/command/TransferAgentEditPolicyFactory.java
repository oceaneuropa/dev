package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.server.tier3.transferagent.ws.TransferAgentServiceResource;
import org.origin.common.rest.agent.AbstractEditPolicy;
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
	public AbstractEditPolicy createEditPolicy() {
		return new TransferAgentEditPolicy();
	}

}
