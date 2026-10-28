package interfaz;

import logica.Barberia;
import logica.Cliente;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelListaEspera extends JPanel {
    private Barberia barberia;
    private DefaultListModel<String> modelo = new DefaultListModel<>();
    private JList<String> lista = new JList<>(modelo);

    public PanelListaEspera(Barberia barberia) {
        this.barberia = barberia;
        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton btnAgregar = new JButton("Agregar cliente");
        JButton btnBorrar = new JButton("Borrar cliente de la lista");
        btnAgregar.addActionListener(e -> agregar());
        btnBorrar.addActionListener(e -> borrar());

        JPanel botones = new JPanel();
        botones.add(btnAgregar);
        botones.add(btnBorrar);
        add(botones, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.clear();
        List<Cliente> espera = barberia.getListaEspera().obtenerListaEspera();
        for (int i = 0; i < espera.size(); i++) {
            modelo.addElement((i + 1) + ". " + espera.get(i).getNombre());
        }
    }

    private void agregar() {
        if (barberia.getClientes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay clientes registrados.");
            return;
        }
        Cliente[] opciones = barberia.getClientes().toArray(new Cliente[0]);
        Object elegido = JOptionPane.showInputDialog(this, "Elija el cliente:", "Agregar a lista de espera",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (elegido == null) {
            return;
        }
        if (!barberia.getListaEspera().agregarCliente((Cliente) elegido)) {
            JOptionPane.showMessageDialog(this, "Ese cliente ya está en la lista de espera.");
        }
        refrescar();
    }

    private void borrar() {
        int posicion = lista.getSelectedIndex();
        if (posicion < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la lista.");
            return;
        }
        Cliente cliente = barberia.getListaEspera().obtenerListaEspera().get(posicion);
        barberia.getListaEspera().borrarCliente(cliente);
        refrescar();
    }
}
