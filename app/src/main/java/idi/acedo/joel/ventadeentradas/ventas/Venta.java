package idi.acedo.joel.ventadeentradas.ventas;

/**
 * Created by joela on 06/06/2016.
 */
public class Venta {
    private String id;
    private String name;
    private String asientos;
    private String email;
    private String id_sesion;

    public Venta(String id, String name, String asientos, String email, String id_sesion) {
        this.id = id;
        this.name = name;
        this.asientos = asientos;
        this.email = email;
        this.id_sesion = id_sesion;
    }

    public Venta() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsientos() {
        return asientos;
    }

    public String getAsientosFormated() {
        return asientos.replace(':', ',');
    }

    public void setAsientos(String asientos) {
        this.asientos = asientos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId_sesion() {
        return id_sesion;
    }

    public void setId_sesion(String id_sesion) {
        this.id_sesion = id_sesion;
    }
}
