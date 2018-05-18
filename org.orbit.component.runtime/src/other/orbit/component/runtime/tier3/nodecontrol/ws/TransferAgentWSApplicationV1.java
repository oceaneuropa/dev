package other.orbit.component.runtime.tier3.nodecontrol.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier3.nodecontrol.ws.NodeControlWSResource;
import org.osgi.framework.BundleContext;

/**
 * https://www.programcreek.com/java-api-examples/index.php?source_dir=para-master/para-server/src/main/java/com/erudika/para/rest/Api1.java
 *
 */
public class TransferAgentWSApplicationV1 extends OrbitWSApplication {

	protected NodeControlService service;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 * @param feature
	 */
	public TransferAgentWSApplicationV1(final BundleContext bundleContext, final NodeControlService service, int feature) {
		super(service.getContextRoot(), feature);
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(NodeControlService.class);
			}
		});
		register(NodeControlWSResource.class);
	}

	public NodeControlService getService() {
		return this.service;
	}

}

// protected ResourcesWebServiceApplication resourceWSApp1;
// protected ResourcesWebServiceApplication resourceWSApp2;

// Resource.Builder idResource = Resource.builder("id/{id}");
// idResource.addMethod(GET).produces(JSON).handledBy(echoResourceGetHandler());
// registerResources(idResource.build());

// Register TransferAgentEditPolicy for TransferAgentServiceResource
// TransferAgentEditPolicyFactory.register();

// Unregister TransferAgentEditPolicy for TransferAgentServiceResource
// TransferAgentEditPolicyFactory.unregister();

// protected TransferAgentServiceTimer serviceIndexTimer;

// @Override
// public Set<Class<?>> getClasses() {
// Set<Class<?>> classes = new HashSet<Class<?>>();
//
// // resources
// classes.add(TransferAgentServiceResource.class);
//
// // resolvers
// classes.add(TransferAgentResolver.class);
//
// // http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
// // In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
// // Add additional features such as support for Multipart.
// classes.add(MultiPartFeature.class);
//
// return classes;
// }

// private Locale getLocale(String localeStr) {
// try {
// return LocaleUtils.toLocale(localeStr);
// } catch (Exception e) {
// return Locale.US;
// }
// }

// protected Inflector<ContainerRequestContext, Response> echoResourceGetHandler() {
// return new Inflector<ContainerRequestContext, Response>() {
// @Override
// public Response apply(ContainerRequestContext requestContext) {
// String message = getPathParam("message", requestContext);
// return Response.ok("echo: '" + message + "' from Server").build();
// }
// };
// }

// @Override
// public void start() {
// LOG.info("start()");
// super.start();
//
// // String contextRoot1 = this.contextRoot + "/root1"; // e.g. /orbit/v1/ta/root1
// // ResourceService resourceService1 = new ResourceService();
// // resourceService1.setName("root1");
// // resourceService1.setContextRoot(contextRoot1);
// // this.resourceWSApp1 = new ResourcesWebServiceApplication(this.bundleContext, resourceService1);
// //
// // String contextRoot2 = this.contextRoot + "/root2"; // e.g. /orbit/v1/ta/root2
// // ResourceService resourceService2 = new ResourceService();
// // resourceService2.setName("root2");
// // resourceService2.setContextRoot(contextRoot2);
// // this.resourceWSApp2 = new ResourcesWebServiceApplication(this.bundleContext, resourceService2);
// //
// // this.resourceWSApp1.start();
// // this.resourceWSApp2.start();
// }
//
// @Override
// public void stop() {
// LOG.info("stop()");
//
// // if (this.resourceWSApp1 != null) {
// // this.resourceWSApp1.stop();
// // this.resourceWSApp1 = null;
// // }
// // if (this.resourceWSApp2 != null) {
// // this.resourceWSApp2.stop();
// // this.resourceWSApp2 = null;
// // }
//
// super.stop();
// }
