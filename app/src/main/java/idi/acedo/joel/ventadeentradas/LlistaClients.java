package idi.acedo.joel.ventadeentradas;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import idi.acedo.joel.ventadeentradas.BD.CRUDEntradas;
import idi.acedo.joel.ventadeentradas.BD.ContratoEntradas;
import idi.acedo.joel.ventadeentradas.R;
import idi.acedo.joel.ventadeentradas.ventas.Venta;
import idi.acedo.joel.ventadeentradas.ventas.VentaAdapter;

public class LlistaClients extends AppCompatActivity {
    public static final String ID_SESION_INTENT = "id_sesion_intent";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Venta> ventasDataset;

    private String idSesio;

    CRUDEntradas crudEntradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        crudEntradas = CRUDEntradas.getInstancia(getApplicationContext());

        Intent intent = getIntent();
        idSesio = intent.getStringExtra(ID_SESION_INTENT);

        if (savedInstanceState != null) {
            String tmpId = savedInstanceState.getString(ID_SESION_INTENT);
            if (tmpId != null)
                idSesio = tmpId;
        }

        ventasDataset = new ArrayList<Venta>();

        mRecyclerView = (RecyclerView) findViewById(R.id.lista_ventas);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new VentaAdapter(ventasDataset);
        mRecyclerView.setAdapter(mAdapter);

        new GetVentes().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ID_SESION_INTENT, idSesio);

        super.onSaveInstanceState(outState);
    }

    private class GetVentes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Cursor cursor = crudEntradas.getVentaBySesionId(idSesio);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    Venta venta = new Venta();
                    venta.setId(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Ventas.ID)));
                    venta.setName(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Ventas.NOMBRE)));
                    venta.setAsientos(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Ventas.ACOMPARDOS)));
                    venta.setEmail(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Ventas.EMMAIL)));
                    venta.setId_sesion(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Ventas.ID_SESION)));

                    ventasDataset.add(venta);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter.notifyDataSetChanged();
        }
    }
}
