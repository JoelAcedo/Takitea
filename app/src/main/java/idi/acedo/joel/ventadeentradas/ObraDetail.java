package idi.acedo.joel.ventadeentradas;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import idi.acedo.joel.ventadeentradas.BD.CRUDEntradas;
import idi.acedo.joel.ventadeentradas.BD.ContratoEntradas;
import idi.acedo.joel.ventadeentradas.listaobras.ObraTeatre;
import idi.acedo.joel.ventadeentradas.sesions.Sesion;
import idi.acedo.joel.ventadeentradas.sesions.SesionDetailAdapter;

public class ObraDetail extends AppCompatActivity {
    public static final String ID_OBRA_INTENT = "id_obra";

    private static final String ID_OBRA_SAVED = "id_obra_saved";

    CRUDEntradas crudEntradas;

    private String idObra;

    private ImageView cover;
    private TextView title;
    private TextView dates;
    private TextView time;
    private TextView price;
    private TextView description;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Sesion> sesionDataset;

    private ObraTeatre obraTeatre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obra_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        crudEntradas = CRUDEntradas.getInstancia(getApplicationContext());

        Intent intent = getIntent();
        idObra = intent.getStringExtra(ID_OBRA_INTENT);

        if (savedInstanceState != null) {
            String tmpId = savedInstanceState.getString(ID_OBRA_SAVED);
            if (tmpId != null)
                idObra = tmpId;
        }

        cover = (ImageView) findViewById(R.id.cover_detail);
        title = (TextView) findViewById(R.id.text_title_detail);
        dates = (TextView) findViewById(R.id.text_dates_detail);
        time = (TextView) findViewById(R.id.text_time_detail);
        price = (TextView) findViewById(R.id.text_price_detail);
        description = (TextView) findViewById(R.id.text_description_detail);

        obraTeatre = new ObraTeatre();

        sesionDataset = new ArrayList<Sesion>();

        mRecyclerView = (RecyclerView) findViewById(R.id.session_available);
        //mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SesionDetailAdapter(sesionDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        new GetObraByIDAndSesions().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ID_OBRA_SAVED, idObra);

        super.onSaveInstanceState(outState);
    }

    private class GetObraByIDAndSesions extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Cursor cursor = crudEntradas.getObraById(idObra);
            String id_obra = null;

            if (cursor != null && cursor.moveToNext()) {
                id_obra = cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.ID));
                obraTeatre.setId(id_obra);
                obraTeatre.setTitle(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.TITULO)));
                obraTeatre.setDescription(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.DESCRIPCION)));
                obraTeatre.setTime_minutes(cursor.getInt(cursor.getColumnIndex(ContratoEntradas.Obras.DURACION)));
                obraTeatre.setPrice(cursor.getDouble(cursor.getColumnIndex(ContratoEntradas.Obras.PRECIO)));
                obraTeatre.setDates(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.DATES)));

                String img_path = cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.IMG_PATH));
                obraTeatre.setImg_path(img_path);

                File imgFile = new  File(Environment.getExternalStorageDirectory() + "/" + MainActivity.MAIN_FOLDER, img_path);
                if(imgFile.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    obraTeatre.setImage_bm(bitmap);
                }

                cursor.close();
                cursor = null;
            }



            if (id_obra != null) {
                cursor = crudEntradas.getSesionsByObraId(id_obra);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        Sesion sesion = new Sesion();
                        sesion.setId(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Sesiones.ID)));

                        long date_time = cursor.getLong(cursor.getColumnIndex(ContratoEntradas.Sesiones.FECHA));
                        Date date = new Date();
                        date.setTime(date_time);

                        sesion.setDate(date);
                        sesion.setAsientosDisponibles(
                                cursor.getInt(cursor.getColumnIndex(ContratoEntradas.Sesiones.ALIBRES)));
                        sesion.setId_obra(cursor.getString(
                                cursor.getColumnIndex(ContratoEntradas.Sesiones.ID_OBRA)));

                        sesionDataset.add(sesion);
                    }

                    cursor.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            cover.setImageBitmap(obraTeatre.getImage_bm());
            title.setText(obraTeatre.getTitle());
            dates.setText(obraTeatre.getDates());
            time.setText(obraTeatre.getTime());
            price.setText(obraTeatre.getPriceString());
            description.setText(obraTeatre.getDescription());

            mAdapter.notifyDataSetChanged();
        }
    }
}
