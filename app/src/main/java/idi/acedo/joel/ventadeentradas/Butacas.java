package idi.acedo.joel.ventadeentradas;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.StringTokenizer;

import idi.acedo.joel.ventadeentradas.BD.CRUDEntradas;
import idi.acedo.joel.ventadeentradas.BD.ContratoEntradas;
import idi.acedo.joel.ventadeentradas.asientos.Asiento;
import idi.acedo.joel.ventadeentradas.asientos.AsientoAdapter;
import idi.acedo.joel.ventadeentradas.sesions.Sesion;

public class Butacas extends AppCompatActivity implements View.OnClickListener {
    public static final String ID_SESION_INTENT = "id_sesion_intent";
    private static final String ID_SESIO_SAVED = "id_sesio_saved";

    private static final int SIZE_SALA = 40;
    public static final String EURO = "€";

    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Asiento> asientoDataset;

    private String idSesion;
    private String idObra;

    private int eNormales;
    private int eReducido;
    private double total;
    private int numSeleccionados;
    private double normalPrice;
    private double reduitPrice;

    private Button addNormal;
    private Button minusNormal;
    private Button addReducido;
    private Button minusReducido;
    private Button comprar;

    private TextView precioNormal;
    private TextView precioReducido;
    private TextView eNormales_view;
    private TextView eReducido_view;
    private TextView total_view;

    CRUDEntradas crudEntradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butacas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        crudEntradas = CRUDEntradas.getInstancia(getApplicationContext());

        Intent intent = getIntent();
        idSesion = intent.getStringExtra(ID_SESION_INTENT);

        if (savedInstanceState != null) {
            String tmpId = savedInstanceState.getString(ID_SESIO_SAVED);
            if (tmpId != null)
                idSesion = tmpId;
        }

        asientoDataset = new ArrayList<Asiento>();
        eNormales = 0;
        eReducido = 0;
        total = 0.0;
        numSeleccionados = 0;
        normalPrice = 10.0;
        reduitPrice = 7.55;

        addNormal = (Button) findViewById(R.id.add_normal);
        minusNormal = (Button) findViewById(R.id.minus_normal);
        addReducido = (Button) findViewById(R.id.add_reduit);
        minusReducido = (Button) findViewById(R.id.minus_reduit);
        comprar = (Button) findViewById(R.id.butaques_buy);

        addNormal.setOnClickListener(this);
        minusNormal.setOnClickListener(this);
        addReducido.setOnClickListener(this);
        minusReducido.setOnClickListener(this);
        comprar.setOnClickListener(this);

        precioNormal = (TextView) findViewById(R.id.normal_price);
        precioReducido = (TextView) findViewById(R.id.reduida_price);
        eNormales_view = (TextView) findViewById(R.id.normal_count_rels);
        eReducido_view = (TextView) findViewById(R.id.reduida_count_rels);
        total_view = (TextView) findViewById(R.id.total_count_rels);

        for (int i = SIZE_SALA; i > 0; i--) {
            Asiento tmpAsiento = new Asiento(i, Asiento.ASIENTO_LIBRE);
            asientoDataset.add(tmpAsiento);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.pati_butaques);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 8);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AsientoAdapter(asientoDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        new GetAsientos().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ID_SESIO_SAVED, idSesion);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

        numSeleccionados = ((AsientoAdapter)mAdapter).getSizeAsientosSeleccionados();

        int id = v.getId();
        if (id == R.id.add_normal) {

            if (numSeleccionados > (eNormales + eReducido)) {
                eNormales++;
                eNormales_view.setText(String.valueOf(eNormales));
                calulateTotal();
            }

        } else if (id == R.id.minus_normal) {

            if (eNormales > 0) {
                eNormales--;
                eNormales_view.setText(String.valueOf(eNormales));
                calulateTotal();
            }

        } else if (id == R.id.add_reduit) {

            if (numSeleccionados > (eNormales + eReducido)) {
                eReducido++;
                eReducido_view.setText(String.valueOf(eReducido));
                calulateTotal();
            }

        } else if (id == R.id.minus_reduit) {

            if (eReducido > 0) {
                eReducido--;
                eReducido_view.setText(String.valueOf(eReducido));
                calulateTotal();
            }

        } else if (id == R.id.butaques_buy) {

            if (numSeleccionados == 0)
                Toast.makeText(this, getString(R.string.error_cero), Toast.LENGTH_LONG).show();
            else if (numSeleccionados != (eNormales + eReducido))
                Toast.makeText(this, getString(R.string.error_butacas), Toast.LENGTH_LONG).show();
            else
                confirmBuy();

        }

    }

    private void confirmBuy() {
        ArrayList<Asiento> asientosSelec = ((AsientoAdapter)mAdapter).asientosSeleccionados();

        String asientosString = new String();

        for (int i = 0; i < asientosSelec.size(); i++) {
            if (i == 0) {
                asientosString = asientosString + asientosSelec.get(i).getIdString();
            } else {
                asientosString = asientosString + ":" + asientosSelec.get(i).getIdString();
            }
        }

        Intent intent = new Intent(this, ConfirmActivity.class);
        intent.putExtra(ConfirmActivity.ID_SESION_INTENT, idSesion);
        intent.putExtra(ConfirmActivity.ASIENTOS_INTENT, asientosString);
        intent.putExtra(ConfirmActivity.ID_OBRA_INTENT, idObra);
        intent.putExtra(ConfirmActivity.TOTAL_INTENT, total);
        startActivity(intent);
    }


    private void calulateTotal() {
        total = (normalPrice * (double)eNormales) + (reduitPrice * (double)eReducido);
        total_view.setText(String.valueOf(total) + " " + EURO);
    }


    private class GetAsientos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Cursor cursor = crudEntradas.getSesioById(idSesion);

            if (cursor != null && cursor.moveToNext()) {

                idObra = cursor.getString(cursor.getColumnIndex(ContratoEntradas.Sesiones.ID_OBRA));
                String asientosOcupados = cursor.getString(cursor.getColumnIndex(ContratoEntradas.Sesiones.AVENDIDOS));

                if (asientosOcupados != "") {
                    StringTokenizer stringTokenizer = new StringTokenizer(asientosOcupados, ":");
                    while (stringTokenizer.hasMoreElements()) {

                        String ocupadoAsiento = stringTokenizer.nextToken();
                        asientoDataset.get(SIZE_SALA - Integer.valueOf(ocupadoAsiento)).setEstado(Asiento.ASIENTO_OCUPADO);
                    }
                }

                cursor.close();
            }

            cursor = crudEntradas.getObraById(idObra);

            if (cursor != null && cursor.moveToNext()) {

                normalPrice = cursor.getDouble(cursor.getColumnIndex(ContratoEntradas.Obras.PRECIO));
                reduitPrice = normalPrice * 0.7;

                cursor.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            precioNormal.setText(String.valueOf(normalPrice) + " " + EURO);
            precioReducido.setText(String.valueOf(reduitPrice) + " " + EURO);

            mAdapter.notifyDataSetChanged();
        }
    }

}
