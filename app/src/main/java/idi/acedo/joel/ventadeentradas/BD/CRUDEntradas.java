package idi.acedo.joel.ventadeentradas.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import idi.acedo.joel.ventadeentradas.listaobras.ObraTeatre;
import idi.acedo.joel.ventadeentradas.sesions.Sesion;
import idi.acedo.joel.ventadeentradas.ventas.Venta;

/**
 * Created by joela on 05/06/2016.
 */
public class CRUDEntradas {

    private static HelperEntradas helperEntradas;

    private static CRUDEntradas instancia = new CRUDEntradas();

    private CRUDEntradas() {}

    public static CRUDEntradas getInstancia(Context context) {
        if (helperEntradas == null)
            helperEntradas = new HelperEntradas(context);

        return instancia;
    }

    //private static final


    //----------------- OBRAS ---------------------------------------//

    public Cursor getObras() {
        SQLiteDatabase db = helperEntradas.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", HelperEntradas.Tablas.OBRAS);
        return db.rawQuery(sql, null);
    }

    public Cursor getObraById(String id) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        String selection = String.format("%s=?", ContratoEntradas.Obras.ID);
        String[] selectionArgs = {id};

        //SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String[] projection = {
                ContratoEntradas.Obras.ID,
                ContratoEntradas.Obras.TITULO,
                ContratoEntradas.Obras.DESCRIPCION,
                ContratoEntradas.Obras.DURACION,
                ContratoEntradas.Obras.PRECIO,
                ContratoEntradas.Obras.DATES,
                ContratoEntradas.Obras.IMG_PATH
        };

        return db.query(HelperEntradas.Tablas.OBRAS, projection, selection, selectionArgs, null, null, null);
    }

    public String insertObra(ObraTeatre obra) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        String idObra = ContratoEntradas.Obras.generarIdObra();

        contentValues.put(ContratoEntradas.Obras.ID, idObra);
        contentValues.put(ContratoEntradas.Obras.TITULO, obra.getTitle());
        contentValues.put(ContratoEntradas.Obras.DESCRIPCION, obra.getDescription());
        contentValues.put(ContratoEntradas.Obras.DURACION, obra.getTime_minutes());
        contentValues.put(ContratoEntradas.Obras.PRECIO, obra.getPrice());
        contentValues.put(ContratoEntradas.Obras.DATES, obra.getDates());
        contentValues.put(ContratoEntradas.Obras.IMG_PATH, obra.getImg_path());

        db.insertOrThrow(HelperEntradas.Tablas.OBRAS, null, contentValues);

        return idObra;
    }

    public boolean updateObra(ObraTeatre obra) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContratoEntradas.Obras.TITULO, obra.getTitle());
        contentValues.put(ContratoEntradas.Obras.DESCRIPCION, obra.getDescription());
        contentValues.put(ContratoEntradas.Obras.DURACION, obra.getTime_minutes());
        contentValues.put(ContratoEntradas.Obras.PRECIO, obra.getPrice());
        contentValues.put(ContratoEntradas.Obras.IMG_PATH, obra.getImg_path());

        String whereClause = String.format("%s=?", ContratoEntradas.Obras.ID);
        String[] whereArgs = {obra.getId()};

        int res = db.update(HelperEntradas.Tablas.OBRAS, contentValues, whereClause, whereArgs);

        return res > 0;
    }

    public boolean deleteObra(String id) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        String whereClause = String.format("%s=?", ContratoEntradas.Obras.ID);
        String[] whereArgs = {id};

        int res = db.delete(HelperEntradas.Tablas.OBRAS, whereClause, whereArgs);

        return res > 0;
    }

    // ------------------------ Sesions ----------------------------------//
    public Cursor getSesionsByObraId(String idObra) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        String selection = String.format("%s=?", ContratoEntradas.Sesiones.ID_OBRA);
        String[] selectionArgs = {idObra};

        //SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String[] projection = {
                ContratoEntradas.Sesiones.ID,
                ContratoEntradas.Sesiones.FECHA,
                ContratoEntradas.Sesiones.ALIBRES,
                ContratoEntradas.Sesiones.ID_OBRA
        };

        return db.query(HelperEntradas.Tablas.SESIONES, projection, selection, selectionArgs, null, null, ContratoEntradas.Sesiones.FECHA +" ASC");
    }

    public Cursor getSesioById(String id) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        String selection = String.format("%s=?", ContratoEntradas.Sesiones.ID);
        String[] selectionArgs = {id};

        //SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String[] projection = {
                ContratoEntradas.Sesiones.ID,
                ContratoEntradas.Sesiones.FECHA,
                ContratoEntradas.Sesiones.ALIBRES,
                ContratoEntradas.Sesiones.AVENDIDOS,
                ContratoEntradas.Sesiones.ENORMALES,
                ContratoEntradas.Sesiones.EDESCUENTO,
                ContratoEntradas.Sesiones.RECAPTACION,
                ContratoEntradas.Sesiones.ID_OBRA
        };

        return db.query(HelperEntradas.Tablas.SESIONES, projection, selection, selectionArgs, null, null, null);
    }

    public String insertSesio(Sesion sesion) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        String idSesio = ContratoEntradas.Sesiones.generarIdSesion();

        contentValues.put(ContratoEntradas.Sesiones.ID, idSesio);
        contentValues.put(ContratoEntradas.Sesiones.FECHA, sesion.getDate().getTime());
        contentValues.put(ContratoEntradas.Sesiones.ALIBRES, sesion.getAsientosDisponibles());
        contentValues.put(ContratoEntradas.Sesiones.AVENDIDOS, sesion.getAsientosVendidos());
        contentValues.put(ContratoEntradas.Sesiones.ENORMALES, sesion.getEntradaNormales());
        contentValues.put(ContratoEntradas.Sesiones.EDESCUENTO, sesion.getEntradasDescuento());
        contentValues.put(ContratoEntradas.Sesiones.RECAPTACION, sesion.getRecaptacion());
        contentValues.put(ContratoEntradas.Sesiones.ID_OBRA, sesion.getId_obra());

        db.insertOrThrow(HelperEntradas.Tablas.SESIONES, null, contentValues);

        return idSesio;
    }

    public boolean updateSesio(Sesion sesion) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContratoEntradas.Sesiones.FECHA, sesion.getDate().getTime());
        contentValues.put(ContratoEntradas.Sesiones.ALIBRES, sesion.getAsientosDisponibles());
        contentValues.put(ContratoEntradas.Sesiones.AVENDIDOS, sesion.getAsientosVendidos());
        contentValues.put(ContratoEntradas.Sesiones.ENORMALES, sesion.getEntradaNormales());
        contentValues.put(ContratoEntradas.Sesiones.EDESCUENTO, sesion.getEntradasDescuento());
        contentValues.put(ContratoEntradas.Sesiones.RECAPTACION, sesion.getRecaptacion());
        contentValues.put(ContratoEntradas.Sesiones.ID_OBRA, sesion.getId_obra());

        String whereClause = String.format("%s=?", ContratoEntradas.Sesiones.ID);
        String[] whereArgs = {sesion.getId()};

        int res = db.update(HelperEntradas.Tablas.SESIONES, contentValues, whereClause, whereArgs);

        return res > 0;
    }

    public boolean updateSeientsFromSesion(String id, String seients) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContratoEntradas.Sesiones.AVENDIDOS, seients);

        String whereClause = String.format("%s=?", ContratoEntradas.Sesiones.ID);
        String[] whereArgs = {id};

        int res = db.update(HelperEntradas.Tablas.SESIONES, contentValues, whereClause, whereArgs);

        return res > 0;
    }

    public boolean deleteSesio(String id) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        String whereClause = String.format("%s=?", ContratoEntradas.Sesiones.ID);
        String[] whereArgs = {id};

        int res = db.delete(HelperEntradas.Tablas.SESIONES, whereClause, whereArgs);

        return res > 0;
    }

    // ----------------------- Ventas ---------------------------//

    public Cursor getVentaBySesionId(String idSesion) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        String selection = String.format("%s=?", ContratoEntradas.Ventas.ID_SESION);
        String[] selectionArgs = {idSesion};

        String[] projection = {
                ContratoEntradas.Ventas.ID,
                ContratoEntradas.Ventas.NOMBRE,
                ContratoEntradas.Ventas.ACOMPARDOS,
                ContratoEntradas.Ventas.EMMAIL,
                ContratoEntradas.Ventas.ID_SESION
        };

        return db.query(HelperEntradas.Tablas.VENTAS, projection, selection, selectionArgs, null, null, null);
    }

    public String insertVenta(Venta venta) {
        SQLiteDatabase db = helperEntradas.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        String idVenta = ContratoEntradas.Ventas.generarIdVenta();

        contentValues.put(ContratoEntradas.Ventas.ID, idVenta);
        contentValues.put(ContratoEntradas.Ventas.NOMBRE, venta.getName());
        contentValues.put(ContratoEntradas.Ventas.ACOMPARDOS, venta.getAsientos());
        contentValues.put(ContratoEntradas.Ventas.EMMAIL, venta.getEmail());
        contentValues.put(ContratoEntradas.Ventas.ID_SESION, venta.getId_sesion());

        db.insertOrThrow(HelperEntradas.Tablas.VENTAS, null, contentValues);

        return idVenta;
    }

}
