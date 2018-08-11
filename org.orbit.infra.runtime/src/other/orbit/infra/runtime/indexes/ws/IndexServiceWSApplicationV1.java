package other.orbit.infra.runtime.indexes.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.orbit.infra.runtime.indexes.ws.IndexItemWSResource;
import org.orbit.infra.runtime.indexes.ws.IndexItemsWSResource;
import org.orbit.infra.runtime.indexes.ws.IndexServiceIndexTimer;
import org.orbit.infra.runtime.indexes.ws.IndexServiceWSResource;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.RestConstants;
import org.origin.common.rest.server.AbstractApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceWSApplicationV1 extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(IndexServiceWSApplicationV1.class);

	protected BundleContext bundleContext;
	protected String contextRoot;
	// protected IndexServiceIndexTimer serviceIndexTimer;
	protected IndexServiceIndexTimer serviceIndexTimer;

	public IndexServiceWSApplicationV1() {
	}

	public BundleContext getBundleContext() {
		return this.bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	@Override
	public void start() {
		System.out.println(getClass().getSimpleName() + ".start()");

		super.start();

		// Register web application
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(RestConstants.CONTEXT_ROOT, contextRoot);
		OSGiServiceUtil.register(this.bundleContext, Application.class, this, props);

		// Start timer for indexing the service
		// this.serviceIndexTimer = new IndexServiceIndexTimer(Activator.getIndexService());
		// this.serviceIndexTimer.start();
	}

	@Override
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");

		// Stop timer for indexing the service
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Unregister web application
		OSGiServiceUtil.unregister(Application.class, this);

		super.stop();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(IndexServiceWSResource.class);
		classes.add(IndexItemsWSResource.class);
		classes.add(IndexItemWSResource.class);

		// resolvers
		classes.add(IndexServiceResolver.class);

		return classes;
	}

}
