package org.orbit.component.cli;

import java.util.List;

import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.rest.client.ClientException;

import other.orbit.component.api.tier1.account.UserRegistryConnectorV1;
import other.orbit.component.api.tier2.appstore.AppStoreConnectorV1;
import other.orbit.component.api.tier3.domainmanagement.DomainServiceConnectorV1;

public class ServicesCommandHelper {

	public static ServicesCommandHelper INSTANCE = new ServicesCommandHelper();

	// /**
	// *
	// * @param connector
	// * @return
	// * @throws ClientException
	// */
	// public Auth getAuth(AuthConnector connector) throws ClientException {
	// Auth auth = connector.getService();
	// if (auth == null) {
	// System.err.println(getClass().getSimpleName() + ".getAuth() auth is not available.");
	// throw new ClientException(500, "auth is not available.");
	// }
	// System.out.println(auth.getName() + " (" + auth.getURL() + ")");
	// return auth;
	// }

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public AppStoreClient getAppStore(AppStoreConnectorV1 connector) throws ClientException {
		AppStoreClient appStore = connector.getService();
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
	public DomainManagementClient getDomainService(DomainServiceConnectorV1 connector) throws ClientException {
		DomainManagementClient domainService = connector.getService();
		if (domainService == null) {
			System.err.println(getClass().getSimpleName() + ".getDomainService() domainService is not available.");
			throw new ClientException(500, "domainService is not available.");
		}
		// System.out.println(domainService.getName() + " (" + domainService.getURL() + ")");
		System.out.println("(" + domainService.getURL() + ")");
		return domainService;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public List<LoadBalanceResource<UserAccountClient>> getUserRegistryResources(UserRegistryConnectorV1 connector) throws ClientException {
		if (connector == null) {
			System.out.println("UserRegistryConnector is not available.");
			throw new ClientException(500, "UserRegistryConnector is not available.");
		}
		LoadBalancer<UserAccountClient> balancer = connector.getLoadBalancer();
		if (balancer == null) {
			System.out.println("load balancer is not available.");
			return null;
		}
		List<LoadBalanceResource<UserAccountClient>> resources = balancer.getResources();
		if (resources == null) {
			System.out.println("load balancer's resource is null.");
			return null;
		}
		return resources;
	}

	// /**
	// *
	// * @param connector
	// * @return
	// * @throws ClientException
	// */
	// public List<LoadBalanceResource<Auth>> getAuthResources(AuthConnector connector) throws ClientException {
	// if (connector == null) {
	// System.out.println("AuthConnector is not available.");
	// throw new ClientException(500, "AuthConnector is not available.");
	// }
	// LoadBalancer<Auth> balancer = connector.getLoadBalancer();
	// if (balancer == null) {
	// System.out.println("load balancer is not available.");
	// return null;
	// }
	// List<LoadBalanceResource<Auth>> resources = balancer.getResources();
	// if (resources == null) {
	// System.out.println("load balancer's resource is null.");
	// return null;
	// }
	// return resources;
	// }

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws ClientException
	 */
	public List<LoadBalanceResource<AppStoreClient>> getAppStoreResources(AppStoreConnectorV1 connector) throws ClientException {
		if (connector == null) {
			System.out.println("AppStoreConnector is not available.");
			throw new ClientException(500, "AppStoreConnector is not available.");
		}
		LoadBalancer<AppStoreClient> balancer = connector.getLoadBalancer();
		if (balancer == null) {
			System.out.println("load balancer is not available.");
			return null;
		}
		List<LoadBalanceResource<AppStoreClient>> resources = balancer.getResources();
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
	public List<LoadBalanceResource<DomainManagementClient>> getDomainServiceResources(DomainServiceConnectorV1 connector) throws ClientException {
		if (connector == null) {
			System.out.println("DomainServiceConnector is not available.");
			throw new ClientException(500, "DomainServiceConnector is not available.");
		}
		LoadBalancer<DomainManagementClient> balancer = connector.getLoadBalancer();
		if (balancer == null) {
			System.out.println("load balancer is not available.");
			return null;
		}
		List<LoadBalanceResource<DomainManagementClient>> resources = balancer.getResources();
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
