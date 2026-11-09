package interfaz;

import logica.Barberia;
import logica.Cita;
import logica.HorarioAtencion.RangoHorario;
import logica.Notificador;
import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelAdministracion extends JPanel {
    private Barberia barberia;
    private Notificador notificador = new Notificador();

    private static final DayOfWeek[] DIAS = {
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
    };
    private static final String[] NOMBRES = {
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    };

    private JTextField[] txtInicio = new JTextField[7];
    private JTextField[] txtFin = new JTextField[7];

    private DefaultListModel<Cita> modeloPendientes = new DefaultListModel<>();
    private JLabel lblEstado = new JLabel(" ");

    public PanelAdministracion(Barberia barberia) {
        this.barberia = barberia;
        setLayout(new GridLayout(1, 2, 10, 10));
        add(crearPanelHorario());
        add(crearPanelNotificaciones());
        refrescar();
    }

    // ---------- Horario de atención ----------
    private JPanel crearPanelHorario() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Horario de atención (HH:mm, vacío = cerrado)"));

        JPanel filas = new JPanel(new GridLayout(8, 3, 5, 5));
        filas.add(new JLabel("Día"));
        filas.add(new JLabel("Inicio"));
        filas.add(new JLabel("Fin"));
        for (int i = 0; i < 7; i++) {
            txtInicio[i] = new JTextField(5);
            txtFin[i] = new JTextField(5);
            filas.add(new JLabel(NOMBRES[i]));
            filas.add(txtInicio[i]);
            filas.add(txtFin[i]);
        }
        panel.add(filas, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar horario");
        btnGuardar.addActionListener(e -> guardarHorario());
        panel.add(btnGuardar, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarHorario() {
        for (int i = 0; i < 7; i++) {
            RangoHorario rango = barberia.getHorarioAtencion().getRango(DIAS[i]);
            if (rango == null) {
                txtInicio[i].setText("");
                txtFin[i].setText("");
            } else {
                txtInicio[i].setText(rango.getInicio().toString());
                txtFin[i].setText(rango.getFin().toString());
            }
        }
    }

    private void guardarHorario() {
        LocalTime[] inicios = new LocalTime[7];
        LocalTime[] fines = new LocalTime[7];
        try {
            // Primero se validan todos los días y después se guarda
            for (int i = 0; i < 7; i++) {
                String ini = txtInicio[i].getText().trim();
                String fin = txtFin[i].getText().trim();
                if (ini.isEmpty() && fin.isEmpty()) {
                    continue; // día cerrado
                }
                inicios[i] = LocalTime.parse(ini);
                fines[i] = LocalTime.parse(fin);
                if (!fines[i].isAfter(inicios[i])) {
                    JOptionPane.showMessageDialog(this, NOMBRES[i] + ": la hora de fin debe ser mayor que la de inicio.");
                    return;
                }
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de hora inválido. Use HH:mm (ejemplo: 09:00).");
            return;
        }
        for (int i = 0; i < 7; i++) {
            if (inicios[i] == null) {
                barberia.getHorarioAtencion().quitarHorario(DIAS[i]);
            } else {
                barberia.getHorarioAtencion().establecerHorario(DIAS[i], inicios[i], fines[i]);
            }
        }
        JOptionPane.showMessageDialog(this, "Horario guardado.");
    }

    // ---------- Notificaciones ----------
    private JPanel crearPanelNotificaciones() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Citas de mañana sin confirmar"));

        panel.add(lblEstado, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JList<>(modeloPendientes)), BorderLayout.CENTER);

        JButton btnEnviar = new JButton("Enviar notificaciones por correo");
        btnEnviar.addActionListener(e -> enviarNotificaciones());
        panel.add(btnEnviar, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarPendientes() {
        modeloPendientes.clear();
        List<Cita> pendientes = barberia.obtenerCitasParaNotificar();
        for (Cita c : pendientes) {
            modeloPendientes.addElement(c);
        }
        if (pendientes.isEmpty()) {
            lblEstado.setText("No hay citas programadas para el día siguiente.");
        } else {
            lblEstado.setText(pendientes.size() + " cita(s) por notificar.");
        }
    }

    private void enviarNotificaciones() {
        List<Cita> pendientes = barberia.obtenerCitasParaNotificar();
        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay citas programadas para el día siguiente.");
            return;
        }
        int enviados = 0;
        for (Cita cita : pendientes) {
            boolean listo = false;
            while (!listo) {
                try {
                    notificador.enviarConfirmacion(cita);
                    listo = true;
                    enviados++;
                } catch (Exception ex) {
                    Object[] opciones = {"Reintentar", "Continuar con la siguiente", "Abortar"};
                    int r = JOptionPane.showOptionDialog(this,
                            "No se pudo enviar el correo a " + cita.getCliente().getEmail() + ".",
                            "Error de envío", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                            null, opciones, opciones[0]);
                    if (r == 1) {
                        break; // pasa a la siguiente cita
                    } else if (r != 0) {
                        JOptionPane.showMessageDialog(this, "Envío abortado. Correos enviados: " + enviados);
                        return;
                    }
                    // si r == 0 el while repite el envío
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Proceso terminado. Correos enviados: " + enviados);
    }

    public void refrescar() {
        cargarHorario();
        cargarPendientes();
    }
}

