package com.osgi.example1.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * http://stackoverflow.com/questions/27962116/simplest-way-to-encrypt-a-text-file-in-java
 * 
 */
public class HelloWorld {

	// private static final String ENCRYPTION_SCHEME = "DES";
	// private static final String ENCRYPTION_SCHEME = "DESede/CBC/PKCS5Padding";
	private static final String ENCRYPTION_SCHEME = "DES";
	private static final String UNICODE_FORMAT = "UTF-8";
	private static final String STRING_KEY = "c2a126aa-4ccb-4325-86b2-f232db88258e";

	public static SecretKey getSecretKey() throws Exception {
		SecretKey key = null;
		if (key == null) {
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_SCHEME);
			DESKeySpec keySpec = new DESKeySpec(STRING_KEY.getBytes());
			key = keyFactory.generateSecret(keySpec);
		}
		String secretKeyStr = new String(key.getEncoded());
		System.out.println("stringKey -> " + STRING_KEY);
		System.out.println("secretKey -> " + secretKeyStr);
		return key;
	}

	public static Cipher getEncryptCipher() throws Exception {
		Cipher cipher = Cipher.getInstance(ENCRYPTION_SCHEME);
		cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
		return cipher;
	}

	public static Cipher getDecryptCipher() throws Exception {
		Cipher cipher = Cipher.getInstance(ENCRYPTION_SCHEME);
		cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
		return cipher;
	}

	public static byte[] encrypt(byte[] bytes) throws Exception {
		return getEncryptCipher().doFinal(bytes);
	}

	public static byte[] decrypt(byte[] bytes) throws Exception {
		return getDecryptCipher().doFinal(bytes);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String uuid = UUID.randomUUID().toString();
			// System.out.println("uuid -> " + uuid);

			// test String
			byte[] testBytes = "No body can see me. 昨夜のコンサートは最高でした。形声字 / 形聲字 구결/口訣 이두/吏讀".getBytes(UNICODE_FORMAT);
			String testStr = new String(testBytes);
			System.out.println("test String -> " + testStr);

			// SecretKey and Cipher
			// SecretKey secretKey = getSecretKey();
			// String secretKeyStr = new String(secretKey.getEncoded());
			// System.out.println("secretKey -> " + secretKeyStr);

			// ENCRYPT
			byte[] encryptedBytes = encrypt(testBytes);
			String encryptedStr = new String(encryptedBytes);
			System.out.println("encrypted String -> " + encryptedStr);

			// DECRYPT
			byte[] decryptedBytes = decrypt(encryptedBytes);
			String decryptedStr = new String(decryptedBytes);
			System.out.println("decrypted String -> " + decryptedStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}