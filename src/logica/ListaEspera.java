package logica;

import java.util.ArrayList;
import java.util.List;

public class ListaEspera {
    private List<Cliente> clientesEspera;

    public ListaEspera() {
        this.clientesEspera = new ArrayList<>();
    }

    public void agregarCliente(Cliente cliente){
        this.clientesEspera.add(cliente);
    }

    public void borrarCliente(Cliente cliente) {
        this.clientesEspera.remove(cliente);
    }

    public List<Cliente> obtenerListaEspera() {
        return this.clientesEspera;
    }
}
