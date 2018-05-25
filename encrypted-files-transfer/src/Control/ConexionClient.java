package Control;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ConexionClient extends Thread {
	private String ip;
	private byte[] archivo; 
	private String nombre;
 	
	public ConexionClient(File arch, String iP, String nomb) {
		EncryptFile cargarArchivo = new EncryptFile();
		archivo = cargarArchivo.leerArchivo(arch);
		ip = iP;
		nombre= nomb;
	}
	
	public void iniciarProceso() {
		try {
			Socket socketCliente = new Socket(ip, 15200);
			System.out.println("socket iniciado");
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        String txt = inFromServer.readLine();
	        System.out.println(txt);
	        if(txt.equalsIgnoreCase("ENVIAR")) {
	        	ServidorTCP server = new ServidorTCP(archivo, nombre);
	        	System.out.println("Iniciando servidor");
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
		iniciarProceso();
	}
}
