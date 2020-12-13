package org.plutus.stock.resource;

import java.io.File;
import java.net.URI;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.util.FileUtil;
import org.origin.common.workingcopy.AbstractWorkingCopyFactory;
import org.origin.common.workingcopy.WorkingCopy;
import org.plutus.stock.common.StockConstants;

public class StockDataResourceFactory extends AbstractWorkingCopyFactory<StockDataResource> {

	public static final String FACTORY_NAME = "StockDataResourceFactory";

	public static StockDataResourceFactory INSTANCE = new StockDataResourceFactory();

	/**
	 * Register resource factory.
	 * 
	 */
	public static void register() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (!(factory instanceof StockDataResourceFactory)) {
			ResourceFactoryRegistry.INSTANCE.register(FACTORY_NAME, StockDataResourceFactory.INSTANCE);
		}
	}

	/**
	 * Unregister resource factory.
	 * 
	 */
	public static void unregister() {
		ResourceFactory<?> factory = ResourceFactoryRegistry.INSTANCE.getFactory(FACTORY_NAME);
		if (factory instanceof StockDataResourceFactory) {
			ResourceFactoryRegistry.INSTANCE.unregister(FACTORY_NAME);
		}
	}

	@Override
	public String getName() {
		return FACTORY_NAME;
	}

	@Override
	public boolean isSupported(URI uri) {
		File file = new File(uri);
		String fileExt = FileUtil.getExtension(file.getName());
		if (StockConstants.STOCK_DATA_FILE_EXTENSION.equalsIgnoreCase(fileExt)) {
			return true;
		}
		return false;
	}

	@Override
	public StockDataResource createResource(URI uri) {
		return new StockDataResource(uri);
	}

	@Override
	protected WorkingCopy createWorkingCopy(URI uri) {
		return new StockDataWorkingCopy(uri);
	}

}
