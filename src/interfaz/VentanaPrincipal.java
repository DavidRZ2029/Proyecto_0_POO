package interfaz;

import control.ControladorBarberia;
import logica.Barberia;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(ControladorBarberia controlador) {
        super("Barbería - Sistema de citas");
        Barberia barberia = controlador.getBarberia();

        PanelClientes panelClientes = new PanelClientes(barberia);
        PanelCitas panelCitas = new PanelCitas(barberia);
        PanelListaEspera panelEspera = new PanelListaEspera(barberia);
        PanelTiposServicio panelServicios = new PanelTiposServicio(barberia);
        PanelAdministracion panelAdmin = new PanelAdministracion(barberia);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Clientes", panelClientes);
        pestanas.addTab("Citas", panelCitas);
        pestanas.addTab("Lista de espera", panelEspera);
        pestanas.addTab("Tipos de servicio", panelServicios);
        pestanas.addTab("Administración", panelAdmin);

        // Al cambiar de pestaña se actualizan todas las listas
        pestanas.addChangeListener(e -> {
            panelClientes.refrescar();
            panelCitas.refrescar();
            panelEspera.refrescar();
            panelServicios.refrescar();
            panelAdmin.refrescar();
        });

        add(pestanas);
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Guarda los datos en disco al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controlador.cerrarSistema();
            }
        });
    }
}
