package org.origin.common.rest.agent;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.Activator;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommand;
import org.origin.common.command.ICommandResult;
import org.origin.common.command.ICommandStack;
import org.origin.common.command.IEditingDomain;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.server.AbstractApplicationResource;

/**
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 *
 */
public abstract class AbstractAgentResource extends AbstractApplicationResource {

	protected static AbstractEditPolicy[] EMPTY_EDIT_POLICIES = new AbstractEditPolicy[0];

	protected Map<String, AbstractEditPolicy> editPoliciesMap = new LinkedHashMap<String, AbstractEditPolicy>();

	public AbstractAgentResource() {
		createDefaultEditPolicies();
	}

	protected void createDefaultEditPolicies() {
		EditPolicyFactory[] factories = EditPolicyFactoryRegistry.INSTANCE.getFactories(getClass().getName());
		for (EditPolicyFactory factory : factories) {
			AbstractEditPolicy editPolicy = factory.createEditPolicy();
			if (editPolicy != null) {
				installEditPolicy(editPolicy.getRole(), editPolicy);
			}
		}
	}

	/**
	 * Returns the editing domain of this resource.
	 * 
	 * @return
	 */
	protected IEditingDomain getEditingDomain() {
		return Activator.getDefault().getEditingDomain();
	}

	/**
	 * Install a EditPolicy to the AgentResource.
	 * 
	 * @param role
	 * @param editPolicy
	 */
	public void installEditPolicy(String role, AbstractEditPolicy editPolicy) {
		assert role != null : "role is null";

		removeEditPolicy(role);

		editPolicy.setResource(this);
		editPolicy.activate();

		this.editPoliciesMap.put(role, editPolicy);
	}

	/**
	 * Remove a EditPolicy from the AgentResource.
	 * 
	 * @param role
	 */
	public void removeEditPolicy(String role) {
		assert role != null : "role is null";

		AbstractEditPolicy editPolicy = this.editPoliciesMap.get(role);
		if (editPolicy != null) {
			this.editPoliciesMap.remove(role);

			editPolicy.deactivate();
			editPolicy.setResource(null);
		}
	}

	public AbstractEditPolicy[] getEditPolicies() {
		Collection<AbstractEditPolicy> editPolicies = this.editPoliciesMap.values();
		if (editPolicies == null || editPolicies.isEmpty()) {
			return EMPTY_EDIT_POLICIES;
		}
		return editPolicies.toArray(new AbstractEditPolicy[editPolicies.size()]);
	}

	public AbstractEditPolicy getEditPolicies(String role) {
		assert role != null : "role is null";
		return this.editPoliciesMap.get(role);
	}

	/**
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
	 * 
	 * @param request
	 * @return
	 */
	@POST
	@Path("/request")
	@Produces(MediaType.APPLICATION_JSON)
	public Response onRequest(Request request) {
		Responses responses = new Responses();
		if (request != null) {
			responses.setRequestName(request.getLabel());

			ICommandStack commandStack = getEditingDomain().getCommandStack();
			CommandContext context = new CommandContext();

			for (Iterator<String> editPolicyItor = this.editPoliciesMap.keySet().iterator(); editPolicyItor.hasNext();) {
				String role = editPolicyItor.next();
				AbstractEditPolicy editPolicy = this.editPoliciesMap.get(role);
				if (editPolicy != null && editPolicy.understandsRequest(request)) {
					ICommand command = editPolicy.getCommand(request);
					if (command != null) {
						try {
							ICommandResult commandResult = commandStack.execute(context, command);
							if (commandResult != null) {
								editPolicy.createResponse(request, responses, commandResult);
							}
						} catch (CommandException e) {
							e.printStackTrace();
							editPolicy.createErrorResponse(request, responses, e);
						}
					}
				}
			}
		}
		return Response.ok().entity(responses).build();
	}

}
