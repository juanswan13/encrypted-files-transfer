package Control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EncryptFile {
	
	public void cifrarArchivo(File archivo) {
	
		// Create data to encrypt 
		Map map = new TreeMap(System.getProperties()); 
		int number = map.size(); 
	
		try {	
		// Create Key 
		KeyGenerator kg = KeyGenerator.getInstance("AES"); 
		SecretKey secretKey = kg.generateKey(); 
	
		// Create Cipher 
		Cipher aesCipher = 
		Cipher.getInstance("AES/CBC/PKCS57Padding"); 
		aesCipher.init(Cipher.ENCRYPT_MODE, secretKey); 
	
		// Create stream 
		FileOutputStream fos = new FileOutputStream(archivo); 
		BufferedOutputStream bos = new BufferedOutputStream(fos); 
		CipherOutputStream cos = new CipherOutputStream(bos, aesCipher); 
		ObjectOutputStream oos = new ObjectOutputStream(cos); 
	
		// Write objects 
		oos.writeObject(map); 
		oos.writeInt(number); 
		oos.flush(); 
		oos.close(); 

		}catch (NoSuchPaddingException e) { 
			System.err.println("Padding problem: " + e); 
		} catch (NoSuchAlgorithmException e) { 
		System.err.println("Invalid algorithm: " + e); 
		} catch (InvalidKeyException e) { 
		System.err.println("Invalid key: " + e); 
		} catch (IOException e) { 
		System.err.println("I/O Problem: " + e); 
		} 
	}
}
