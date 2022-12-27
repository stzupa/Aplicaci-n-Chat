package Cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PanelCliente extends JPanel implements Runnable{
	
	private JTextField mensaje;
	private JButton boton;
	private JTextArea areaChat;
	private JLabel nick;
	private JComboBox ip;
	private InetAddress mInetAddress;
	private String miIp;
	
	public PanelCliente() throws UnknownHostException {
		
		mInetAddress = InetAddress.getLocalHost();
		miIp = mInetAddress.getHostAddress();
		nick = new JLabel();
		nick.setText(JOptionPane.showInputDialog("Nick: "));
		add(nick);
		JLabel cliente = new JLabel("-Chat-");
		add(cliente);		
		ip = new JComboBox<>();
		add(ip);
		mensaje = new JTextField(20);		
		areaChat = new JTextArea(12, 20);
		areaChat.setEditable(false);	
		
		add(areaChat);
		add(mensaje);
		
		boton = new JButton("Enviar");
		
		boton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				
				try {
					// Crear la vía de comunicación
					Socket socket = new Socket("192.168.1.83", 9999);
					
					EnvioPaqueteDatos paquete = new EnvioPaqueteDatos();
					
					paquete.setNick(nick.getText());
					paquete.setIp(ip.getSelectedItem().toString());
					paquete.setTexto(mensaje.getText());
					
					ObjectOutputStream objetoSalida = new ObjectOutputStream(socket.getOutputStream());
					
					objetoSalida.writeObject(paquete);
					
					socket.close();
					
					areaChat.append("yo:" + "\n" + paquete.getTexto() + "\n");
					
					mensaje.setText("");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		add(boton);		
		
		Thread hiloThread = new Thread(this);
		hiloThread.start();		
		
	}

	@Override
	public void run() {
		
		try {
			
			ServerSocket serverSocketEscucha = new ServerSocket(9990);
			Socket clienteSocket;
			EnvioPaqueteDatos paqueteDatosRecibido;
			
			while(true) {
				
				clienteSocket = serverSocketEscucha.accept();
				ObjectInputStream flujoEntrada = new ObjectInputStream(clienteSocket.getInputStream());
				paqueteDatosRecibido = (EnvioPaqueteDatos) flujoEntrada.readObject();
				
				if (paqueteDatosRecibido.getTexto().equals(" online")) {
					//areaChat.append("\n" + paqueteDatosRecibido.getDireccionesIp());
					ArrayList<String> ipS = new ArrayList<>();
					
					ipS = paqueteDatosRecibido.getDireccionesIp();
					
					for (String string : ipS) {
						System.out.println(string);
						if (!miIp.equals(string)) {
							ip.addItem(string);
						}						
					}
					
				}else {
					areaChat.append(paqueteDatosRecibido.getNick() + "\n" +
									paqueteDatosRecibido.getTexto() + "\n");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}	

}
