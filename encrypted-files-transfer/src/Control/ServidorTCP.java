package Control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
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

public class ServidorTCP extends Thread{
	private byte[] archivoSinCifrar;
	private String nombreArchivo;
	
	public ServidorTCP(byte[] arch, String nomArch) {
		archivoSinCifrar = arch;
		nombreArchivo = nomArch;
	}
	
	/**
	* Realiza el proceso de intercambio de un archivo que se va a enviar a un cliente usando el protocolo TCP <br>
	* <b>pre: </b> El archivo ha sido cargado, pero falta cifrarlo.
	* <b>post: </b> Se ha enviado el archivo, Se ha comprobado su correcta llegada mediante el hash MD5.
	* @param archivoSinCifrar El archivo que fue seleccionado almacenado en un arreglo de bytes
	*/
	public void IntercambiarArchivos() {
		try {
			int bitLength = 1024;
		    BigInteger p;
		    BigInteger g;
		    String MD5 = "";
		    EncryptFile encrypt = new EncryptFile();
		    //VARIABLE PARA MANEJAR MENSAJES DEL CLIENTE
			String entry;
			//SOCKET QUE DEJA AL SERVIDOR ESPERANDO
			ServerSocket socketServidor = new ServerSocket(15210);
			System.out.println("Ya inicializo el SocketServidor");
			//ESPERA QUE SE CONECTE EL CLIENTE
			Socket client = socketServidor.accept();	
			System.out.println("Ya se conecto el usuario para la descarga");
			
			//CANALES PARA INTERCAMBIAR INFORMACION
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
	        	        
	        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        BufferedWriter OutFromServer = new BufferedWriter( new OutputStreamWriter(client.getOutputStream() ) );
	        
	        OutputStream out = client.getOutputStream();
	        DataOutputStream dataOut = new DataOutputStream(out);
	        
	        //GENERACION DE CLAVE COMPARTIDA POR MEDIO DEL ALGORITMO DIFFIE HELLMAN
	        OutFromServer.write("GENERAR DH\n");
	        OutFromServer.flush();
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " + entry);
	        //g = random(k);
	        SecureRandom rnd = new SecureRandom();
	        g= BigInteger.probablePrime(bitLength, rnd);
	        if(entry.equals("LISTO PARA GENERAR DH")) {
	        	OutFromServer.write(g.toString()+"\n");
	        	System.out.println("Parametro G generado y enviado" + "g = " + g + "");
	        	OutFromServer.flush();
	        }
	        entry = inFromClient.readLine();
	        System.out.println(entry);
	        p = new BigInteger(entry);
	        System.out.println("Parametro P: " + p + "" );
	        DHParameterSpec dhParams = new  DHParameterSpec(g, p); //CREA LOS PARAMETROS PARA LA CREACION DE LA CLAVE
	        System.out.println("parametros diffie Hellman generados");
	        KeyPairGenerator serverKeyGen = KeyPairGenerator.getInstance("DH");//DECLARAR EL GENERADOR DE CLAVES EN MODO DH - Diffie Hellman
	        serverKeyGen.initialize(dhParams, new SecureRandom()); //
	        KeyAgreement serverKeyAgree = KeyAgreement.getInstance("DH");
	        KeyPair serverPair = serverKeyGen.generateKeyPair();
	        Key clientePublicKey = (Key) ois.readObject(); //RECIBE CLAVE PUBLICA DEL CLIENTE
	        System.out.println("Recibi clave publica cliente: " + clientePublicKey.toString());
	        oos.writeObject(serverPair.getPublic());oos.flush(); //ENVIA CLAVE PUBLICA DEL SERVIDOR
	        System.out.println("Envie clave publia (servidor): " + serverPair.getPublic().toString());
	        serverKeyAgree.init(serverPair.getPrivate());
	        serverKeyAgree.doPhase(clientePublicKey, true);
	        byte[] serverSharedSecret = serverKeyAgree.generateSecret();
	        SecretKeySpec claveServer = new SecretKeySpec(serverSharedSecret, 0, 16, "AES");
	        System.out.println("Clave secreta: "+ claveServer.toString());
	        
	        //ESPERA INICIO DE INTERACCIÓN
	        OutFromServer.write("INICIO\n");
	        OutFromServer.flush();
	        System.out.println("Enviando INICIO para iniciar Transferencia");
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " +  entry);
	        if(entry.equalsIgnoreCase("LISTO PARA INICIO")) {
	        	//CALCULA HASH MD5 ANTES DE CIFRAR EL ARCHIVO
	        	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        	messageDigest.reset();
	        	messageDigest.update(archivoSinCifrar);
	        	byte[] resultByte = messageDigest.digest();
	        	MD5 = bytesToHex(resultByte);
	        	System.out.println("MD5: " + MD5);
	        }
	        //CIFRA EL ARCHIVO Y LO ENVIA AL CLIENTE
	        System.out.println(archivoSinCifrar.length);
	        byte[] archivoCifrado = encrypt.cifrarArchivo(archivoSinCifrar, claveServer);
	        OutFromServer.write(""+ 0 + "," + archivoCifrado.length+"\n");
	        OutFromServer.flush();
	        System.out.println("Enviando info de control: " + ""+ 0 + "," + archivoCifrado.length);
	        entry = inFromClient.readLine();
	        System.out.println("From Client:" + entry);
	        if(entry.equalsIgnoreCase("OK")) {
	        	dataOut.write(archivoCifrado);
	        	dataOut.flush();
	        	System.out.println("archivo enviado");
	        }
	        
	        //ESPERTA CONFIRMACION DEL CLIENTE Y ENVIA EL HASH MD5 CALCULADO PREVIAMENTE
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " + entry);
	        byte[] params = encrypt.getParams(); 
	        OutFromServer.write(""+ 0 + "," + params.length+"\n");
	        OutFromServer.flush();
	        System.out.println("Out To Client: " + ""+ 0 + "," + params.length);
	        dataOut.write(params);
        	dataOut.flush();
	        if(entry.equalsIgnoreCase("RECIBIDO")) {
	        	OutFromServer.write(MD5+"\n");
	        	OutFromServer.flush();
	        }
	        
	        OutFromServer.write(nombreArchivo+"\n");
        	OutFromServer.flush();
	        //ESPERA ESTADO DE LA TRANSFERENCIA
	        entry = inFromClient.readLine();
	        System.out.println("From Client: " + entry);
	        if(entry.equalsIgnoreCase("TRANSFERENCIA CORRECTA")) {
	        	
	        }else {
	        	
	        } 
	        System.out.println("Se cierra el socket");
	        socketServidor.close();
		} catch (Exception e) {
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
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("entro a run de servidor TCP");
		IntercambiarArchivos();
	}

}
