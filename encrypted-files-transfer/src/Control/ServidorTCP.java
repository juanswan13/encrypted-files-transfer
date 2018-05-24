package Control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP implements Runnable{
	
	/**
	* Realiza el proceso de intercambio de un archivo que se va a enviar a un cliente usando el protocolo TCP <br>
	* <b>pre: </b> El archivo ha sido cargado, pero falta cifrarlo.
	* <b>post: </b> Se ha enviado el archivo, Se ha comprobado su correcta llegada mediante el hash MD5.
	* @param archivoSinCifrar El archivo que fue seleccionado almacenado en un arreglo de bytes
	*/
	public void IntercambiarArchivos(byte[] archivoSinCifrar) {
		try {
			ServerSocket socketServidor = new ServerSocket();
			Socket client = socketServidor.accept();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
