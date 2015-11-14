package com.osgi.example1;

import java.util.Comparator;

import org.osgi.service.component.ComponentContext;

/**
 * @see http://felix.apache.org/documentation/subprojects/apache-felix-service-component-runtime.html#example
 */
public class SampleComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		return o1.equals(o2) ? 0 : -1;
	}

	protected void activate(ComponentContext context) {
		System.out.println("SampleComparator.activate()");
	}

	protected void deactivate(ComponentContext context) {
		System.out.println("SampleComparator.deactivate()");
	}

}