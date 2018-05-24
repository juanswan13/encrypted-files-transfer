package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;



public class InterfazProyecto extends JFrame {
	private static final long serialVersionUID = 10L;
	
	private PanelServidor panelServidor;
	public InterfazProyecto() {
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
	
	public static void main(String[] args) {
		InterfazProyecto ventana = new InterfazProyecto();
		ventana.setVisible(true);
		
	}
}
