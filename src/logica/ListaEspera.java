package logica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaEspera implements Serializable{
    private List<Cliente> clientesEspera;

    public ListaEspera() {
        this.clientesEspera = new ArrayList<>();
    }

    public boolean agregarCliente(Cliente cliente) {
        if (cliente != null && !clientesEspera.contains(cliente)) {
            clientesEspera.add(cliente);
            return true;
        }
        return false;
    }

    public void borrarCliente(Cliente cliente) {
        this.clientesEspera.remove(cliente);
    }

    public List<Cliente> obtenerListaEspera() {
        return this.clientesEspera;
    }
}
