package logica;

import java.io.Serializable;

public class Notificador implements Serializable {
    public void enviarNotificacionesPendientes(Barberia barberia) {
        for (Cita c : barberia.obtenerCitasParaNotificar()) {
            System.out.println("Enviando notificación a: " + c.getCliente().getEmail());
        }
    }

    public void enviarConfirmacion(Cita cita) {
    if (cita != null && cita.getCliente() != null) {
        System.out.println("Enviando confirmación de cita a: " + cita.getCliente().getEmail());
    }
}
}