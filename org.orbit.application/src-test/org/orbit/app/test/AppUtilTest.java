package org.orbit.app.test;

import java.io.File;
import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.orbit.app.AppManifest;
import org.orbit.app.util.AppUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppUtilTest {

	protected File appsFolder;

	public AppUtilTest() {
		setUp();
	}

	protected void setUp() {
		this.appsFolder = new File("/Users/oceaneuropa/Downloads/zip");
	}

	@Test
	public void test001_read_manifest_from_app() throws IOException {
		System.out.println(getClass().getSimpleName() + ".test001_read_manifest_from_app()");

		File appFile = new File("/Users/oceaneuropa/Downloads/zip/editor_1.0.0.app");
		AppManifest manifest = AppUtil.extractAppManifest(appFile);
		System.out.println("manifest = " + manifest);

		System.out.println();
	}

	@Test
	public void test002_unzip_app() throws IOException {
		System.out.println(getClass().getSimpleName() + ".test002_unzip_app()");

		File appFile = new File("/Users/oceaneuropa/Downloads/zip/editor_1.0.0.app");
		AppUtil.extractToAppsFolder(this.appsFolder, appFile);

		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(AppUtilTest.class);

		System.out.println("--- --- --- AppUtilTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
