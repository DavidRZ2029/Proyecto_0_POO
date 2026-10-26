package logica;

import java.io.Serializable;

public class TipoServicio implements Serializable{
    private String descripcion;

    public TipoServicio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
}
