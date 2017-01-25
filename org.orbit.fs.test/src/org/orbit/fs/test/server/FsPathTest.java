package org.orbit.fs.test.server;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.orbit.fs.api.FilePath;

public class FsPathTest {

	public FsPathTest() {
	}

	public void setUp() {
	}

	@Test
	public void test001() {
		System.out.println("--- --- --- test001 --- --- ---");

		String path0a = "";
		String path0b = "/";
		String path1 = "/dir1";
		String path2 = "dir1";
		String path3 = "/readme1.txt";
		String path4 = "readme1.txt";
		String path5 = "/path/to/the/dir2";
		String path6 = "path/to/the/dir2";
		String path7 = "/path/to/the/file.rar";
		String path8 = "path/to/the/file.rar";

		String[] segments0a = path0a.split(FilePath.SEPARATOR);
		String[] segments0b = path0b.split(FilePath.SEPARATOR);
		String[] segments1 = path1.split(FilePath.SEPARATOR);
		String[] segments2 = path2.split(FilePath.SEPARATOR);
		String[] segments3 = path3.split(FilePath.SEPARATOR);
		String[] segments4 = path4.split(FilePath.SEPARATOR);
		String[] segments5 = path5.split(FilePath.SEPARATOR);
		String[] segments6 = path6.split(FilePath.SEPARATOR);
		String[] segments7 = path7.split(FilePath.SEPARATOR);
		String[] segments8 = path8.split(FilePath.SEPARATOR);

		System.out.println(Arrays.toString(segments0a) + " (length=" + segments0a.length + ")");
		System.out.println(Arrays.toString(segments0b) + " (length=" + segments0b.length + ")");
		System.out.println(Arrays.toString(segments1) + " (length=" + segments1.length + ")");
		System.out.println(Arrays.toString(segments2) + " (length=" + segments2.length + ")");
		System.out.println(Arrays.toString(segments3) + " (length=" + segments3.length + ")");
		System.out.println(Arrays.toString(segments4) + " (length=" + segments4.length + ")");
		System.out.println(Arrays.toString(segments5) + " (length=" + segments5.length + ")");
		System.out.println(Arrays.toString(segments6) + " (length=" + segments6.length + ")");
		System.out.println(Arrays.toString(segments7) + " (length=" + segments7.length + ")");
		System.out.println(Arrays.toString(segments8) + " (length=" + segments8.length + ")");

		System.out.println();
	}

	@Test
	public void test002() {
		System.out.println("--- --- --- test002 --- --- ---");

		FilePath path0a = new FilePath("");
		FilePath path0b = new FilePath("/");
		FilePath path0c = new FilePath("///");
		FilePath path1 = new FilePath("/dir1");
		FilePath path2 = new FilePath("dir1");
		FilePath path3 = new FilePath("/readme1.txt");
		FilePath path4 = new FilePath("readme1.txt");
		FilePath path5 = new FilePath("/path/to/the/dir2");
		FilePath path6 = new FilePath("path/to/the/dir2");
		FilePath path7 = new FilePath("/path/to/the/file.rar");
		FilePath path8 = new FilePath("path/to/the/file.rar");

		String[] segments0a = path0a.getSegments();
		String[] segments0b = path0b.getSegments();
		String[] segments0c = path0c.getSegments();
		String[] segments1 = path1.getSegments();
		String[] segments2 = path2.getSegments();
		String[] segments3 = path3.getSegments();
		String[] segments4 = path4.getSegments();
		String[] segments5 = path5.getSegments();
		String[] segments6 = path6.getSegments();
		String[] segments7 = path7.getSegments();
		String[] segments8 = path8.getSegments();

		System.out.println(Arrays.toString(segments0a) + " (length=" + segments0a.length + ")");
		System.out.println(Arrays.toString(segments0b) + " (length=" + segments0b.length + ")");
		System.out.println(Arrays.toString(segments0c) + " (length=" + segments0c.length + ")");
		System.out.println(Arrays.toString(segments1) + " (length=" + segments1.length + ")");
		System.out.println(Arrays.toString(segments2) + " (length=" + segments2.length + ")");
		System.out.println(Arrays.toString(segments3) + " (length=" + segments3.length + ")");
		System.out.println(Arrays.toString(segments4) + " (length=" + segments4.length + ")");
		System.out.println(Arrays.toString(segments5) + " (length=" + segments5.length + ")");
		System.out.println(Arrays.toString(segments6) + " (length=" + segments6.length + ")");
		System.out.println(Arrays.toString(segments7) + " (length=" + segments7.length + ")");
		System.out.println(Arrays.toString(segments8) + " (length=" + segments8.length + ")");

		String subPath7 = path7.getPathString(0, 3);
		String subPath8 = path8.getPathString(0, 3);
		System.out.println("subPath7 = " + subPath7);
		System.out.println("subPath8 = " + subPath8);

		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(FsPathTest.class);
		System.out.println("--- --- --- FsPathTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
