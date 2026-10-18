package logica;

public class Cliente {
    private String nombre;
    private String email;
    private String telefono;

    public Cliente(String nombre, String email, String telefono) throws Exception{
       if (validarEmail(email) == false) {
        throw new Exception("Email inválido: " + email);
       }

       if (validarTelefono(telefono) == false) {
        throw new Exception("Telefono inválido: " + telefono);
       }
        
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean validarEmail(String email) {
        if (email == null) {
            return false;
        }

        int posArroba = email.indexOf("@");
        int posPunto = email.lastIndexOf(".");

        if (posArroba < 1) {
            return false;
        }
        if (posArroba != email.lastIndexOf("@")) {
            return false;
        }
        if (posPunto < posArroba + 2 || posPunto == email.length() - 1) {
            return false;
        }

        return true;
    }

    public boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }

        for (int i = 0; i < telefono.length(); i++) {
            char c = telefono.charAt(i);

            boolean esNumero = Character.isDigit(c);
            boolean esPermitido = c == '(' || c == ')' || c == '-';

            if (!esNumero && !esPermitido) {
                return false;
            }
        }

        return true;
    }
}
