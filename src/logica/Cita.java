package logica;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private LocalDate fecha;
    private LocalTime hora;
    private Cliente cliente;
    private TipoServicio tipoServicio;
    private boolean confirmada;

    public Cita(LocalDate fecha, LocalTime hora, Cliente cliente, TipoServicio tipoServicio, boolean confirmada) {
        this.fecha = fecha;
        this.hora = hora;
        this.cliente = cliente;
        this.tipoServicio = tipoServicio;
        this.confirmada = confirmada;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }

    public void confirmar() {
        this.confirmada = true;
    }
}
