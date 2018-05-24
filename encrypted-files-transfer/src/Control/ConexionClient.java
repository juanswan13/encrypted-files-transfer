package Control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ConexionClient extends Thread {
	private String ip;
	private byte[] archivo; 
 	
	public ConexionClient(String ruta) {
		EncryptFile cargarArchivo = new EncryptFile();
		archivo = cargarArchivo.leerArchivo(ruta);
	}
	
	public void iniciarProceso() {
		try {
			Socket socketCliente = new Socket(ip, 15210);
			
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        BufferedWriter OutFromClient = new BufferedWriter( new OutputStreamWriter( socketCliente.getOutputStream() ) );
	        
	        String txt = inFromServer.readLine();
	        if(txt.equalsIgnoreCase("ENVIAR")) {
	        	ServidorTCP server = new ServidorTCP(archivo);
	        	server.run();
	        }else {
				JOptionPane.showMessageDialog(null,
						"El cliente cancelo la descarga o se perdio la conexion",
						"Finalizado", JOptionPane.INFORMATION_MESSAGE);
	        }
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
