package Control;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ConexionClient extends Thread {
	private String ip;
	private byte[] archivo; 
 	
	public ConexionClient(File arch) {
		EncryptFile cargarArchivo = new EncryptFile();
		archivo = cargarArchivo.leerArchivo(arch);
	}
	
	public void iniciarProceso() {
		try {
			Socket socketCliente = new Socket(ip, 15200);
			
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
 
	        String txt = inFromServer.readLine();
	        if(txt.equalsIgnoreCase("ENVIAR")) {
	        	ServidorTCP server = new ServidorTCP(archivo);
	        	server.run();
	        }else {
				JOptionPane.showMessageDialog(null,
						"El cliente cancelo la descarga o se perdio la conexion",
						"Finalizado", JOptionPane.INFORMATION_MESSAGE);
	        }
	        socketCliente.close();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		iniciarProceso();
	}
}