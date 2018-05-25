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
// METODO ADAPTADO, OBTENIDO DE: http://www.forosdelweb.com/f45/escribir-fichero-array-bytes-recuperarlos-494509/
	public void escribirArchivo(byte[] data, String nombreArchivo) {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File("./Transferencias/" + nombreArchivo);
			
			int cont = 1;
			while	(file.exists()) {
				file = new File("./Transferencias/" + nombreArchivo.replace('.', '/').split("/")[0] + cont + "." + nombreArchivo.replace('.', '/').split("/")[1] );
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
