package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Control.ConexionClient;
import Control.ConexionServer;



public class InterfazProyecto extends JFrame {
	private static final long serialVersionUID = 10L;
	
	private PanelServidor panelServidor;
	private String nomb;
	private File archivo;
	public InterfazProyecto() {
		archivo = null;
		nomb = "";
		setTitle("Transferencia segura de archivos");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//setResizable(false);
		setSize(516, 366);
		centrarPantalla();
		
		panelServidor = new PanelServidor(this);
		panelServidor.setVisible(true);
		
		add(panelServidor, BorderLayout.CENTER);
	
		ConexionServer conexionServer = new ConexionServer();
		conexionServer.start();
	}
	
	public void centrarPantalla() {
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = getSize();
		setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
	}
	public void iniciarProceso(String ipRemota) {
		if(archivo!=null) {
			System.out.println(ipRemota);
			ConexionClient conexionCliente = new ConexionClient(archivo, ipRemota, nomb);
			conexionCliente.start();
			
		}
		else {
      	  JOptionPane.showMessageDialog(null,
					"Por favor seleccione un archivo para iniciar la transferencia",
					"No hay archivo seleccionado", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	public void elegirArchivo(File arc, String nom) {
		archivo = arc;
		nomb = nom;
	}
	
	public static void main(String[] args) {
		InterfazProyecto ventana = new InterfazProyecto();
		ventana.setVisible(true);
	}
	
}
