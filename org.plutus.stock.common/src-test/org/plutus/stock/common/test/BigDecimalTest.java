package org.plutus.stock.common.test;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.util.BigDecimalUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BigDecimalTest {

	public BigDecimalTest() {
		setUp();
	}

	protected void setUp() {
	}

	@Test
	public void test001() throws IOException {
		System.out.println("--- --- --- test001() --- --- ---");
		BigDecimal a = BigDecimalUtil.toBigDecimal(12.5);
		BigDecimal b = BigDecimalUtil.toBigDecimal(500);
		BigDecimal c = BigDecimalUtil.multiply(a, b);
		System.out.println("result = " + c.toPlainString());
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(BigDecimalTest.class);

		System.out.println("--- --- --- BigDecimalTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
