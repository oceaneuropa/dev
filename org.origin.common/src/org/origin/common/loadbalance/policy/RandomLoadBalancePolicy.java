package org.origin.common.loadbalance.policy;

import java.util.List;
import java.util.Random;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;

public class RandomLoadBalancePolicy<S> extends AbstractLoadBalancePolicy<S> {

	protected Random random;

	public RandomLoadBalancePolicy() {
		this.random = new Random();
	}

	@Override
	public LoadBalanceResource<S> next() {
		LoadBalancer<S> lb = checkLoadBalancer();
		List<LoadBalanceResource<S>> services = lb.getResources();
		if (services == null || services.isEmpty()) {
			return null;
		}
		if (services.size() == 1) {
			return services.get(0);
		}

		int randomIndex = this.random.nextInt(services.size());
		return services.get(randomIndex);
	}

	public static void main(String[] args) {
		Random random = new Random();
		for (int i = 0; i < 30; i++) {
			int randomInt = random.nextInt(10);
			System.out.println(randomInt);
		}
	}

}
