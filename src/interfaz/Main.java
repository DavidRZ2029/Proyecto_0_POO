package interfaz;

import control.ControladorBarberia;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorBarberia controlador = new ControladorBarberia();
            VentanaPrincipal ventana = new VentanaPrincipal(controlador);
            ventana.setVisible(true);
        });
    }
}

