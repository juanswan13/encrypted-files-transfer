package Control;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

import Vista.InterfazProyecto;

public class ConexionClient extends Thread {
	private String ip;
	private byte[] archivo; 
	private String nombre;
	private InterfazProyecto intd;
 	
	public ConexionClient(File arch, String iP, String nomb, InterfazProyecto s) {
		EncryptFile cargarArchivo = new EncryptFile();
		archivo = cargarArchivo.leerArchivo(arch);
		ip = iP;
		nombre= nomb;
		intd = s;
	}
	
	public void iniciarProceso() {
		try {
			Socket socketCliente = new Socket(ip, 15200);
			System.out.println("socket iniciado");
	        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        String txt = inFromServer.readLine();
	        System.out.println(txt);
	        if(txt.equalsIgnoreCase("ENVIAR")) {
	        	ServidorTCP server = new ServidorTCP(archivo, nombre, intd);
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
			JOptionPane.showMessageDialog(null,
					"No se pudo establecer una conexion con esa direccion IP",
					"Finalizado", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void run() {
		iniciarProceso();
	}
}
