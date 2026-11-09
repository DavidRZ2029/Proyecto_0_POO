package interfaz;

import logica.Barberia;
import logica.TipoServicio;
import javax.swing.*;
import java.awt.*;

public class PanelTiposServicio extends JPanel {
    private Barberia barberia;
    private DefaultListModel<TipoServicio> modelo = new DefaultListModel<>();
    private JList<TipoServicio> lista = new JList<>(modelo);

    public PanelTiposServicio(Barberia barberia) {
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
        for (TipoServicio t : barberia.getTiposServicio()) {
            modelo.addElement(t);
        }
    }

    private void crear() {
        String desc = JOptionPane.showInputDialog(this, "Descripción del servicio:");
        if (desc == null || desc.trim().isEmpty()) {
            return;
        }
        barberia.crearTipoServicio(desc.trim());
        refrescar();
    }

    private void modificar() {
        TipoServicio servicio = lista.getSelectedValue();
        if (servicio == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio de la lista.");
            return;
        }
        String desc = JOptionPane.showInputDialog(this, "Nueva descripción:", servicio.getDescripcion());
        if (desc == null || desc.trim().isEmpty()) {
            return;
        }
        barberia.modificarTipoServicio(servicio, desc.trim());
        refrescar();
    }

    private void borrar() {
        TipoServicio servicio = lista.getSelectedValue();
        if (servicio == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio de la lista.");
            return;
        }
        if (!barberia.borrarTipoServicio(servicio)) {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar: el tipo de servicio está siendo utilizado en una cita.");
        }
        refrescar();
    }

    private void consultar() {
        TipoServicio servicio = lista.getSelectedValue();
        if (servicio == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un servicio de la lista.");
            return;
        }
        JOptionPane.showMessageDialog(this, "Descripción: " + servicio.getDescripcion());
    }
}
