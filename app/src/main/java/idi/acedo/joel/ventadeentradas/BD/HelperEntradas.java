package idi.acedo.joel.ventadeentradas.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import java.util.Calendar;

import idi.acedo.joel.ventadeentradas.ventas.Venta;

public class HelperEntradas extends SQLiteOpenHelper{

    private static final String NOMBRE_BD = "entradas.db";

    private static final int VERSION = 5;

    private final Context context;

    interface Tablas {
        String OBRAS = "obras";
        String SESIONES = "sesiones";
        String VENTAS = "ventas";
    }

    interface Referencias {

        //ON DELETE CASCADE

        String ID_OBRA = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.OBRAS, ContratoEntradas.Obras.ID);

        String ID_SESION = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.SESIONES, ContratoEntradas.Sesiones.ID);

    }

    public HelperEntradas(Context context) {
        super(context, NOMBRE_BD, null, VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT UNIQUE NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s INTEGER NOT NULL," +
                            "%s REAL NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL)",
                Tablas.OBRAS, BaseColumns._ID,
                ContratoEntradas.Obras.ID, ContratoEntradas.Obras.TITULO,
                ContratoEntradas.Obras.DESCRIPCION, ContratoEntradas.Obras.DURACION,
                ContratoEntradas.Obras.PRECIO,
                ContratoEntradas.Obras.DATES, ContratoEntradas.Obras.IMG_PATH));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT UNIQUE NOT NULL," +
                            "%s INTEGER NOT NULL," +
                            "%s INTEGER NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s INTEGER NOT NULL," +
                            "%s INTEGER NOT NULL," +
                            "%s REAL NOT NULL," +
                            "%s TEXT NOT NULL %s)",
                Tablas.SESIONES, BaseColumns._ID,
                ContratoEntradas.Sesiones.ID, ContratoEntradas.Sesiones.FECHA,
                ContratoEntradas.Sesiones.ALIBRES, ContratoEntradas.Sesiones.AVENDIDOS,
                ContratoEntradas.Sesiones.ENORMALES,
                ContratoEntradas.Sesiones.EDESCUENTO, ContratoEntradas.Sesiones.RECAPTACION,
                ContratoEntradas.Sesiones.ID_OBRA, Referencias.ID_OBRA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s TEXT UNIQUE NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL," +
                            "%s TEXT NOT NULL %s)",
                Tablas.VENTAS, BaseColumns._ID,
                ContratoEntradas.Ventas.ID, ContratoEntradas.Ventas.NOMBRE,
                ContratoEntradas.Ventas.ACOMPARDOS, ContratoEntradas.Ventas.EMMAIL,
                ContratoEntradas.Ventas.ID_SESION, Referencias.ID_SESION));

        insertDefaultValues(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.OBRAS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.SESIONES);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.VENTAS);

        onCreate(db);
    }

    private void insertDefaultValues(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        String[] names = { "Joel Acedo Delgado", "Joan García", "Anna Rodríguez"};
        String[] email = { "joel.acedo@est.fib.upc.edu", "joan@gmail.com", "anna_rodriguez@outlook.com"};
        String[] acomprado = { "10:11", "1:2:3", "22:33"};

        String idObra = ContratoEntradas.Obras.generarIdObra();

        values.put(ContratoEntradas.Obras.ID, idObra);
        values.put(ContratoEntradas.Obras.TITULO, "Romeu i Julieta");
        values.put(ContratoEntradas.Obras.DESCRIPCION, "Conta la història dels amors de Romeu i Julieta, dos joves que pertanyen a famílies enfrontades, els Montagut i Capulet, i que han esdevingut arquetips literaris, un dels màxims exemples de parelles desgraciades.");
        values.put(ContratoEntradas.Obras.DURACION, 85);
        values.put(ContratoEntradas.Obras.PRECIO, 15);
        values.put(ContratoEntradas.Obras.DATES, "05/06/2016 - 10/06/2016");
        values.put(ContratoEntradas.Obras.IMG_PATH, "pre1.jpg");

        db.insertOrThrow(HelperEntradas.Tablas.OBRAS, null, values);

        for (int i = 0; i <= 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, 5, i+5, 19, 30);

            values = new ContentValues();
            String idSesio = ContratoEntradas.Sesiones.generarIdSesion();
            values.put(ContratoEntradas.Sesiones.ID, idSesio);
            values.put(ContratoEntradas.Sesiones.FECHA, calendar.getTimeInMillis());
            values.put(ContratoEntradas.Sesiones.ALIBRES, 40);
            values.put(ContratoEntradas.Sesiones.AVENDIDOS, "10:11:12:22:23:1:2:3");
            values.put(ContratoEntradas.Sesiones.ENORMALES, 0);
            values.put(ContratoEntradas.Sesiones.EDESCUENTO, 0);
            values.put(ContratoEntradas.Sesiones.RECAPTACION, 0);
            values.put(ContratoEntradas.Sesiones.ID_OBRA, idObra);

            db.insertOrThrow(Tablas.SESIONES, null, values);

            for (int j = 0; j < 3; j++) {

                values = new ContentValues();
                String idVenta = ContratoEntradas.Ventas.generarIdVenta();
                values.put(ContratoEntradas.Ventas.ID, idVenta);
                values.put(ContratoEntradas.Ventas.NOMBRE, names[j]);
                values.put(ContratoEntradas.Ventas.ACOMPARDOS, acomprado[j]);
                values.put(ContratoEntradas.Ventas.EMMAIL, email[j]);
                values.put(ContratoEntradas.Ventas.ID_SESION, idSesio);

                db.insertOrThrow(Tablas.VENTAS, null, values);
            }
        }



        values = new ContentValues();
        idObra = ContratoEntradas.Obras.generarIdObra();

        values.put(ContratoEntradas.Obras.ID, idObra);
        values.put(ContratoEntradas.Obras.TITULO, "La Divina Comèdia");
        values.put(ContratoEntradas.Obras.DESCRIPCION, "L'obra narra el pelegrinatge de l'autor pels tres mons després de perdre's en un bosc. Durant el seu viatge a través de l'infern i el purgatori, el poeta Virgili és el guia del Dant. Com que per la seva condició de pagà l'entrada al paradís li és vetada, en el tercer llibre el poeta viatja acompanyat de Beatriu, una jove que Dant havia estimat en la seva joventut.");
        values.put(ContratoEntradas.Obras.DURACION, 110);
        values.put(ContratoEntradas.Obras.PRECIO, 19.85);
        values.put(ContratoEntradas.Obras.DATES, "13/12/2016 - 15/01/2017");
        values.put(ContratoEntradas.Obras.IMG_PATH, "pre2.jpg");

        db.insertOrThrow(HelperEntradas.Tablas.OBRAS, null, values);

        for (int i = 0; i <= 4; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, 5, i+20, 20, 15);

            values = new ContentValues();
            String idSesio = ContratoEntradas.Sesiones.generarIdSesion();
            values.put(ContratoEntradas.Sesiones.ID, idSesio);
            values.put(ContratoEntradas.Sesiones.FECHA, calendar.getTimeInMillis());
            values.put(ContratoEntradas.Sesiones.ALIBRES, 40);
            values.put(ContratoEntradas.Sesiones.AVENDIDOS, "10:11:12:22:23:1:2:3");
            values.put(ContratoEntradas.Sesiones.ENORMALES, 0);
            values.put(ContratoEntradas.Sesiones.EDESCUENTO, 0);
            values.put(ContratoEntradas.Sesiones.RECAPTACION, 0);
            values.put(ContratoEntradas.Sesiones.ID_OBRA, idObra);

            db.insertOrThrow(Tablas.SESIONES, null, values);

            for (int j = 0; j < 3; j++) {

                values = new ContentValues();
                String idVenta = ContratoEntradas.Ventas.generarIdVenta();
                values.put(ContratoEntradas.Ventas.ID, idVenta);
                values.put(ContratoEntradas.Ventas.NOMBRE, names[j]);
                values.put(ContratoEntradas.Ventas.ACOMPARDOS, acomprado[j]);
                values.put(ContratoEntradas.Ventas.EMMAIL, email[j]);
                values.put(ContratoEntradas.Ventas.ID_SESION, idSesio);

                db.insertOrThrow(Tablas.VENTAS, null, values);
            }
        }

        values = new ContentValues();
        idObra = ContratoEntradas.Obras.generarIdObra();

        values.put(ContratoEntradas.Obras.ID, idObra);
        values.put(ContratoEntradas.Obras.TITULO, "La Celestina");
        values.put(ContratoEntradas.Obras.DESCRIPCION, "La Celestina és el nom amb què s\'ha popularitzat la Tragicomedia de Calisto y Melibea una de les obres més importants de la literatura castellana, atribuïda a Fernando de Rojas.");
        values.put(ContratoEntradas.Obras.DURACION, 60);
        values.put(ContratoEntradas.Obras.PRECIO, 10.00);
        values.put(ContratoEntradas.Obras.DATES, "03/02/2017 - 19/02/2017");
        values.put(ContratoEntradas.Obras.IMG_PATH, "pre3.jpg");

        db.insertOrThrow(HelperEntradas.Tablas.OBRAS, null, values);

        for (int i = 0; i < 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2017, 2, (i*4)+3, 18, 45);

            values = new ContentValues();
            String idSesio = ContratoEntradas.Sesiones.generarIdSesion();
            values.put(ContratoEntradas.Sesiones.ID, idSesio);
            values.put(ContratoEntradas.Sesiones.FECHA, calendar.getTimeInMillis());
            values.put(ContratoEntradas.Sesiones.ALIBRES, 40);
            values.put(ContratoEntradas.Sesiones.AVENDIDOS, "10:11:12:22:23:1:2:3");
            values.put(ContratoEntradas.Sesiones.ENORMALES, 0);
            values.put(ContratoEntradas.Sesiones.EDESCUENTO, 0);
            values.put(ContratoEntradas.Sesiones.RECAPTACION, 0);
            values.put(ContratoEntradas.Sesiones.ID_OBRA, idObra);

            db.insertOrThrow(Tablas.SESIONES, null, values);

            for (int j = 0; j < 3; j++) {

                values = new ContentValues();
                String idVenta = ContratoEntradas.Ventas.generarIdVenta();
                values.put(ContratoEntradas.Ventas.ID, idVenta);
                values.put(ContratoEntradas.Ventas.NOMBRE, names[j]);
                values.put(ContratoEntradas.Ventas.ACOMPARDOS, acomprado[j]);
                values.put(ContratoEntradas.Ventas.EMMAIL, email[j]);
                values.put(ContratoEntradas.Ventas.ID_SESION, idSesio);

                db.insertOrThrow(Tablas.VENTAS, null, values);
            }
        }
    }
}
