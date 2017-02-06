package idi.acedo.joel.ventadeentradas.BD;

import java.util.UUID;

/**
 * Created by joela on 04/06/2016.
 */
public class ContratoEntradas {

    interface ColumnasObras {
        String ID = "id";
        String TITULO = "titulo";
        String DESCRIPCION = "descripcion";
        String DURACION = "duracion";
        String PRECIO = "precio";
        String DATES = "dates";
        String IMG_PATH = "img_path";
    }

    interface ColumnasSesiones {
        String ID = "id";
        String FECHA = "fecha";
        String ALIBRES = "alibres";
        String AVENDIDOS = "avendidos";
        String ENORMALES = "enormales";
        String EDESCUENTO = "edescuento";
        String RECAPTACION = "recaptacion";
        String ID_OBRA = "id_obra";
    }

    interface ColumnasVentas {
        String ID = "id";
        String NOMBRE = "nombre";
        String ACOMPARDOS = "acomprados";
        String EMMAIL = "email";
        String ID_SESION = "id_sesion";
    }

    public static class Obras implements ColumnasObras {
        public static String generarIdObra() {
            return "O-" + UUID.randomUUID().toString();
        }
    }

    public static class Sesiones implements ColumnasSesiones {
        public static String generarIdSesion() {
            return "S-" + UUID.randomUUID().toString();
        }
    }

    public static class Ventas implements ColumnasVentas {
        public static String generarIdVenta() {
            return "V-" + UUID.randomUUID().toString();
        }
    }

    private ContratoEntradas() {}

}
