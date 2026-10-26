package logica;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Barberia implements Serializable{
    private List<Cliente> clientes;
    private List<Cita> citas;
    private List<TipoServicio> tiposServicio;
    private ListaEspera listaEspera;
    private HorarioAtencion horarioAtencion;

    public Barberia() {
        this.clientes = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.tiposServicio = new ArrayList<>();
        this.listaEspera = new ListaEspera();
        this.horarioAtencion = new HorarioAtencion();
    }

    public boolean crearCliente(String nombre, String email, String telefono) throws Exception{
        if (consultarCliente(email) != null) {
            return false;
        }
        Cliente temp = new Cliente(nombre, email, telefono);
        if (!temp.validarEmail(email) || !temp.validarTelefono(telefono)){
            return false;
        }
        clientes.add(temp);
        return true;
    }

    public boolean modificarCliente(String emailActual, String nuevoNombre, String nuevoEmail, String nuevoTel) throws Exception{
        Cliente cliente = consultarCliente(emailActual);
        if (cliente == null) {
            return false;
        }
        Cliente tempValidador = new Cliente(nuevoNombre, nuevoEmail, nuevoTel);
        if (!tempValidador.validarEmail(nuevoEmail) || !tempValidador.validarTelefono(nuevoTel)) {
            return false;
        }

        cliente.setNombre(nuevoNombre);
        cliente.setEmail(nuevoEmail);
        cliente.setTelefono(nuevoTel);
        return true;
    }

    public boolean borrarCliente(String email) {
        Cliente cliente = consultarCliente(email);
        if (cliente == null) {
            return false;
        }

        for (Cita c : citas) {
            if (c.getCliente().equals(cliente) && c.getFecha().isAfter(LocalDate.now())) {
                return false;
            }
        }

        if (listaEspera.obtenerListaEspera().contains(cliente)) {
            return false;
        }

        clientes.remove(cliente);
        return true;
    }

    public Cliente consultarCliente(String email) {
        for (Cliente c : clientes) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        return null;
    }

    public boolean crearCita(LocalDate fecha, LocalTime hora, Cliente cliente, TipoServicio servicio) {
        if (!clientes.contains(cliente) || !tiposServicio.contains(servicio)) {
            return false;
        }

        if (!horarioAtencion.estaDentroDelHorario(fecha, hora)) {
            return false;
        }

        for (Cita c : citas) {
            if (c.getFecha().equals(fecha) && c.getHora().equals(hora)) {
                return false;
            }
        }

        Cita nuevaCita = new Cita(fecha, hora, cliente, servicio);
        citas.add(nuevaCita);
        return true;
    }

    public boolean modificarCita(Cita cita, LocalDate nuevaFecha, LocalTime nuevaHora, TipoServicio nuevoServicio) {
        if (!citas.contains(cita) || !tiposServicio.contains(nuevoServicio)) {
            return false;
        }

        if (!horarioAtencion.estaDentroDelHorario(nuevaFecha, nuevaHora)) {
            return false;
        }

        cita.setFecha(nuevaFecha);
        cita.setHora(nuevaHora);
        cita.setTipoServicio(nuevoServicio);
        return true;
    }

    public void borrarCita(Cita cita) {
        citas.remove(cita);
    }

    public void confirmarCita(Cita cita) {
        cita.confirmar();
    }

    public List<Cita> obtenerCitasParaNotificar() {
        LocalDate diaSiguiente = LocalDate.now().plusDays(1);
        List<Cita> resultado = new ArrayList<>();
        for (Cita c: citas) {
            if (c.getFecha().equals(diaSiguiente) && !c.isConfirmada()) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Cita> verCalendario(LocalDate fechaInicio, String tipoVista) {
        List<Cita> resultado = new ArrayList<>();
        LocalDate fechaFin;
        
        if (tipoVista.equalsIgnoreCase("semana")) {
            fechaFin = fechaInicio.plusWeeks(1);
        }
        else {
            fechaFin = fechaInicio.plusMonths(1);
        }

        for (Cita c : citas) {
            if (!c.getFecha().isBefore(fechaInicio) && c.getFecha().isBefore(fechaFin)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public void crearTipoServicio(String descripcion) {
        tiposServicio.add(new TipoServicio(descripcion));
    }

    public boolean borrarTipoServicio(TipoServicio servicio) {
        for (Cita c : citas) {
            if (c.getTipoServicio().equals(servicio)) {
                return false;
            }
        }
        tiposServicio.remove(servicio);
        return true;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public List<TipoServicio> getTiposServicio() {
        return tiposServicio;
    }

    public ListaEspera getListaEspera() {
        return listaEspera;
    }

    public HorarioAtencion getHorarioAtencion() {
        return horarioAtencion;
    }
}