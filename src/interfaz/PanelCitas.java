package interfaz;

import logica.Barberia;
import logica.Cita;
import logica.Cliente;
import logica.TipoServicio;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PanelCitas extends JPanel {
    private Barberia barberia;
    private DefaultListModel<Cita> modelo = new DefaultListModel<>();
    private JList<Cita> lista = new JList<>(modelo);

    private static final String MSG_FORMATO =
            "Formato inválido. Use AAAA-MM-DD para la fecha y HH:mm para la hora (ejemplo: 14:30).";

    public PanelCitas(Barberia barberia) {
        this.barberia = barberia;
        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton btnCrear = new JButton("Crear");
        JButton btnModificar = new JButton("Modificar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnConfirmar = new JButton("Confirmar");
        JButton btnCalendario = new JButton("Ver calendario");

        btnCrear.addActionListener(e -> crear());
        btnModificar.addActionListener(e -> modificar());
        btnBorrar.addActionListener(e -> borrar());
        btnConsultar.addActionListener(e -> consultar());
        btnConfirmar.addActionListener(e -> confirmar());
        btnCalendario.addActionListener(e -> verCalendario());

        JPanel botones = new JPanel();
        botones.add(btnCrear);
        botones.add(btnModificar);
        botones.add(btnBorrar);
        botones.add(btnConsultar);
        botones.add(btnConfirmar);
        botones.add(btnCalendario);
        add(botones, BorderLayout.SOUTH);

        refrescar();
    }

    public void refrescar() {
        modelo.clear();
        for (Cita c : ordenar(barberia.getCitas())) {
            modelo.addElement(c);
        }
    }

    // Devuelve una copia de la lista ordenada por fecha y hora
    private List<Cita> ordenar(List<Cita> citas) {
        List<Cita> copia = new ArrayList<>(citas);
        copia.sort(Comparator.comparing(Cita::getFecha).thenComparing(Cita::getHora));
        return copia;
    }

    private Cita citaSeleccionada() {
        Cita cita = lista.getSelectedValue();
        if (cita == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita de la lista.");
        }
        return cita;
    }

    private void crear() {
        if (barberia.getClientes().isEmpty() || barberia.getTiposServicio().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Primero debe existir al menos un cliente y un tipo de servicio.");
            return;
        }
        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        JTextField txtHora = new JTextField("09:00");
        JComboBox<Cliente> cmbCliente = new JComboBox<>(barberia.getClientes().toArray(new Cliente[0]));
        JComboBox<TipoServicio> cmbServicio = new JComboBox<>(barberia.getTiposServicio().toArray(new TipoServicio[0]));

        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
        p.add(new JLabel("Fecha (AAAA-MM-DD):"));
        p.add(txtFecha);
        p.add(new JLabel("Hora (HH:mm):"));
        p.add(txtHora);
        p.add(new JLabel("Cliente:"));
        p.add(cmbCliente);
        p.add(new JLabel("Servicio:"));
        p.add(cmbServicio);

        int r = JOptionPane.showConfirmDialog(this, p, "Crear cita",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            if (!barberia.getHorarioAtencion().estaDentroDelHorario(fecha, hora)) {
                JOptionPane.showMessageDialog(this, "La fecha u hora está fuera del horario de atención.");
                return;
            }
            if (barberia.estaOcupado(fecha, hora, null)) {
                JOptionPane.showMessageDialog(this, "Esa hora ya está ocupada por otra cita.");
                return;
            }
            barberia.crearCita(fecha, hora, (Cliente) cmbCliente.getSelectedItem(),
                    (TipoServicio) cmbServicio.getSelectedItem());
            refrescar();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, MSG_FORMATO);
        }
    }

    private void modificar() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }
        JTextField txtFecha = new JTextField(cita.getFecha().toString());
        JTextField txtHora = new JTextField(cita.getHora().toString());
        JComboBox<TipoServicio> cmbServicio = new JComboBox<>(barberia.getTiposServicio().toArray(new TipoServicio[0]));
        cmbServicio.setSelectedItem(cita.getTipoServicio());

        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.add(new JLabel("Fecha (AAAA-MM-DD):"));
        p.add(txtFecha);
        p.add(new JLabel("Hora (HH:mm):"));
        p.add(txtHora);
        p.add(new JLabel("Servicio:"));
        p.add(cmbServicio);

        int r = JOptionPane.showConfirmDialog(this, p, "Modificar cita",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            if (!barberia.getHorarioAtencion().estaDentroDelHorario(fecha, hora)) {
                JOptionPane.showMessageDialog(this, "La fecha u hora no se encuentra dentro del horario de atención.");
                return;
            }
            if (barberia.estaOcupado(fecha, hora, cita)) {
                JOptionPane.showMessageDialog(this, "Esa hora ya está ocupada por otra cita.");
                return;
            }
            barberia.modificarCita(cita, fecha, hora, (TipoServicio) cmbServicio.getSelectedItem());
            refrescar();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, MSG_FORMATO);
        }
    }

    private void borrar() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Borrar esta cita?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            barberia.borrarCita(cita);
            refrescar();
        }
    }

    private void consultar() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }
        JOptionPane.showMessageDialog(this, "Fecha: " + cita.getFecha()
                + "\nHora: " + cita.getHora()
                + "\nCliente: " + cita.getCliente().getNombre()
                + "\nServicio: " + cita.getTipoServicio().getDescripcion()
                + "\nEstado: " + (cita.isConfirmada() ? "Confirmada" : "Sin confirmar"));
    }

    private void confirmar() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }
        if (cita.isConfirmada()) {
            JOptionPane.showMessageDialog(this, "Esa cita ya estaba confirmada.");
            return;
        }
        barberia.confirmarCita(cita);
        refrescar();
    }

    private void verCalendario() {
        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        JComboBox<String> cmbVista = new JComboBox<>(new String[]{"semana", "mes"});

        JPanel p = new JPanel(new GridLayout(2, 2, 5, 5));
        p.add(new JLabel("Fecha de inicio (AAAA-MM-DD):"));
        p.add(txtFecha);
        p.add(new JLabel("Ver por:"));
        p.add(cmbVista);

        int r = JOptionPane.showConfirmDialog(this, p, "Ver calendario de citas",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            LocalDate inicio = LocalDate.parse(txtFecha.getText().trim());
            List<Cita> citas = ordenar(barberia.verCalendario(inicio, (String) cmbVista.getSelectedItem()));

            StringBuilder texto = new StringBuilder();
            for (Cita c : citas) {
                texto.append(c).append("\n");
            }
            if (citas.isEmpty()) {
                texto.append("No hay citas en ese período.");
            }
            JTextArea area = new JTextArea(texto.toString(), 12, 50);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Calendario de citas",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD.");
        }
    }
}
