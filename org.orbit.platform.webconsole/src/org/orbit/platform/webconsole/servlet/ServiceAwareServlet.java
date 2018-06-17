package org.orbit.platform.webconsole.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class ServiceAwareServlet extends HttpServlet {

	private static final long serialVersionUID = 7776058348000038134L;

	protected ServiceContext serviceContext;

	@Override
	public void init() throws ServletException {
		this.serviceContext = (ServiceContext) getServletContext().getAttribute("serviceContext");
	}

	protected ServiceContext getServiceContext() {
		return this.serviceContext;
	}

}
