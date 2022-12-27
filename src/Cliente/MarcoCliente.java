package Cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MarcoCliente extends JFrame {
	
	public MarcoCliente() throws UnknownHostException {
		
		setBounds(300, 300, 250, 350);
		PanelCliente panel = new PanelCliente();
		add(panel);
		setVisible(true);
		// Llamada a metodo que informa al servidor nuevo usuario conectado
		estableceConexionConServidor();
		
	}
	
	// Metodo que envia nuevo usuario conenctado
	public void estableceConexionConServidor() {
		
		try {
			
			Socket socket = new Socket("192.168.1.83", 9999);
			EnvioPaqueteDatos paqueteDatos = new EnvioPaqueteDatos();
			paqueteDatos.setTexto(" online");
			ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
			flujoSalida.writeObject(paqueteDatos);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
