package org.origin.mgm.client.api;

public interface IndexServiceFactoryProvider {

	public String getProviderId();

	public IndexServiceFactory getInstance();

}
