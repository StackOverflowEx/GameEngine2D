package de.Luca.Security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
	
	//CODE ZUR VERSCHLÜSSELUNG MIT AES

	public static byte[] encrypt(String strClearText, String strKey) throws Exception {
		byte[] encrypted = null;;
		byte[] decodedKey = Base64.getDecoder().decode(strKey);
		try {
			SecretKeySpec skeyspec = new SecretKeySpec(decodedKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			encrypted = cipher.doFinal(strClearText.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return encrypted;
	}

	public static String decrypt(byte[] encrypted, String strKey) throws Exception {
		String strData = "";
		byte[] decodedKey = Base64.getDecoder().decode(strKey);
		try {
			SecretKeySpec skeyspec = new SecretKeySpec(decodedKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(encrypted);
			strData = new String(decrypted);

		} catch (Exception e) {
			throw new Exception(e);
		}
		return strData;
	}
	
	public static String genKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
		SecretKey key = keyGen.generateKey();
		String keyStr = Base64.getEncoder().encodeToString(key.getEncoded());
		return new String(keyStr);
	}

}
