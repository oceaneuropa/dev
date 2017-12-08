package org.orbit.component.cli;

import java.util.List;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.AuthConnector;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.rest.client.ClientException;

public class ServicesCommandHelper {

	public static ServicesCommandHelper INSTANCE = new ServicesCommandHelper();

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public Auth getAuth(AuthConnector connector) throws ClientException {
		Auth auth = connector.getService();
		if (auth == null) {
			System.err.println(getClass().getSimpleName() + ".getAuth() auth is not available.");
			throw new ClientException(500, "auth is not available.");
		}
		System.out.println(auth.getName() + " (" + auth.getURL() + ")");
		return auth;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public AppStore getAppStore(AppStoreConnector connector) throws ClientException {
		AppStore appStore = connector.getService();
		if (appStore == null) {
			System.err.println(getClass().getSimpleName() + ".getAppStore() appStore is not available.");
			throw new ClientException(500, "appStore is not available.");
		}
		System.out.println(appStore.getName() + " (" + appStore.getURL() + ")");
		return appStore;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public DomainManagement getDomainManagement(DomainManagementConnector connector) throws ClientException {
		DomainManagement domainMgmt = connector.getService();
		if (domainMgmt == null) {
			System.err.println(getClass().getSimpleName() + ".getDomainManagement() domainMgmt is not available.");
			throw new ClientException(500, "domainMgmt is not available.");
		}
		System.out.println(domainMgmt.getName() + " (" + domainMgmt.getURL() + ")");
		return domainMgmt;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public List<LoadBalanceResource<UserRegistry>> getUserRegistryResources(UserRegistryConnector connector) throws ClientException {
		if (connector == null) {
			System.out.println("UserRegistryConnector is not available.");
			throw new ClientException(500, "UserRegistryConnector is not available.");
		}
		LoadBalancer<UserRegistry> balancer = connector.getLoadBalancer();
		if (balancer == null) {
			System.out.println("load balancer is not available.");
			return null;
		}
		List<LoadBalanceResource<UserRegistry>> resources = balancer.getResources();
		if (resources == null) {
			System.out.println("load balancer's resource is null.");
			return null;
		}
		return resources;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public List<LoadBalanceResource<Auth>> getAuthResources(AuthConnector connector) throws ClientException {
		if (connector == null) {
			System.out.println("AuthConnector is not available.");
			throw new ClientException(500, "AuthConnector is not available.");
		}
		LoadBalancer<Auth> balancer = connector.getLoadBalancer();
		if (balancer == null) {
			System.out.println("load balancer is not available.");
			return null;
		}
		List<LoadBalanceResource<Auth>> resources = balancer.getResources();
		if (resources == null) {
			System.out.println("load balancer's resource is null.");
			return null;
		}
		return resources;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public List<LoadBalanceResource<AppStore>> getAppStoreResources(AppStoreConnector connector) throws ClientException {
		if (connector == null) {
			System.out.println("AppStoreConnector is not available.");
			throw new ClientException(500, "AppStoreConnector is not available.");
		}
		LoadBalancer<AppStore> balancer = connector.getLoadBalancer();
		if (balancer == null) {
			System.out.println("load balancer is not available.");
			return null;
		}
		List<LoadBalanceResource<AppStore>> resources = balancer.getResources();
		if (resources == null) {
			System.out.println("load balancer's resource is null.");
			return null;
		}
		return resources;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public List<LoadBalanceResource<DomainManagement>> getDomainManagementResources(DomainManagementConnector connector) throws ClientException {
		if (connector == null) {
			System.out.println("DomainManagementConnector is not available.");
			throw new ClientException(500, "DomainManagementConnector is not available.");
		}
		LoadBalancer<DomainManagement> balancer = connector.getLoadBalancer();
		if (balancer == null) {
			System.out.println("load balancer is not available.");
			return null;
		}
		List<LoadBalanceResource<DomainManagement>> resources = balancer.getResources();
		if (resources == null) {
			System.out.println("load balancer's resource is null.");
			return null;
		}
		return resources;
	}

}

// /**
// *
// * @param connector
// * @return
// * @throws ClientException
// */
// public List<LoadBalanceResource<TransferAgent>> getTransferAgentResources(TransferAgentConnector connector) throws ClientException {
// if (connector == null) {
// System.out.println("TransferAgentConnector is not available.");
// throw new ClientException(500, "TransferAgentConnector is not available.");
// }
// LoadBalancer<TransferAgent> balancer = connector.getLoadBalancer();
// if (balancer == null) {
// System.out.println("load balancer is not available.");
// return null;
// }
// List<LoadBalanceResource<TransferAgent>> resources = balancer.getResources();
// if (resources == null) {
// System.out.println("load balancer's resource is null.");
// return null;
// }
// return resources;
// }
