package Servidor;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
			// Arreglo que almacena clientes conectados
			ArrayList<String> listaIpConectados = new ArrayList<>();
			
			while(true) {
			
				Socket socket = servidor.accept();
				
				ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
				
				paqueteRecibido = (EnvioPaqueteDatos) flujoEntrada.readObject();
				
				nick = paqueteRecibido.getNick();
				ip = paqueteRecibido.getIp();
				texto = paqueteRecibido.getTexto();
				
				if (!texto.equals(" online")) {
					
					areaTexto.append("nick: " + nick + "\n" + "ip: " + ip + "\n" + "mensaje: " + texto + "\n");

					// Crear socket para salida de información

					Socket socketSalida = new Socket(ip, 9990);

					ObjectOutputStream paqueteSalida = new ObjectOutputStream(socketSalida.getOutputStream());
					
					paqueteSalida.writeObject(paqueteRecibido);

					socketSalida.close();
					
				}else {
					
					// Detectar usuarios que se conectan
					
					InetAddress direccionCliente = socket.getInetAddress();
					
					String ipClienteConectado = direccionCliente.getHostAddress();
					
					listaIpConectados.add(ipClienteConectado);
					
					System.out.println("Direccion remota coenctada: " + ipClienteConectado);
					
					// Agregar el arreglo al objeto a enviar
					
					paqueteRecibido.setDireccionesIp(listaIpConectados);
					
					for (String string : listaIpConectados) {
						
						System.out.println("ArrayList: " + string);
						
						// Enviar el arreglo a los clientes conectados
						
						Socket socketSalida = new Socket(string, 9990);

						ObjectOutputStream paqueteSalida = new ObjectOutputStream(socketSalida.getOutputStream());
						
						paqueteSalida.writeObject(paqueteRecibido);

						socketSalida.close();						
						/////////////////////////////////////////////
					}
					
					/////////////////////////////////////////////////////////////////////////
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

}
