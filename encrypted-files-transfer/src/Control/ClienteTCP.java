package Control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ClienteTCP extends Thread{
	
	private InetAddress IPServer;
	
	public ClienteTCP(InetAddress IPserver) {
		IPServer = IPserver;
	}
	
	/**
	* Realiza el proceso de intercambio de un archivo que esta recibiendo desde un servidor por medio del protocolo TCP. <br>
	* <b>post: </b> Se ha recibido el archivo, Se ha comprobado su correcta llegada mediante el hash MD5.
	*/
	public void IntercambiarArchivos() {
		try {
	        int k=6;
		    BigInteger p;
		    BigInteger g;
		    DecryptFile descifrar = new DecryptFile();
		    
			String parametro;
			byte[] lectura = null;
			byte[] params = null;
			byte[] archivoDescifrado = null;
			boolean correcto = false;
			
			//CONEXION
			System.out.println("Abriendo puerto para recibir");
	        Socket socketCliente = new Socket(IPServer, 15210);
	        
	        ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
	        	        
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        BufferedWriter OutFromClient = new BufferedWriter( new OutputStreamWriter(socketCliente.getOutputStream()));
	        
	        InputStream in = socketCliente.getInputStream();
	        DataInputStream dataIn = new DataInputStream(in);
	       
	        //GENERACION DE CLAVE COMPARTIDA POR MEDIO DEL ALGORITMO DIFFIE HELLMAN
	        parametro = inFromServer.readLine();
	        System.out.println("Me llego clave compartida: "+parametro);
	        if(parametro.equalsIgnoreCase("GENERAR DH")) {
	        	OutFromClient.write("LISTO PARA GENERAR DH\n");
	        	OutFromClient.flush();
	        }	
	        parametro = inFromServer.readLine();//RECIBE EL PARAMETRO G
	        System.out.println("Me llega parametro G: "+parametro);
	        g = new BigInteger(parametro);
	        //p = random(k);
	        SecureRandom secureP = new SecureRandom();
	        p = BigInteger.probablePrime(1024, secureP);
	        OutFromClient.write(p.toString()+"\n"); //ENVIA EL PARAMETRO P
	        OutFromClient.flush();
	        DHParameterSpec dhParams = new  DHParameterSpec(g, p); //CREA LOS PARAMETROS PARA LA CREACION DE LA CLAVE
	        KeyPairGenerator clienteKeyGen = KeyPairGenerator.getInstance("DH");
	        clienteKeyGen.initialize(dhParams, new SecureRandom());
	        KeyAgreement clienteKeyAgree = KeyAgreement.getInstance("DH");
	        KeyPair clientePair = clienteKeyGen.generateKeyPair();
	        oos.writeObject(clientePair.getPublic()); //ENVIA CLAVE PUBLICA DEL CLIENTE
	        Key serverPublicKey = (Key) ois.readObject(); //RECIBE CLAVE PUBLICA DEL SERVIDOR
	        System.out.println(serverPublicKey.toString());
	        clienteKeyAgree.init(clientePair.getPrivate());
	        clienteKeyAgree.doPhase(serverPublicKey, true); 
	        byte[] clienteSharedSecret =clienteKeyAgree.generateSecret();
	      

	        
	        SecretKeySpec claveCliente = new SecretKeySpec(clienteSharedSecret,0,16, "AES");
	        
	        
	        //ESPERA INICIO DE INTERACCIÓN
	        parametro = inFromServer.readLine();
	        if(parametro.equalsIgnoreCase("INICIO")) {
	        	OutFromClient.write("LISTO PARA INICIO\n");
	        	OutFromClient.flush();
	        	System.out.println("Escribi LISTO PARA INICIO");
	        }
	        
	        //ESPERA PARAMETRO DE LECTURA: inicio y TAM
	        parametro = inFromServer.readLine();
	        System.out.println("Recibi: "+parametro);
	        int inicio = Integer.parseInt(parametro.split(",")[0]);
	        int tam = Integer.parseInt(parametro.split(",")[1]);
	        if(tam > 0) {
		        OutFromClient.write("OK\n");
		        OutFromClient.flush();
		        lectura = new byte[tam];
		        dataIn.readFully(lectura, inicio, tam);
		        OutFromClient.write("RECIBIDO\n");
		        OutFromClient.flush();
		        System.out.println("Escribi OK y RECIBIDO");
	        }
	        
	        parametro = inFromServer.readLine();	        
	        inicio = Integer.parseInt(parametro.split(",")[0]);
	        tam = Integer.parseInt(parametro.split(",")[1]);
	       if(tam > 0) {
	    	   params = new byte[tam];
	    	   dataIn.readFully(params, inicio, tam);
	    	   System.out.println("Recibi "+parametro+" "+tam);
	       }
	        
	        //ESPERA EL HASH MD5 DEL SERVIDOR Y CALCULA EL HASH MD5 DEL ARCHIVO RECIBIDO DESCIFRADO
	        if(lectura != null) {
	        	parametro = inFromServer.readLine(); //MD5 del servidor
	        	System.out.println("Recibi: "+parametro);
	        	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        	messageDigest.reset();	        	
	        	archivoDescifrado = descifrar.descifrarArchivo(lectura, claveCliente, params);
	        	messageDigest.update(archivoDescifrado);
	        	byte[] resultByte = messageDigest.digest();
	        	String MD5 = bytesToHex(resultByte);
	        	if(parametro.equalsIgnoreCase(MD5))
	        		correcto = true;
	        }
	        
	        //RESPONDE AL SERVIDOR EL ESTADO DE LA TRANSFERENCIA
	        if(correcto) {
	        	parametro = inFromServer.readLine();
	        	OutFromClient.write("TRANSFERENCIA CORRECTA\n");
	        	descifrar.escribirArchivo(archivoDescifrado, parametro);
	        	System.out.println("Mande transferencia correcta");
	        }else {
	        	OutFromClient.write("TRANSFERENCIA INCORRECTA\n");
	        	System.out.println("Mande transferencia incorrecta");
	        }
	        OutFromClient.flush();
	        
	        
	        OutFromClient.close();
	        socketCliente.close();
	        System.out.println("Conexi�n cerrada");
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
	
	/**
	* Crea un numero de formato BigInteger aleatorio. <br>
	* @param k numero entero mayor que cero
	*/
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
		IntercambiarArchivos();
	}
}
