package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;



public class InterfazProyecto extends JFrame {
	private static final long serialVersionUID = 10L;
	
	private PanelServidor panelServidor;
	private File archivo;
	public InterfazProyecto() {
		archivo = null;
		setTitle("Transferencia segura de archivos");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setResizable(false);
		setSize(516, 366);
		centrarPantalla();
		
		panelServidor = new PanelServidor(this);
		panelServidor.setVisible(true);
		
		add(panelServidor, BorderLayout.CENTER);
	}
	
	public void centrarPantalla() {
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ventana = getSize();
		setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
	}
	public void iniciarProceso() {
		
	}
	
	public void elegirArchivo(File arc) {
		archivo = arc;
	}
	
	public static void main(String[] args) {
		InterfazProyecto ventana = new InterfazProyecto();
		ventana.setVisible(true);
	}
	
}
