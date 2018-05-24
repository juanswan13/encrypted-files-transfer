package Control;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class EncryptFile {
	
	public EncryptFile(){
		
	}
	
	public byte[] leerArchivo(String ruta) {
		 byte[] archivoEnBytes = null;
		    FileInputStream theFIS = null;
		    BufferedInputStream theBIS = null;
		    byte[] buffer = new byte[8 * 1024];
		    int leido = 0;
		    ByteArrayOutputStream theBOS = new ByteArrayOutputStream();

		    try{
		      theFIS = new FileInputStream(ruta);
		      theBIS = new BufferedInputStream(theFIS);
		      while ((leido = theBIS.read(buffer)) >= 0){
		        theBOS.write(buffer, 0, leido);
		      }
		      // Fichero leido del todo, pasamos el contenido
		      // del BOS al byte[]
		      archivoEnBytes = theBOS.toByteArray();
		      // Liberamos y cerramos para ser eficientes
		      theBOS.reset();
		      // Este close no va dentro de un try/catch por que
		      // BOS es un Stream especial y close no hace nada
		      theBOS.close();
		    }
		    catch (IOException e1) {
		      // Error leyendo el fichero así que no tenemos
		      // en memoria el fichero. 
		      e1.printStackTrace();
		    }
		    finally {
		      if (theBIS != null) {
		        try {
		          theBIS.close();
		        }
		        catch (IOException e) {
		          // Error cerrando stream del fichero
		          e.printStackTrace();
		        }
		      }
		    }
		    return archivoEnBytes;
	}
	
	public byte[] cifrarArchivo(byte[] archivo, Key secretKey) {
		
		byte[] encryptedFile = null;
		try {	
		// Create Cipher 
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
		aesCipher.init(Cipher.ENCRYPT_MODE, secretKey); 
		encryptedFile = aesCipher.doFinal(archivo);
		}catch (NoSuchPaddingException e) { 
			System.err.println("Padding problem: " + e); 
		} catch (NoSuchAlgorithmException e) { 
		System.err.println("Invalid algorithm: " + e); 
		} catch (InvalidKeyException e) { 
		System.err.println("Invalid key: " + e); 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		return encryptedFile;
	}
}
