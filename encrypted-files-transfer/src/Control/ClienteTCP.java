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
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ClienteTCP extends Thread{
	
	//IP del servidor remoto 
	private InetAddress IPServer;	
	
	public ClienteTCP(InetAddress IPserver) {
		//Se inicializa IP del servidor
		IPServer = IPserver;
	}
	
	/**
	* Realiza el proceso de intercambio de un archivo que esta recibiendo desde un servidor por medio del protocolo TCP. <br>
	* <b>post: </b> Se ha recibido el archivo, Se ha comprobado su correcta llegada mediante el hash MD5.
	*/
	public void IntercambiarArchivos() {
		try {
			//Se declaran las variables de las llaves 
		    BigInteger p;
		    BigInteger g;
		    
		    //Se declara la clase para desencriptar
		    DecryptFile descifrar = new DecryptFile();
		    
		    //Se declaran las variables de lectura, almacenamiento y control
			String parametro;
			byte[] lectura = null;
			byte[] params = null;
			byte[] archivoDescifrado = null;
			boolean correcto = false;
			
			//Se abre el puerto al servidor para transmitir
			System.out.println("Abriendo puerto para recibir");
	        Socket socketCliente = new Socket(IPServer, 15210);
	        
	        //Crea los flujos de objetos
	        ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(socketCliente.getInputStream());
	        
	        //Crea los flujos de escritura y lectura
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        BufferedWriter OutFromClient = new BufferedWriter( new OutputStreamWriter(socketCliente.getOutputStream()));
	        
	        //Crea el flujo de lectura de datos
	        InputStream in = socketCliente.getInputStream();
	        DataInputStream dataIn = new DataInputStream(in);
	       
	        //Lectura de la orden de inicio del servidor
	        parametro = inFromServer.readLine();
	        System.out.println("Mensaje del servidor: "+parametro);
	        if(parametro.equalsIgnoreCase("GENERAR DH")) {
	        	
	        	//Mensaje de confirmacion al servidor
	        	OutFromClient.write("LISTO PARA GENERAR DH\n");
	        	OutFromClient.flush();
	        }	
	        
	        //Lectura del parametro G del servidor
	        parametro = inFromServer.readLine();
	        System.out.println("Me llega parametro G: "+parametro);
	        g = new BigInteger(parametro);
	        
	        //Se genera el parametro P del cliente
	        SecureRandom secureP = new SecureRandom();
	        p = BigInteger.probablePrime(1024, secureP);
	        
	        //Transmision del parametro P
	        OutFromClient.write(p.toString()+"\n"); 
	        OutFromClient.flush();
	        
	        //Se usan los parametros G y P para generar la clave
	        DHParameterSpec dhParams = new  DHParameterSpec(g, p); 
	        KeyPairGenerator clienteKeyGen = KeyPairGenerator.getInstance("DH");
	        clienteKeyGen.initialize(dhParams, new SecureRandom());
	        KeyPair clientePair = clienteKeyGen.generateKeyPair();
	        
	        
	        //Se obtiene la clave publica y se transmite al servidor
	        oos.writeObject(clientePair.getPublic()); 
	        
	        //Se obtiene la clave publica del servidor
	        Key serverPublicKey = (Key) ois.readObject(); 
	        System.out.println(serverPublicKey.toString());
	        
	        //Se crea el keyagreemente para comprobar las llaves
	        KeyAgreement clienteKeyAgree = KeyAgreement.getInstance("DH");
	        //Clave privada del cliente
	        clienteKeyAgree.init(clientePair.getPrivate());
	        //Clave publica del servidor
	        clienteKeyAgree.doPhase(serverPublicKey, true);
	        
	        //Genera la clave secreta
	        byte[] clienteSharedSecret =clienteKeyAgree.generateSecret();	        
	        SecretKeySpec claveCliente = new SecretKeySpec(clienteSharedSecret,0,16, "AES");
	        
	        
	        //Lectura de inicio de transmision del servidor
	        parametro = inFromServer.readLine();
	        if(parametro.equalsIgnoreCase("INICIO")) {
	        	//Confirmacion inicio de transmision al servidor
	        	OutFromClient.write("LISTO PARA INICIO\n");
	        	OutFromClient.flush();
	        	System.out.println("Inicio transmision");
	        }
	        
	        //Espera los parametros para iniciar la lectura del archivo
	        parametro = inFromServer.readLine();
	        System.out.println("Recibido: "+parametro);
	        //Se inicializan los parametros de tamano y byte de inicio del archivo
	        int inicio = Integer.parseInt(parametro.split(",")[0]);
	        int tam = Integer.parseInt(parametro.split(",")[1]);
	        if(tam > 0) {
	        	//Mensaje de confirmacion de inicio de la transmision
		        OutFromClient.write("OK\n");
		        OutFromClient.flush();
		        
		        //Lectura del canal 
		        lectura = new byte[tam];
		        dataIn.readFully(lectura, inicio, tam);
		        
		        //Mensaje de confirmacion de recepcion de la transmision del archivo
		        OutFromClient.write("RECIBIDO\n");
		        OutFromClient.flush();
		        System.out.println("Transmision: RECIBIDO");
	        }
	        
	        //Lectura de inicio y tamano del parametro de desencriptacion
	        parametro = inFromServer.readLine();	        
	        inicio = Integer.parseInt(parametro.split(",")[0]);
	        tam = Integer.parseInt(parametro.split(",")[1]);
	       if(tam > 0) {
	    	   //Lectura del parametro de desencriptacion
	    	   params = new byte[tam];
	    	   dataIn.readFully(params, inicio, tam);
	    	   System.out.println("Recibi "+parametro);
	       }
	        
	        //Comprobacion archivo no vacio
	        if(lectura != null) {
	        	//Lectura del MD5 transmitido desde el servidor
	        	parametro = inFromServer.readLine(); 
	        	System.out.println("Recibi: "+parametro);
	        	
	        	//Calcula el hash del MD5 del servidor
	        	MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	        	messageDigest.reset();	        	
	        	
	        	//Descifra el archivo y lo pasa al messageDigest
	        	archivoDescifrado = descifrar.descifrarArchivo(lectura, claveCliente, params);
	        	messageDigest.update(archivoDescifrado);
	        	
	        	//Se calcula el MD5 en bytes
	        	byte[] resultByte = messageDigest.digest();
	        	
	        	//Se pasa de bytes a HEX
	        	String MD5 = bytesToHex(resultByte);
	        	//Se compara MD5 del archivo descifrado con el transmitido por el servidor
	        	if(parametro.equalsIgnoreCase(MD5))
	        		correcto = true;
	        }
	        
	        //Lectura nombre del archivo
	        parametro = inFromServer.readLine();
	        
	        //Mensaje de confirmacion de la transmision
	        if(correcto) {
	        	OutFromClient.write("TRANSFERENCIA CORRECTA\n");
	        	
	        	//Se guarda el archivo en la carpeta /Transferencia del proyecto
	        	descifrar.escribirArchivo(archivoDescifrado, parametro);
	        	System.out.println("Mande transferencia correcta");
	        }
	        //Mensaje de informe de error de la transmision
	        else {
	        	OutFromClient.write("TRANSFERENCIA INCORRECTA\n");
	        	System.out.println("Mande transferencia incorrecta");
	        }
	        //Se cierran los canales y el socket
	        OutFromClient.flush();
	        oos.close();
	        OutFromClient.close();
	        socketCliente.close();
	        System.out.println("Conexion cerrada");
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
