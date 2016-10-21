package org.origin.common.loadbalance;

import java.util.Properties;

public interface LoadBalanceableResource {

	/**
	 * 
	 * @param id
	 */
	public void setLoadBalanceId(String id);

	/**
	 * 
	 * @return
	 */
	public String getLoadBalanceId();

	/**
	 * 
	 * @param properties
	 */
	public void setLoadBalanceProperties(Properties properties);

	/**
	 * 
	 * @return
	 */
	public Properties getLoadBalanceProperties();

}
