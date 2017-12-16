package jersey.example3;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("order/select")
public class SelectOrder {

	public SelectOrder() {
		System.out.println("SelectOrder constructor");
	}

	/*
	 * @Context is used to inject UriInfo which in turn can return the parameters.
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public OrderBean getOrder(@Context UriInfo uriInfo) {
		String orderId = "";

		if (uriInfo.getQueryParameters().containsKey("id"))
			orderId = uriInfo.getQueryParameters().getFirst("id");

		OrderBean orderBean = null;
		if (!"".equals(orderId)) {
			orderBean = new OrderBean();
			orderBean.setOrderId(orderId);
			orderBean.setItemId("Item1");
			orderBean.setCustomerName("Ram");
		}

		return orderBean;
	}

}