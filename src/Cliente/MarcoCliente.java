package Cliente;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MarcoCliente extends JFrame {
	
	public MarcoCliente() {
		
		setBounds(300, 300, 250, 350);
		PanelCliente panel = new PanelCliente();
		add(panel);
		setVisible(true);
		
	}

}
