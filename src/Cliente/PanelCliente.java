package Cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PanelCliente extends JPanel implements Runnable{
	
	private JTextField campo1;
	private JButton boton;
	private JTextArea areaChat;
	private JTextField nick;
	private JTextField ip;
	
	public PanelCliente() {
		
		nick = new JTextField(5);
		add(nick);
		JLabel cliente = new JLabel("-Chat-");
		add(cliente);		
		ip = new JTextField(15);
		add(ip);
		campo1 = new JTextField(20);		
		areaChat = new JTextArea(12, 20);
		
				
		
		add(areaChat);
		add(campo1);
		
		boton = new JButton("Enviar");
		
		boton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				
				try {
					// Crear la vía de comunicación
					Socket socket = new Socket("192.168.1.83", 9999);
					
					EnvioPaqueteDatos paquete = new EnvioPaqueteDatos();
					
					paquete.setNick(nick.getText());
					paquete.setIp(ip.getText());
					paquete.setTexto(campo1.getText());
					
					ObjectOutputStream objetoSalida = new ObjectOutputStream(socket.getOutputStream());
					
					objetoSalida.writeObject(paquete);
					
					socket.close();
					
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
				areaChat.append("nick: " + paqueteDatosRecibido.getNick() + "\n" +
								"mensaje: " + paqueteDatosRecibido.getTexto() + "\n");
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	

}
