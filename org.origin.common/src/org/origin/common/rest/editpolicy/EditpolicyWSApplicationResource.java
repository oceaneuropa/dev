package org.origin.common.rest.editpolicy;

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
import org.origin.common.command.ICommandStack;
import org.origin.common.command.IEditingDomain;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/**
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 *
 */
public class EditpolicyWSApplicationResource extends AbstractWSApplicationResource {

	protected static AbstractWSEditPolicyV1[] EMPTY_EDIT_POLICIES = new AbstractWSEditPolicyV1[0];

	protected Map<String, AbstractWSEditPolicyV1> editPoliciesMap = new LinkedHashMap<String, AbstractWSEditPolicyV1>();

	public EditpolicyWSApplicationResource() {
		createDefaultEditPolicies();
	}

	protected void createDefaultEditPolicies() {
		EditPolicyFactory[] factories = EditPolicyFactoryRegistry.INSTANCE.getFactories(getClass().getName());
		for (EditPolicyFactory factory : factories) {
			AbstractWSEditPolicyV1 editPolicy = factory.createEditPolicy();
			if (editPolicy != null) {
				installEditPolicy(editPolicy.getRole(), editPolicy);
			}
		}
		createEditPolicies();
	}

	protected void createEditPolicies() {
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
	 * Install a EditPolicy to the ws resource.
	 * 
	 * @param editPolicy
	 */
	public void installEditPolicy(AbstractWSEditPolicyV1 editPolicy) {
		installEditPolicy(editPolicy.getRole(), editPolicy);
	}

	/**
	 * Install a EditPolicy to the ws resource.
	 * 
	 * @param role
	 * @param editPolicy
	 */
	public void installEditPolicy(String role, AbstractWSEditPolicyV1 editPolicy) {
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

		AbstractWSEditPolicyV1 editPolicy = this.editPoliciesMap.get(role);
		if (editPolicy != null) {
			this.editPoliciesMap.remove(role);

			editPolicy.deactivate();
			editPolicy.setResource(null);
		}
	}

	public AbstractWSEditPolicyV1[] getEditPolicies() {
		Collection<AbstractWSEditPolicyV1> editPolicies = this.editPoliciesMap.values();
		if (editPolicies == null || editPolicies.isEmpty()) {
			return EMPTY_EDIT_POLICIES;
		}
		return editPolicies.toArray(new AbstractWSEditPolicyV1[editPolicies.size()]);
	}

	public AbstractWSEditPolicyV1 getEditPolicies(String role) {
		assert role != null : "role is null";
		return this.editPoliciesMap.get(role);
	}

	public <T> T getService(Class<T> serviceClass) {
		return null;
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
			responses.setRequestName(request.getRequestName());

			ICommandStack commandStack = getEditingDomain().getCommandStack();
			CommandContext context = new CommandContext();
			context.adapt(Responses.class, responses);

			// CompositeCommandResult commandResults = new CompositeCommandResult();
			for (Iterator<String> editPolicyItor = this.editPoliciesMap.keySet().iterator(); editPolicyItor.hasNext();) {
				String role = editPolicyItor.next();
				AbstractWSEditPolicyV1 editPolicy = this.editPoliciesMap.get(role);
				if (editPolicy != null) {
					ICommand command = editPolicy.getCommand(this, request);
					if (command != null) {
						try {
							commandStack.execute(context, command);
							// commandResults.add(commandResult);
							// ICommandResult commandResult = commandStack.execute(context, command);
							// if (commandResult != null) {
							// editPolicy.createResponse(request, responses, commandResult);
							// }
						} catch (CommandException e) {
							e.printStackTrace();
							// editPolicy.createErrorResponse(request, responses, e);
							// responses.setResponse(request.getRequestName(), new org.origin.common.rest.model.Response("exception", e.getMessage(), e));
						}
					}
				}
			}
		}
		return Response.ok().entity(responses).build();
	}

}
