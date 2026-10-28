package interfaz;

import logica.Barberia;
import logica.Cliente;
import javax.swing.*;
import java.awt.*;

public class PanelClientes extends JPanel {
    private Barberia barberia;
    private DefaultListModel<Cliente> modelo = new DefaultListModel<>();
    private JList<Cliente> lista = new JList<>(modelo);

    public PanelClientes(Barberia barberia) {
        this.barberia = barberia;
        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton btnCrear = new JButton("Crear");
        JButton btnModificar = new JButton("Modificar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnConsultar = new JButton("Consultar");

        btnCrear.addActionListener(e -> crear());
        btnModificar.addActionListener(e -> modificar());
        btnBorrar.addActionListener(e -> borrar());
        btnConsultar.addActionListener(e -> consultar());

        JPanel botones = new JPanel();
        botones.add(btnCrear);
        botones.add(btnModificar);
        botones.add(btnBorrar);
        botones.add(btnConsultar);
        add(botones, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.clear();
        for (Cliente c : barberia.getClientes()) {
            modelo.addElement(c);
        }
    }

    private void crear() {
        JTextField txtNombre = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtTel = new JTextField();
        if (!pedirDatos("Crear cliente", txtNombre, txtEmail, txtTel)) {
            return;
        }
        try {
            boolean creado = barberia.crearCliente(txtNombre.getText().trim(),
                    txtEmail.getText().trim(), txtTel.getText().trim());
            if (!creado) {
                JOptionPane.showMessageDialog(this, "Ese cliente ya existe (el email ya está registrado).");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        refrescar();
    }

    private void modificar() {
        Cliente cliente = lista.getSelectedValue();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la lista.");
            return;
        }
        JTextField txtNombre = new JTextField(cliente.getNombre());
        JTextField txtEmail = new JTextField(cliente.getEmail());
        JTextField txtTel = new JTextField(cliente.getTelefono());
        if (!pedirDatos("Modificar cliente", txtNombre, txtEmail, txtTel)) {
            return;
        }
        try {
            boolean ok = barberia.modificarCliente(cliente.getEmail(), txtNombre.getText().trim(),
                    txtEmail.getText().trim(), txtTel.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Los datos fueron guardados correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar: el email ya pertenece a otro cliente.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        refrescar();
    }

    private void borrar() {
        Cliente cliente = lista.getSelectedValue();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la lista.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Borrar a " + cliente.getNombre() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        if (!barberia.borrarCliente(cliente.getEmail())) {
            JOptionPane.showMessageDialog(this,
                    "No se puede borrar: el cliente tiene citas futuras o está en la lista de espera.");
        }
        refrescar();
    }

    private void consultar() {
        Cliente cliente = lista.getSelectedValue();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la lista.");
            return;
        }
        JOptionPane.showMessageDialog(this, "Nombre: " + cliente.getNombre()
                + "\nEmail: " + cliente.getEmail()
                + "\nTeléfono: " + cliente.getTelefono());
    }

    // Muestra un formulario con los 3 campos. Devuelve true si el usuario presionó OK.
    private boolean pedirDatos(String titulo, JTextField nombre, JTextField email, JTextField tel) {
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.add(new JLabel("Nombre completo:"));
        p.add(nombre);
        p.add(new JLabel("Email:"));
        p.add(email);
        p.add(new JLabel("Teléfono:"));
        p.add(tel);
        int r = JOptionPane.showConfirmDialog(this, p, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return r == JOptionPane.OK_OPTION;
    }
}
