package Control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

public class ServidorTCP implements Runnable{
	
	/**
	* Realiza el proceso de intercambio de un archivo que se va a enviar a un cliente usando el protocolo TCP <br>
	* <b>pre: </b> El archivo ha sido cargado, pero falta cifrarlo.
	* <b>post: </b> Se ha enviado el archivo, Se ha comprobado su correcta llegada mediante el hash MD5.
	* @param archivoSinCifrar El archivo que fue seleccionado almacenado en un arreglo de bytes
	*/
	public void IntercambiarArchivos(byte[] archivoSinCifrar) {
		try {
			String entry;
			ServerSocket socketServidor = new ServerSocket();
			Socket client = socketServidor.accept();
			
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
	        	        
	        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        BufferedWriter OutFromServer = new BufferedWriter( new OutputStreamWriter(client.getOutputStream() ) );
	        
	        OutputStream in = client.getOutputStream();
	        DataOutputStream dataOut = new DataOutputStream(in);
	        
	        OutFromServer.write("GENERAR DH");
	        entry = inFromClient.readLine();
	        if(entry.equals("LISTO PARA GENERAR DH")) {
	        	
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
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
		
	}

}
