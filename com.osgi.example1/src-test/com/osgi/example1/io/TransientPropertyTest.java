package com.osgi.example1.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @see http://snowolf.iteye.com/blog/1329649
 */
public class TransientPropertyTest {

	/**
	 * 为了代码简洁，未将流操作至于try-catch中，仅为演示，勿仿！
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		People people = new People();
		people.setUsername("snowolf");
		people.setPassword("123456");

		System.err.println("------操作前------");
		System.err.println("username: " + people.getUsername());
		System.err.println("password: " + people.getPassword());

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("people.txt"));
		oos.writeObject(people);
		oos.flush();
		oos.close();

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("people.txt"));
		people = (People) ois.readObject();
		ois.close();

		System.err.println("------操作后------");
		System.err.println("username: " + people.getUsername());
		System.err.println("password: " + people.getPassword());
	}

}
