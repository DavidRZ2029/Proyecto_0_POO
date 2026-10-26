package control;

import logica.Barberia;
import guardado.GestorPersistencia;

public class ControladorBarberia {
    private Barberia barberia;

    public ControladorBarberia() {
        this.barberia = GestorPersistencia.cargarDatos();
    }

    public Barberia getBarberia(){
        return barberia;
    }

    public void cerrarSistema(){
        GestorPersistencia.guardarDatos(barberia);
    }
}
