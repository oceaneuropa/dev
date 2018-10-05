package com.osgi.example1.io;

import java.io.File;

public class FileTest {

	public static void main(String[] args) {
		File file1 = new File("/Users/oceaneuropa/path/to/Abc.txt");
		File file2 = new File("/Users/oceaneuropa/path/to/abc.txt");

		boolean exists1 = file1.exists();
		boolean exists2 = file2.exists();

		System.out.println(file1.getName() + " exist: " + exists1);
		System.out.println(file2.getName() + " exist: " + exists2);
	}

}
