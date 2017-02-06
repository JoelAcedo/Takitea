package idi.acedo.joel.ventadeentradas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import idi.acedo.joel.ventadeentradas.BD.CRUDEntradas;
import idi.acedo.joel.ventadeentradas.BD.ContratoEntradas;
import idi.acedo.joel.ventadeentradas.listaobras.ObraTeatre;
import idi.acedo.joel.ventadeentradas.listaobras.ObraTeatreAdapter;

public class MainActivity extends AppCompatActivity {
        //implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "MainActivity";
    public static final String MAIN_FOLDER = "takitea";
    public static final String CONFIRM_BUY = "confirm_buy";
    public static final String CONFIRM_ADD = "confirm_add";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ObraTeatre> obresDataset;

    private CoordinatorLayout coordinatorLayout;

    CRUDEntradas crudEntradas;

    //private DatabaseChanged databaseChanged = new DatabaseChanged();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        setTitle(getString(R.string.title_activity_main));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //new IntentFilter().addAction(DATABAS);
        //this.registerReceiver(databaseChanged, );

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        //toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        checkFileSystem();

        obresDataset = new ArrayList<ObraTeatre>();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinator_snack_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.obras_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ObraTeatreAdapter(obresDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        crudEntradas = CRUDEntradas.getInstancia(getApplicationContext());

        Intent intent = getIntent();
        if (intent.hasExtra(CONFIRM_BUY))
            Snackbar.make(coordinatorLayout, getApplicationContext().getString(R.string.confrim_correct),
                    Snackbar.LENGTH_SHORT).show();
        else if (intent.hasExtra(CONFIRM_ADD))
            Snackbar.make(coordinatorLayout, getApplicationContext().getString(R.string.correct_add),
                    Snackbar.LENGTH_SHORT).show();

        new GetObras().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetObras().execute();
    }


    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, SobreTakitea.class);
            startActivity(intent);
        } else if (id == R.id.action_addObra) {
            Intent intent = new Intent(this, NewObraActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkFileSystem() {
        boolean result = Utility.checkPermission(MainActivity.this, Utility.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        if (result) {
            File f = new File(Environment.getExternalStorageDirectory(), MAIN_FOLDER);

            if (!f.exists()) {
                f.mkdirs();
                new inicializarFS().execute();
            }
        }
    }

    // -------------------------- Inicializacion del sistema de ficheros ---------------//

    public class inicializarFS extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Bitmap img1_bm = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.romeo_y_julieta_m);
            Bitmap img2_bm = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.la_divina_comedia_m);
            Bitmap img3_bm = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.la_celestina);

            File img1 = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER, "pre1.jpg");
            File img2 = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER, "pre2.jpg");
            File img3 = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER, "pre3.jpg");
            FileOutputStream fo1;
            FileOutputStream fo2;
            FileOutputStream fo3;

            try {
                img1.createNewFile();
                img2.createNewFile();
                img3.createNewFile();

                fo1 = new FileOutputStream(img1);
                fo2 = new FileOutputStream(img2);
                fo3 = new FileOutputStream(img3);

                img1_bm.compress(Bitmap.CompressFormat.JPEG, 20, fo1);
                img2_bm.compress(Bitmap.CompressFormat.JPEG, 20, fo2);
                img3_bm.compress(Bitmap.CompressFormat.JPEG, 20, fo3);
                //fo.write(bytes.toByteArray());
                fo1.close();
                fo2.close();
                fo3.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    // -------------------------- Base de Datos -------------------//

    private class GetObras extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Cursor cursor = crudEntradas.getObras();

            if (cursor != null) {
                if(cursor.getCount() != mAdapter.getItemCount()) {
                    obresDataset.removeAll(obresDataset);

                    while (cursor.moveToNext()) {
                        ObraTeatre obraTeatre = new ObraTeatre();
                        obraTeatre.setId(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.ID)));
                        obraTeatre.setTitle(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.TITULO)));
                        obraTeatre.setDescription(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.DESCRIPCION)));
                        obraTeatre.setTime_minutes(cursor.getInt(cursor.getColumnIndex(ContratoEntradas.Obras.DURACION)));
                        obraTeatre.setPrice(cursor.getDouble(cursor.getColumnIndex(ContratoEntradas.Obras.PRECIO)));
                        obraTeatre.setDates(cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.DATES)));

                        String img_path = cursor.getString(cursor.getColumnIndex(ContratoEntradas.Obras.IMG_PATH));
                        obraTeatre.setImg_path(img_path);

                        File imgFile = new File(Environment.getExternalStorageDirectory() + "/" + MAIN_FOLDER, img_path);
                        if (imgFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            obraTeatre.setImage_bm(bitmap);
                        }

                        obresDataset.add(obraTeatre);
                    }

                    cursor.close();
                }
            } else
                Log.d(LOG_TAG, "Error, cursor es null");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
            /*Toast.makeText(getBaseContext(),
                    obresDataset.get(0).getImg_path(),
                    Toast.LENGTH_LONG).show();*/
        }
    }

    public class DatabaseChanged extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    public void BorrarObraById(String id) {
        new BorrarObra().execute(id);
    }

    public class BorrarObra extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String id = params[0];
            crudEntradas.deleteObra(id);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
