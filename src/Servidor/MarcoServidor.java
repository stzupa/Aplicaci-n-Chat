package Servidor;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Cliente.EnvioPaqueteDatos;

@SuppressWarnings("serial")
public class MarcoServidor extends JFrame implements Runnable{
	
	private JTextArea areaTexto;
	
	public MarcoServidor() {
		
		setBounds(600,300,280,350);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		areaTexto = new JTextArea();
		panel.add(areaTexto, BorderLayout.CENTER);
		add(panel);
		setVisible(true);
		Thread hiloThread =new Thread(this);
		hiloThread.start();
	}

	@Override
	public void run() {
		
		try {
			ServerSocket servidor = new ServerSocket(9999);
			String nick, ip, texto;
			EnvioPaqueteDatos paqueteRecibido;
			
			while(true) {
			
			Socket socket = servidor.accept();
			ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
			paqueteRecibido = (EnvioPaqueteDatos) flujoEntrada.readObject();
			
			nick = paqueteRecibido.getNick();
			ip = paqueteRecibido.getIp();
			texto = paqueteRecibido.getTexto();
			
			areaTexto.append("nick: " + nick + "\n" +
								"ip: " + ip + "\n" +
								"mensaje: " + texto + "\n");	
			
			// Crear socket para salida de información
			
			Socket socketSalida = new Socket(ip, 9990);
			
			ObjectOutputStream paqueteSalida = new ObjectOutputStream(socketSalida.getOutputStream());
			
			paqueteSalida.writeObject(paqueteRecibido);
			
			socketSalida.close();
			}
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

}
