package guardado;

import logica.Barberia;
import java.io.*;

public class GestorPersistencia {
    
    private static final String NOMBRE_ARCHIVO = "datos_barberia.dat";

    public static void guardarDatos(Barberia barberia) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO))) {
            oos.writeObject(barberia);
            System.out.println("Datos guardados exitosamente en disco.");
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public static Barberia cargarDatos() {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) {
            return new Barberia();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO))) {
            Barberia barberiaCargada = (Barberia) ois.readObject();
            System.out.println("Datos cargados exitosamente desde el disco.");
            return barberiaCargada;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos, se iniciará con un sistema vacío: " + e.getMessage());
            return new Barberia();
        }
    }
}