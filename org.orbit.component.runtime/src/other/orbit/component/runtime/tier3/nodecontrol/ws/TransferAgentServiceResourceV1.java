package other.orbit.component.runtime.tier3.nodecontrol.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.origin.common.command.EditingDomain;
import org.origin.common.rest.editpolicy.other.EditpolicyWSApplicationResource;

import other.orbit.component.runtime.tier3.nodecontrol.editpolicy.NodeEditPolicyV1;
import other.orbit.component.runtime.tier3.nodecontrol.editpolicy.NodespaceEditPolicyV1;

/**
 * Transfer agent web service resource.
 * 
 * {contextRoot} example: /orbit/v1/ta
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/inbound (Body parameter: ChannelMessageDTO)
 *
 * @see HomeAgentResource
 * @see HomeWorkspacesResource
 * 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransferAgentServiceResourceV1 extends EditpolicyWSApplicationResource {

	@Inject
	public NodeControlService service;

	public NodeControlService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("TransferAgentService is not available.");
		}
		return this.service;
	}

	@Override
	protected EditingDomain getEditingDomain() {
		NodeControlService service = getService();
		// return service.getEditingDomain();
		return null;
	}

	@Override
	protected void createEditPolicies() {
		// TransferAgentService service = getService();
		NodespaceEditPolicyV1 nodespaceEditPolicy = new NodespaceEditPolicyV1(service);
		NodeEditPolicyV1 nodeEditPolicy = new NodeEditPolicyV1(service);

		installEditPolicy(nodespaceEditPolicy);
		installEditPolicy(nodeEditPolicy);
	}

}

// @GET
// @Path("ping")
// @Produces(MediaType.APPLICATION_JSON)
// public Response ping() {
// TransferAgentService service = getService();
// if (service != null) {
// return Response.ok(1).build();
// }
// return Response.ok(0).build();
// }