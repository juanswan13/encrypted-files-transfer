package Control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;

public class ClienteTCP implements Runnable{
	
	
	/**
	* Realiza el proceso de intercambio de un archivo que esta recibiendo desde un servidor por medio del protocolo TCP. <br>
	* <b>post: </b> Se ha recibido el archivo, Se ha comprobado su correcta llegada mediante el hash MD5.
	*/
	public void IntercambiarArchivos() {
		try {
			int n=4;
	        int k=6;
		    BigInteger p;
		    BigInteger g;
		    
			String parametro;
			byte[] lectura = null;
			boolean correcto = false;
			//CONEXION
	        Socket socketCliente = new Socket("127.0.0.1", 6789);
	        
	        
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        BufferedWriter OutFromClient = new BufferedWriter( new OutputStreamWriter( socketCliente.getOutputStream() ) );
	        
	        InputStream in = socketCliente.getInputStream();
	        DataInputStream dataIn = new DataInputStream(in);
	        DataOutputStream dataOut = new DataOutputStream(socketCliente.getOutputStream());
	       
	        parametro = inFromServer.readLine();
	        if(parametro.equalsIgnoreCase("GENERAR DH")) {
	        	OutFromClient.write("LISTO PARA GENERAR DH");
	        }	
	        parametro = inFromServer.readLine(); //RECIBE EL PARAMETRO G
	        g = new BigInteger(parametro);
	        p = random(k);
	        OutFromClient.write(p.toString()); //ENVIA EL PARAMETRO P
	        DHParameterSpec dhParams = new  DHParameterSpec(g, p); //CREA LOS PARAMETROS PARA LA CREACION DE LA CLAVE
	        KeyPairGenerator clienteKeyGen = KeyPairGenerator.getInstance("DH", "BC");
	        clienteKeyGen.initialize(dhParams, new SecureRandom());
	        KeyAgreement clienteKeyAgree = KeyAgreement.getInstance("DH", "BC");
	        KeyPair clientePair = clienteKeyGen.generateKeyPair();
	        OutFromClient.write(p.toString());
	        
	        
	        //ESPERA INICIO DE INTERACCIÓN
	        parametro = inFromServer.readLine();
	        if(parametro.equalsIgnoreCase("INICIO")) {
	        	OutFromClient.write("LISTO PARA INICIO");
	        }
	        
	        //ESPERA PARAMETRO DE LECTURA: inicio y TAM
	        parametro = inFromServer.readLine();
	        int inicio = Integer.parseInt(parametro.split(",")[0]);
	        int tam = Integer.parseInt(parametro.split(",")[1]);
	        if(tam > 0) {
		        OutFromClient.write("OK");
		        lectura = new byte[tam];
		        dataIn.readFully(lectura, inicio, tam);
		        OutFromClient.write("RECIBIDO");
	        }
	        
	        //ESPERA EL HASH MD5 DEL SERVIDOR Y CALCULA EL HASH MD5 DEL ARCHIVO RECIBIDO
	        if(lectura != null) {
	        	parametro = inFromServer.readLine();
	        	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        	messageDigest.reset();
	        	messageDigest.update(lectura);
	        	byte[] resultByte = messageDigest.digest();
	        	String MD5 = bytesToHex(resultByte);
	        	if(parametro.equalsIgnoreCase(MD5))
	        		correcto = true;
	        }
	        
	        //RESPONDE AL SERVIDOR EL ESTADO DE LA TRANSFERENCIA
	        if(correcto) {
	        	OutFromClient.write("TRANSFERENCIA CORRECTA");
	        }else {
	        	OutFromClient.write("TRANSFERENCIA INCORRECTA");
	        }
	        socketCliente.close();
		}catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	public static String bytesToHex(byte[] data) {
	      if (data==null) {
	         return null;
	      } else {
	         int len = data.length;
	         String str = "";
	         for (int i=0; i<len; i++) {
	            if ((data[i]&0xFF)<16) str = str + "0" 
	               + java.lang.Integer.toHexString(data[i]&0xFF);
	            else str = str
	               + java.lang.Integer.toHexString(data[i]&0xFF);
	         }
	         return str.toUpperCase();
	      }
	   }     
	
    public BigInteger random(int k){
    	SecureRandom sr = new SecureRandom();
        byte[] ba = new byte[k];
        ba[0] = (byte) (sr.nextInt(9)+49);
        for (int d = 1;d<k;d++){
            ba[d] = (byte) (sr.nextInt(10)+48); 
        }
        return new BigInteger(new String(ba));
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub		
	}
}
