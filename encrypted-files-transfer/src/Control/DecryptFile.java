package Control;

import java.io.File;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.Key;

import javax.crypto.Cipher;
import javax.xml.crypto.AlgorithmMethod;

public class DecryptFile {
	
	public DecryptFile() {
		
	}
	
	public void escribirArchivo(byte[] data, String nombreArchivo) {
		FileOutputStream fop = null;
		File file;
		try {
			//file = new File("C:/Users/rubendcm/Documents/Tranferencia");
			file = new File("C:/Users/Juan K/Documents/Universidad/" + nombreArchivo);
			// if file doesnt exists, then create it
			int cont = 1;
			while	(file.exists()) {
				//file = new File("C:/Users/rubendcm/Documents/Tranferencia" + cont);
				file = new File("C:/Users/Juan K/Documents/Universidad/Tranferencia" + cont);
				cont++;
			}
			fop = new FileOutputStream(file);
			file.createNewFile();
			fop.write(data);
			fop.flush();
			fop.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public byte[] descifrarArchivo(byte[] encryptedFile, Key secretKey, byte[] encodedParams) {
		byte[] decryptedFile = null; 
		try {
		     AlgorithmParameters aesParams = AlgorithmParameters.getInstance("AES");
		     aesParams.init(encodedParams);
			 Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
			 aesCipher.init(Cipher.DECRYPT_MODE, secretKey, aesParams);
	         decryptedFile = aesCipher.doFinal(encryptedFile);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	return decryptedFile;
	}
}
