package logica;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class HorarioAtencion implements Serializable{
    private Map<DayOfWeek, RangoHorario> horariosPorDia;

    public static class RangoHorario {
        private LocalTime inicio;
        private LocalTime fin;

        public RangoHorario(LocalTime inicio, LocalTime fin) {
            this.inicio = inicio;
            this.fin = fin;
        }

        public LocalTime getInicio() {
            return inicio;
        }

        public void setInicio(LocalTime inicio) {
            this.inicio = inicio;
        }

        public LocalTime getFin() {
            return fin;
        }

        public void setFin(LocalTime fin) {
            this.fin = fin;
        }
    }

    public HorarioAtencion() {
        this.horariosPorDia = new  HashMap<>();
    }

    public void establecerHorario(DayOfWeek dia, LocalTime inicio, LocalTime fin) {
        RangoHorario rango = new RangoHorario(inicio, fin);
        horariosPorDia.put(dia, rango);
    }

    public boolean estaDentroDelHorario(LocalDate fecha, LocalTime hora) {
        DayOfWeek dia = fecha.getDayOfWeek();
        RangoHorario rango = horariosPorDia.get(dia);
        if (rango == null) {
            return false;
        }
        boolean despuesDeInicio = !hora.isBefore(rango.getInicio());
        boolean antesDeFin = !hora.isAfter(rango.getFin());

        return despuesDeInicio && antesDeFin;
    }
}
