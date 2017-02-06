package idi.acedo.joel.ventadeentradas;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

import idi.acedo.joel.ventadeentradas.BD.CRUDEntradas;
import idi.acedo.joel.ventadeentradas.BD.ContratoEntradas;
import idi.acedo.joel.ventadeentradas.asientos.Asiento;
import idi.acedo.joel.ventadeentradas.ventas.Venta;

public class ConfirmActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ID_SESION_INTENT = "id_sesion_intent";
    public static final String ASIENTOS_INTENT = "asientos_intent";
    public static final String TOTAL_INTENT = "total_intent";
    public static final String ID_OBRA_INTENT = "id_obra_intent";


    private TextView total_view;
    private TextView seients_view;
    private TextInputLayout name_edit;
    private TextInputLayout email_edit;
    private Button confirm_button;

    private double total;
    private String seientsSelec;
    private String idSesion;

    private String email;
    private String name;

    CRUDEntradas crudEntradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        crudEntradas = CRUDEntradas.getInstancia(getApplicationContext());

        total_view = (TextView) findViewById(R.id.total_euro_confirm);
        seients_view = (TextView) findViewById(R.id.asientos_num_confirm);
        name_edit = (TextInputLayout) findViewById(R.id.name_add);
        email_edit = (TextInputLayout) findViewById(R.id.email_add);
        confirm_button = (Button) findViewById(R.id.confirm_buy);

        confirm_button.setOnClickListener(this);

        Intent intent = getIntent();
        total = intent.getDoubleExtra(TOTAL_INTENT, 0.0);
        seientsSelec = intent.getStringExtra(ASIENTOS_INTENT);
        idSesion = intent.getStringExtra(ID_SESION_INTENT);

        if (savedInstanceState != null) {
            String tmpId = savedInstanceState.getString(ID_SESION_INTENT);
            if (tmpId != null) idSesion = tmpId;

            String tmpAs = savedInstanceState.getString(ASIENTOS_INTENT);
            if (tmpAs != null) seientsSelec = tmpAs;

            Double tmpTo = savedInstanceState.getDouble(TOTAL_INTENT);
            if (tmpTo != null) total = tmpTo;
        }

        total_view.setText(String.valueOf(total) + " " + Butacas.EURO);
        seients_view.setText(seientsSelec.replace(':', ','));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ID_SESION_INTENT, idSesion);
        outState.putString(ASIENTOS_INTENT, seientsSelec);
        outState.putDouble(TOTAL_INTENT, total);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.confirm_buy) {
            checkForm();
        }
    }

    private void checkForm() {
        boolean correct = true;

        if (name_edit.getEditText().getText().toString().isEmpty()) {
            name_edit.setError(getString(R.string.required_data));
            correct = false;
        } else {
            name_edit.setError(null);
            name_edit.setErrorEnabled(false);
        }

        if (email_edit.getEditText().getText().toString().isEmpty()) {
            email_edit.setError(getString(R.string.required_data));
            correct = false;
        } else if (!isEmailValid(email_edit.getEditText().getText())) {
            email_edit.setError(getString(R.string.error_mail));
            correct = false;
        } else {
            email_edit.setError(null);
            email_edit.setErrorEnabled(false);
        }

        if (correct) {
            name = name_edit.getEditText().getText().toString();
            email = email_edit.getEditText().getText().toString();
            new UpdateAsientosAndVentas().execute();
        }

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class UpdateAsientosAndVentas extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Cursor cursor = crudEntradas.getSesioById(idSesion);
            String asientosOcupados = "";

            if (cursor != null && cursor.moveToNext()) {
                asientosOcupados = cursor.getString(cursor.getColumnIndex(ContratoEntradas.Sesiones.AVENDIDOS));
                asientosOcupados = asientosOcupados + ":" + seientsSelec;
                cursor.close();
            }

            crudEntradas.updateSeientsFromSesion(idSesion, asientosOcupados);

            Venta venta = new Venta();
            venta.setName(name);
            venta.setEmail(email);
            venta.setAsientos(seientsSelec);
            venta.setId_sesion(idSesion);

            crudEntradas.insertVenta(venta);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Snackbar.make(coordinatorLayout, getApplicationContext().getString(R.string.correct_add),
              //      Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(MainActivity.CONFIRM_BUY, true);
            startActivity(intent);
        }
    }
}
