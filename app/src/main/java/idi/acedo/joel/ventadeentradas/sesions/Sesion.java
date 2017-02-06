package idi.acedo.joel.ventadeentradas.sesions;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joela on 04/06/2016.
 */
public class Sesion {
    public static final int TOTAL_ASIENTOS = 40;

    private String id;
    private Date date;
    private int asientosDisponibles;
    private String asientosVendidos;
    private int entradaNormales;
    private int entradasDescuento;
    private int recaptacion;
    private String id_obra;

    public Sesion(String id, Date date, int asientosDisponibles) {
        this.id = id;
        this.date = date;
        this.asientosDisponibles = asientosDisponibles;
    }

    public Sesion() {
    }

    public String getAsientosVendidos() {
        return asientosVendidos;
    }

    public void setAsientosVendidos(String asientosVendidos) {
        this.asientosVendidos = asientosVendidos;
    }

    public int getEntradaNormales() {
        return entradaNormales;
    }

    public void setEntradaNormales(int entradaNormales) {
        this.entradaNormales = entradaNormales;
    }

    public int getEntradasDescuento() {
        return entradasDescuento;
    }

    public void setEntradasDescuento(int entradasDescuento) {
        this.entradasDescuento = entradasDescuento;
    }

    public int getRecaptacion() {
        return recaptacion;
    }

    public void setRecaptacion(int recaptacion) {
        this.recaptacion = recaptacion;
    }

    public String getId_obra() {
        return id_obra;
    }

    public void setId_obra(String id_obra) {
        this.id_obra = id_obra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAsientosDisponibles() {
        return asientosDisponibles;
    }

    public void setAsientosDisponibles(int asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles;
    }

    public String getDateFormated() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formatedData = simpleDateFormat.format(date);

        return formatedData;
    }

    public String getDayName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String dayName = simpleDateFormat.format(date);

        return dayName;
    }

    public String getHourString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String hour = simpleDateFormat.format(date);

        return hour;
    }
}
