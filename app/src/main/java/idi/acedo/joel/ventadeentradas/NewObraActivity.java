package idi.acedo.joel.ventadeentradas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import idi.acedo.joel.ventadeentradas.BD.CRUDEntradas;
import idi.acedo.joel.ventadeentradas.listaobras.ObraTeatre;
import idi.acedo.joel.ventadeentradas.sesions.Sesion;
import idi.acedo.joel.ventadeentradas.sesions.SesionAdapter;

public class NewObraActivity extends AppCompatActivity implements View.OnClickListener {

    //Bundle tags
    private static final String SAVE_TITLE = "SAVE_TITLE";
    private static final String SAVE_DESCR = "SAVE_DESCR";
    private static final String SAVE_TIME = "SAVE_TIME";
    private static final String SAVE_PRECIO = "SAVE_PRECIO";
    private static final String SAVE_BITMAP = "SAVE_BITMAP";

    private CoordinatorLayout coordinatorLayout;
    private Button select_image_galery;
    private Button select_image_camera;
    private Button add_session;
    private Button confirm;
    private ImageView preview;
    private TextInputLayout title;
    private TextInputLayout description;
    private TextInputLayout time;
    private TextInputLayout price;
    private TextView errorSesion;

    private Bitmap preview_bitmap;

    private static int REQUEST_GALERY = 1;
    private static int REQUEST_CAMERA = 2;

    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Sesion> sesionDataset;

    private int pickedYear;
    private int pickedMonth;
    private int pickedDay;
    private int pickedHour;
    private int pickedMinute;

    CRUDEntradas crudEntradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_obra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinator_snack);

        select_image_galery = (Button) findViewById(R.id.select_image_galery);
        select_image_camera = (Button) findViewById(R.id.select_image_camera);
        add_session = (Button) findViewById(R.id.add_session);
        confirm = (Button) findViewById(R.id.confirm_obra);
        preview = (ImageView) findViewById(R.id.preview_image);

        errorSesion = (TextView) findViewById(R.id.error_sesion);
        title = (TextInputLayout) findViewById(R.id.text_input_title);
        description = (TextInputLayout) findViewById(R.id.text_input_description);
        time = (TextInputLayout) findViewById(R.id.text_input_time);
        price = (TextInputLayout) findViewById(R.id.text_input_price);

        select_image_galery.setOnClickListener(this);
        select_image_camera.setOnClickListener(this);
        add_session.setOnClickListener(this);
        confirm.setOnClickListener(this);

        crudEntradas = CRUDEntradas.getInstancia(getApplicationContext());

        sesionDataset = new ArrayList<Sesion>();

        mRecyclerView = (RecyclerView) findViewById(R.id.add_session_list);
        //mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SesionAdapter(sesionDataset);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            title.getEditText().setText(savedInstanceState.getString(SAVE_TITLE));
            description.getEditText().setText(savedInstanceState.getString(SAVE_DESCR));
            time.getEditText().setText(String.valueOf(savedInstanceState.getInt(SAVE_TIME)));
            price.getEditText().setText(String.valueOf(savedInstanceState.getDouble(SAVE_PRECIO)));
            preview_bitmap = savedInstanceState.getParcelable(SAVE_BITMAP);

            if (preview_bitmap != null) {
                preview.setImageBitmap(preview_bitmap);
                preview.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVE_TITLE, title.getEditText().getText().toString());
        outState.putString(SAVE_DESCR, description.getEditText().getText().toString());

        String tmpTime = time.getEditText().getText().toString();
        if (!tmpTime.isEmpty())
            outState.putInt(SAVE_TIME, Integer.valueOf(tmpTime));

        String tmpPrice = price.getEditText().getText().toString();
        if (!tmpPrice.isEmpty())
            outState.putDouble(SAVE_PRECIO, Double.valueOf(tmpPrice));

        if (preview_bitmap != null)
            outState.putParcelable(SAVE_BITMAP, preview_bitmap);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.select_image_galery) {
            getImageFromGalery();
        } else if (id == R.id.select_image_camera) {
            getImageFromCamera();
        } else if (id == R.id.confirm_obra) {
            validateForm();
        } else if (id == R.id.add_session) {
            addSessionDate();
        }

    }


    private void addSessionDate() {

        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                pickedYear = year;
                pickedDay = dayOfMonth;
                pickedMonth = monthOfYear;
                //Toast.makeText(getApplicationContext(), Integer.toString(pickedYear), Toast.LENGTH_LONG).show();
                addSessionTime();
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

        /*pickedMinute = c.get(Calendar.MINUTE);
        pickedHour = c.get(Calendar.HOUR_OF_DAY);
        pickedDay = c.get(Calendar.DAY_OF_MONTH);
        pickedMonth = c.get(Calendar.MONTH);
        pickedYear = c.get(Calendar.YEAR);

        addSessionItem();*/
    }


    private void addSessionTime() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                pickedHour = hourOfDay;
                pickedMinute = minute;
                addSessionItem();
            }
        }, 0, 0, true);

        timePickerDialog.show();
    }

    private void addSessionItem() {
        Calendar c = Calendar.getInstance();
        c.set(pickedYear, pickedMonth, pickedDay, pickedHour, pickedMinute);
        Date date = c.getTime();

        Date now = Calendar.getInstance().getTime();

        if (now.before(date)) {
            Sesion sesion = new Sesion();
            sesion.setDate(date);
            sesion.setAsientosDisponibles(40);
            ((SesionAdapter)mAdapter).addItem(sesion);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_date), Toast.LENGTH_LONG).show();;
        }
    }

    private void getImageFromGalery() {
        boolean result = Utility.checkPermission(NewObraActivity.this, Utility.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        if (result) {
            /*Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(intent.createChooser(intent, "Selecciona fitxer"), REQUEST_GALERY);
        }
    }

    private void getImageFromCamera() {
        boolean result = Utility.checkPermission(NewObraActivity.this, Utility.PERMISSIONS_REQUEST_CAMERA);
        if (result) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_GALERY && resultCode == RESULT_OK) {
            preview_bitmap = null;
            if (data != null) {
                try {
                    preview_bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext()
                            .getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            preview_bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            preview_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;

            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (preview_bitmap != null) {
            preview.setImageBitmap(preview_bitmap);
            preview.setVisibility(View.VISIBLE);
        } else {
            preview.setVisibility(View.GONE);
        }
    }

    //--------------------------



    // Revusar la gestion de los errores
        // BUG, si se produce un error, y se va a otro activity al volver los campos pueden aparecer
        // marcados con error
        // Posible solucion: Comprobar los campos a medida que se escribe
    private void validateForm() {
        boolean correct = true;

        if (title.getEditText().getText().toString().isEmpty()) {
            title.setError(getString(R.string.required_data));
            correct = false;
        } else {
            title.setError(null);
            title.setErrorEnabled(false);
        }

        if (description.getEditText().getText().toString().isEmpty()) {
            description.setError(getString(R.string.required_data));
            correct = false;
        } else {
            description.setError(null);
            description.setErrorEnabled(false);
        }

        if (time.getEditText().getText().toString().isEmpty()) {
            time.setError(getString(R.string.required_data));
            correct = false;
        } else if (Integer.valueOf(time.getEditText().getText().toString()) <= 0) {
            time.setError(getString(R.string.invalid_data));
            correct = false;
        } else {
            time.setError(null);
            time.setErrorEnabled(false);
        }

        if (price.getEditText().getText().toString().isEmpty()) {
            price.setError(getString(R.string.required_data));
            correct = false;
        } else if (Double.valueOf(price.getEditText().getText().toString()) <= 0.0) {
            price.setError(getString(R.string.invalid_data));
            correct = false;
        } else {
            price.setError(null);
            price.setErrorEnabled(false);
        }

        if (mAdapter.getItemCount() == 0) {
            errorSesion.setVisibility(View.VISIBLE);
            correct = false;
        } else errorSesion.setVisibility(View.GONE);

        if (correct) {
            //Creamos la obra
            ObraTeatre obra = new ObraTeatre();
            obra.setTitle(title.getEditText().getText().toString());
            obra.setDescription(description.getEditText().getText().toString());
            obra.setTime_minutes(Integer.valueOf(time.getEditText().getText().toString()));
            obra.setPrice(Double.valueOf(price.getEditText().getText().toString()));
            //obra.setDates("27/06/2016 - 31/07/2016");

            String img_path = System.currentTimeMillis() + ".jpg";
            obra.setImg_path(img_path);

            new AddObra().execute(obra);
        }

        return;
    }


    private class AddObra extends AsyncTask<ObraTeatre, Void, Void> {

        @Override
        protected Void doInBackground(ObraTeatre... obra) {

            //Insertamos la obra
            ObraTeatre obraTeatre = obra[0];

            Date minDate = null; //Calendar.getInstance().getTime();
            Date maxDate = null; //Calendar.getInstance().getTime();

            for (int i = 0; i < sesionDataset.size(); i++) {
                Date tmp = sesionDataset.get(i).getDate();
                if (minDate == null || minDate.after(tmp))
                    minDate = tmp;

                if (maxDate == null || maxDate.before(tmp))
                    maxDate = tmp;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String minD = simpleDateFormat.format(minDate);
            String maxD = simpleDateFormat.format(maxDate);

            obraTeatre.setDates(minD + " - " + maxD);

            if (preview_bitmap != null) {
                File file = new File(
                        Environment.getExternalStorageDirectory() + "/" + MainActivity.MAIN_FOLDER,
                        obraTeatre.getImg_path());

                FileOutputStream fo;

                try {
                    file.createNewFile();
                    fo = new FileOutputStream(file);
                    preview_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fo);
                    fo.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String idObra = crudEntradas.insertObra(obraTeatre);

            // Insertamos las sesiones

            for (int i = 0; i < sesionDataset.size(); i++) {
                Sesion sesion = sesionDataset.get(i);
                sesion.setAsientosVendidos("");
                sesion.setEntradaNormales(0);
                sesion.setEntradasDescuento(0);
                sesion.setRecaptacion(0);
                sesion.setId_obra(idObra);

                crudEntradas.insertSesio(sesion);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(MainActivity.CONFIRM_ADD, true);
            startActivity(intent);
            //Snackbar.make(coordinatorLayout, getApplicationContext().getString(R.string.correct_add),
              //      Snackbar.LENGTH_SHORT).show();

            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent);
        }
    }

}