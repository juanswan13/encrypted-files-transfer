package Control;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.Cipher;

public class DecryptFile {
	
	public DecryptFile() {
		
	}
	
	public void escribirArchivo(byte[] data) {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File("C:/Users/Juan K/Documents/Universidad/Semestre 8/Seguridad/ProyectoFinal/Tranferencia");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			int cont = 1;
			while	(file.exists()) {
				file = new File("C:/Users/Juan K/Documents/Universidad/Semestre 8/Seguridad/ProyectoFinal/Tranferencia" + cont);
				cont++;
			}
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
	
	public byte[] descifrarArchivo(byte[] encryptedFile, Key secretKey) {
		byte[] decryptedFile = null; 
		try {
			 Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
			 aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
	         decryptedFile = aesCipher.doFinal(encryptedFile);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	return decryptedFile;
	}
}
