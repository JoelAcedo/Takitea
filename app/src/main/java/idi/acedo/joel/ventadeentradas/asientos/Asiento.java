package idi.acedo.joel.ventadeentradas.asientos;

/**
 * Created by joela on 06/06/2016.
 */
public class Asiento {

    public static final int ASIENTO_LIBRE = 0;
    public static final int ASIENTO_OCUPADO = 1;
    public static final int ASIENTO_SELEC = 2;

    private int id_asiento;
    private int estado;

    public Asiento() {
    }

    public Asiento(int id_asiento, int estado) {
        this.id_asiento = id_asiento;
        this.estado = estado;
    }

    public int getId_asiento() {
        return id_asiento;
    }

    public String getIdString() {
        return String.valueOf(id_asiento);
    }

    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
