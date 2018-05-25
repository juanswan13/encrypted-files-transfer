package Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.InetAddress;
import java.net.DatagramSocket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class PanelServidor extends JPanel implements ActionListener{
private static final long serialVersionUID = 1877L;

	private InterfazProyecto principal;
	private JLabel lblIp, lblIpR;
	private JTextArea txtCypherInfo;
	private JTextField txtIpOct1, txtIpOct2, txtIpOct3, txtIpOct4, txtNombreArchivo;
	private JTextField txtIpOctR1, txtIpOctR2, txtIpOctR3, txtIpOctR4;
	private JProgressBar progressBar;
	private JButton btnSubir, btnEnviar;
	private JList lista;
	private JFileChooser fc;
	
	public PanelServidor(InterfazProyecto interfaz){
		setPrincipal(interfaz);
		setBorder(BorderFactory.createMatteBorder(0, 0 , 1, 1, new Color(22, 160, 133)));
		setVisible(false);
		setPreferredSize( new Dimension( 750, 445 ) );
		setBackground( new Color(255,255,255) );
		setLayout( new GridBagLayout( ) );
	    GridBagConstraints gbc = new GridBagConstraints();	
	    gbc.insets = new Insets(2,2,2,2);
	    gbc.fill = GridBagConstraints.BOTH;
	    fc = new JFileChooser();
	    
//DISEÑO DE LA LINEA DE IP.
	    String ipLoc = "";
	    try {
	    	 DatagramSocket socket = new DatagramSocket();
	    	 socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
	    	 ipLoc = socket.getLocalAddress().getHostAddress();
	    }catch(Exception e) {
	    	e.printStackTrace();
	    }
	    lblIp = new JLabel("    IP Local : ");
	    txtIpOct1 = new JTextField(3);
	    txtIpOct1.setEditable(false);
	    txtIpOct1.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOct1.getText().length()== 3)
	    	 	     e.consume();
	    		if (txtIpOct1.getText().length() == 2)
	    			 txtIpOct2.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    txtIpOct2 = new JTextField(3);
	    txtIpOct2.setEditable(false);
	    txtIpOct2.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOct2.getText().length() == 3)
	    	 	     e.consume();
	    		if (txtIpOct2.getText().length() == 2)
	    			 txtIpOct3.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    txtIpOct3 = new JTextField(3);
	    txtIpOct3.setEditable(false);
	    txtIpOct3.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOct3.getText().length()== 3)
	    	 	     e.consume();
	    		if (txtIpOct3.getText().length() == 2)
	    			 txtIpOct4.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    txtIpOct4 = new JTextField(3);
	    txtIpOct4.setEditable(false);
	    txtIpOct4.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOct4.getText().length()== 3)
	    	 	     e.consume();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    JLabel punto1 =  new JLabel(".");
	    JLabel punto2 =  new JLabel(".");
	    JLabel punto3 =  new JLabel(".");
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    add(lblIp, gbc);
	    gbc.gridx = 2;
	    gbc.gridy = 0;
	    add(txtIpOct1, gbc);
	    gbc.gridx = 3;
	    gbc.gridy = 0;
	    add(punto1, gbc);
	    gbc.gridx = 4;
	    gbc.gridy = 0;
	    add(txtIpOct2, gbc);
	    gbc.gridx = 5;
	    gbc.gridy = 0;
	    add(punto2, gbc);
	    gbc.gridx = 6;
	    gbc.gridy = 0;
	    add(txtIpOct3, gbc);
	    gbc.gridx = 7;
	    gbc.gridy = 0;
	    add(punto3, gbc);
	    gbc.gridx = 8;
	    gbc.gridy = 0;
	    add(txtIpOct4, gbc);
	    gbc.gridx = 9;
	    gbc.gridy = 0;
	    add(new JLabel(""), gbc);
	    gbc.gridx = 10;
	    gbc.gridy = 2;
	    add(new JLabel("         "), gbc);
	    txtIpOct1.setText(ipLoc.replace('.', 'f').split("f")[0]+"");
	    txtIpOct2.setText(ipLoc.replace('.', 'f').split("f")[1]+"");
	    txtIpOct3.setText(ipLoc.replace('.', 'f').split("f")[2]+"");
	    txtIpOct4.setText(ipLoc.replace('.', 'f').split("f")[3]+"");
//FIN DE DISEÑO LINEA IP
	    
//DISEÑO DE LA LINEA DE IP REMOTA.
	    lblIpR = new JLabel("    IP Remota : ");
	    txtIpOctR1 = new JTextField(3);
	    txtIpOctR1.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctR1.getText().length()== 3)
	    	 	     e.consume();
	    		if (txtIpOctR1.getText().length() == 2)
	    			 txtIpOctR2.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    txtIpOctR2 = new JTextField(3);
	    txtIpOctR2.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctR2.getText().length() == 3)
	    	 	     e.consume();
	    		if (txtIpOctR2.getText().length() == 2)
	    			 txtIpOctR3.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    txtIpOctR3 = new JTextField(3);
	    txtIpOctR3.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctR3.getText().length()== 3)
	    	 	     e.consume();
	    		if (txtIpOctR3.getText().length() == 2)
	    			 txtIpOctR4.requestFocus();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    txtIpOctR4 = new JTextField(3);
	    txtIpOctR4.addKeyListener(new KeyListener(){
	    	public void keyTyped(KeyEvent e){
	    		if (txtIpOctR4.getText().length()== 3)
	    	 	     e.consume();
	    	}
	    	public void keyPressed(KeyEvent arg0) {
	    	}
	    	public void keyReleased(KeyEvent arg0) {
	    	}
	    	});
	    JLabel punto1R =  new JLabel(".");
	    JLabel punto2R =  new JLabel(".");
	    JLabel punto3R =  new JLabel(".");
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    add(lblIpR, gbc);
	    gbc.gridx = 2;
	    gbc.gridy = 1;
	    add(txtIpOctR1, gbc);
	    gbc.gridx = 3;
	    gbc.gridy = 1;
	    add(punto1R, gbc);
	    gbc.gridx = 4;
	    gbc.gridy = 1;
	    add(txtIpOctR2, gbc);
	    gbc.gridx = 5;
	    gbc.gridy = 1;
	    add(punto2R, gbc);
	    gbc.gridx = 6;
	    gbc.gridy = 1;
	    add(txtIpOctR3, gbc);
	    gbc.gridx = 7;
	    gbc.gridy = 1;
	    add(punto3R, gbc);
	    gbc.gridx = 8;
	    gbc.gridy = 1;
	    add(txtIpOctR4, gbc);
	    gbc.gridx = 9;
	    gbc.gridy = 1;
	    add(new JLabel(""), gbc);
//FIN DE DISEÑO LINEA IP REMOTA
	    
//INICIO DEL AREA DE TEXTO DONDE VA LA INFORMACION DE CIFRADO
	    txtCypherInfo = new JTextArea("");
	    txtCypherInfo.setEditable(false);
	    txtCypherInfo.setText("- Algoritmo de cifrado: AES" + "\n"
	    						+ "- Clave: 128 bits" + "\n"
	    						+ "- Algoritmo de clave de sisión: Diffie-Hellman");
	    Border border = BorderFactory.createLineBorder(Color.BLACK);
	    txtCypherInfo.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
	    gbc.fill =  GridBagConstraints.HORIZONTAL;
	    gbc.gridwidth = 9;
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    add(txtCypherInfo, gbc);
//FIN DE AREA CON INFORMACION DE CIFRADO
	    
//INICIO DE LISTA CON INFO DE INTERCAMBIO DE ARCHIVOS
	    lista = new JList();
	    lista.getBorder();
	    lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );	
	    JScrollPane scroll1 = new JScrollPane(lista, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll1.setPreferredSize(new Dimension(100, 100));	
		scroll1.setViewportView(lista);
	    gbc.gridwidth = 11;
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    add(scroll1, gbc);
//FIN DE LISTA CON INFO DE INTERCAMBIO 
	    
	    
//INICIO BOTON PARA ELEGIR ARCHIVO
	    	//COLOR BOTON = #005BFF
	    btnSubir = new JButton(new ImageIcon("./data/uploadIcon.png"));
	    btnSubir.setOpaque(true);
	    btnSubir.setBorder(null);
	    Color c = UIManager.getLookAndFeel().getDefaults().getColor("Panel.background");
	    btnSubir.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue()));
	    btnSubir.setToolTipText("Elegir archivo para enviar");
	    btnSubir.addActionListener(this);
	    btnSubir.setActionCommand("subir");
	    gbc.gridwidth = 1;
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    add(btnSubir, gbc);
//FIN BOTON PARA ELEGIR ARCHIVO
	    
	    txtNombreArchivo = new JTextField(10);
	    txtNombreArchivo.setText("Agregar archivo...");
	    txtNombreArchivo.setEditable(false);
	    gbc.gridwidth = 9;
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    add(txtNombreArchivo, gbc);
	    
	    
//INICIO BOTON PARA ENVIAR ARCHIVO
	    	//COLOR BOTON = #005BFF
	    btnEnviar = new JButton(new ImageIcon("./data/sendIcon.png"));
	    btnEnviar.setOpaque(true);
	    btnEnviar.setBorder(null);
	    btnEnviar.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue()));
	    btnEnviar.setToolTipText("Enviar archivo !");
	    btnEnviar.addActionListener(this);
	    btnEnviar.setActionCommand("enviar");
	    gbc.gridwidth = 1;
	    gbc.gridx = 10;
	    gbc.gridy = 4;
	    add(btnEnviar, gbc);
//FIN BOTON PARA ENVIAR ARCHIVO
	    
	    progressBar = new JProgressBar(0, 100);
	    progressBar.setValue(0);
	    progressBar.setStringPainted(true);
	    gbc.gridwidth =  GridBagConstraints.REMAINDER;
	    gbc.gridx = 0;
	    gbc.gridy = 5;
	    add(progressBar, gbc);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String p = e.getActionCommand();
		if(p.equals("subir")){
			 int returnVal = fc.showOpenDialog(this);
	         if (returnVal == JFileChooser.APPROVE_OPTION) {
	              File file = fc.getSelectedFile();
	              if(file!=null) {
	              principal.elegirArchivo(file);
	              txtNombreArchivo.setText(file.getName());
	              }
	         }
		}else if(p.equals("enviar")) {
			principal.iniciarProceso();
		}
	}


	/**
	 * @return the principal
	 */
	public InterfazProyecto getPrincipal() {
		return principal;
	}


	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(InterfazProyecto principal) {
		this.principal = principal;
	}
	
}
