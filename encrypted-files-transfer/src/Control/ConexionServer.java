package Control;


import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

import Vista.InterfazProyecto;

public class ConexionServer extends Thread {
	
	private InterfazProyecto intd;
	
	public ConexionServer(InterfazProyecto s) {
		intd = s;
	}
	
	public void esperarProceso() {
		ServerSocket socketServidor;
			try {
				//Creando nuevo socket de conexion en el puerto 15200
				socketServidor = new ServerSocket(15200);
				while(true) {
					//Espera y acepta cuando reciba conexion 
					Socket client = socketServidor.accept();
					System.out.println("Nueva conexion");
					
					//Conexion y mensaje con IP remota y confimacion 
					BufferedWriter OutFromServer = new BufferedWriter( new OutputStreamWriter(client.getOutputStream() ) );
				    String IPremota= client.getInetAddress().toString();
				    String msj = "Desde la direccion: " + IPremota + " Desea enviarte un archivo. �Quieres recibirlo?";
				    int dialogResult = JOptionPane.showConfirmDialog (null, msj,"Warning",JOptionPane.YES_NO_OPTION);
				    
				    //Da orden de enviar al cliente y corre clienteTCP (se invierten roles)
				    if(dialogResult == JOptionPane.YES_OPTION){
				    	OutFromServer.write("ENVIAR\n");
				    	OutFromServer.flush();
				    	System.out.println("ENVIAR transmitido");
				    	sleep(3000);
				    	ClienteTCP cliente = new ClienteTCP(client.getInetAddress(),intd);
				    	cliente.run();
				    }
				    //Da orden de cancelar al cliente y cierra canal
				    else {
				    	OutFromServer.write("CANCELADO");
				    	OutFromServer.flush();
				    	OutFromServer.close();
			    }
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showConfirmDialog (null, "Ha ocurrido un error en la conexion","Warning",JOptionPane.ERROR_MESSAGE);
			}
		
	}

	@Override
	public void run() {
		esperarProceso();
	}

}
