package org.orbit.spirit.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.orbit.spirit.runtime.earth.service.EarthService;
import org.orbit.spirit.runtime.earth.ws.EarthServiceAdapter;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.orbit.spirit.runtime.gaia.ws.GaiaServiceAdapter;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiritServices {

	protected static Logger LOG = LoggerFactory.getLogger(SpiritServices.class);

	private static SpiritServices INSTANCE = new SpiritServices();

	public static SpiritServices getInstance() {
		return INSTANCE;
	}

	protected Map<Object, Object> properties;
	protected ServiceConnectorAdapter<IndexProviderClient> indexProviderConnector;

	protected GaiaServiceAdapter gaiaServiceAdapter;
	protected EarthServiceAdapter earthServiceAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		this.properties = properties;

		this.indexProviderConnector = new ServiceConnectorAdapter<IndexProviderClient>(IndexProviderClient.class) {
			@Override
			public void connectorAdded(ServiceConnector<IndexProviderClient> connector) {
				doStart(bundleContext);
			}

			@Override
			public void connectorRemoved(ServiceConnector<IndexProviderClient> connector) {
			}
		};
		this.indexProviderConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		doStop(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void doStart(BundleContext bundleContext) {
		// Start service adapters
		this.gaiaServiceAdapter = new GaiaServiceAdapter(this.properties);
		this.gaiaServiceAdapter.start(bundleContext);

		this.earthServiceAdapter = new EarthServiceAdapter(this.properties);
		this.earthServiceAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void doStop(BundleContext bundleContext) {
		// Stop service adapters
		if (this.gaiaServiceAdapter != null) {
			this.gaiaServiceAdapter.stop(bundleContext);
			this.gaiaServiceAdapter = null;
		}

		if (this.earthServiceAdapter != null) {
			this.earthServiceAdapter.stop(bundleContext);
			this.earthServiceAdapter = null;
		}
	}

	public GaiaService getGaiaService() {
		return (this.gaiaServiceAdapter != null) ? this.gaiaServiceAdapter.getService() : null;
	}

	public EarthService getEarthService() {
		return (this.earthServiceAdapter != null) ? this.earthServiceAdapter.getService() : null;
	}

}