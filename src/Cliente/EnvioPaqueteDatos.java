package Cliente;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class EnvioPaqueteDatos implements Serializable {
	
	private String nick, ip, texto;
	private ArrayList<String> direccionesIp;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public ArrayList<String> getDireccionesIp() {
		return direccionesIp;
	}

	public void setDireccionesIp(ArrayList<String> direccionesIp) {
		this.direccionesIp = direccionesIp;
	}
	
	

}
