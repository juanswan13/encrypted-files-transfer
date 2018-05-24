package Control;


import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ConexionServer extends Thread {
	
	public void esperarProceso() {
		ServerSocket socketServidor;
			try {
				socketServidor = new ServerSocket(15200);
				while(true) {
					Socket client = socketServidor.accept();
					BufferedWriter OutFromServer = new BufferedWriter( new OutputStreamWriter(client.getOutputStream() ) );
				    String IPremota= client.getInetAddress().toString();
				    String msj = "Desde la dirección: " + IPremota + " Desea enviarte un archivo. ¿Quieres recibirlo ?";
				    int dialogResult = JOptionPane.showConfirmDialog (null, msj,"Warning",JOptionPane.YES_NO_OPTION);
				    if(dialogResult == JOptionPane.YES_OPTION){
				    	OutFromServer.write("ENVIAR");
				    	sleep(1000);
				    	ClienteTCP cliente = new ClienteTCP(client.getInetAddress());
				    	cliente.run();
				    }else {
				    	OutFromServer.write("CANCELADO");
			    }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		esperarProceso();
	}

}
