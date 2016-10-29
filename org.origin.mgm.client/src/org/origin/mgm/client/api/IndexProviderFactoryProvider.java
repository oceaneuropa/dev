package org.origin.mgm.client.api;

public interface IndexProviderFactoryProvider {

	public String getProviderId();

	public IndexProviderFactory getInstance();

}
