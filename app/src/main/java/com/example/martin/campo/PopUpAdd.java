package com.example.martin.campo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Martin on 21/02/2016.
 */
public class PopUpAdd extends Activity{

    private EditText nameJobInput ;
    private EditText descriptionInput ;
    private TextView dateInput ;
    private Button insert ;
    private ImageButton addPhotos;

    private CoordGPS coord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up_add);

        DisplayMetrics met = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(met);
        getWindow().setLayout((int) (met.widthPixels * .8), (int) (met.heightPixels * .8));

        nameJobInput = (EditText) findViewById(R.id.name_job_input);
        descriptionInput = (EditText) findViewById(R.id.description_input);
        dateInput = (TextView) findViewById(R.id.date_input);

        inicilizar(); // inicializa la hora fecha, y las coordenadas tomadas

        insert = (Button) findViewById(R.id.insert);
        addPhotos = (ImageButton) findViewById(R.id.add_photos);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent backData = new Intent(); // TODO ESTO PODRIA CARGAR DIRECTAMENTE ACA LA LISTa DE trabaJOS, SIN el ONRESULT
                backData.putExtra("name", nameJobInput.getText().toString());
                backData.putExtra("description", descriptionInput.getText().toString());
                backData.putExtra("date", dateInput.getText().toString());
                setResult(RESULT_OK, backData);
                getFragmentManager().popBackStack();
                */
                //finish(); TODO para cerrar la ventana una vez agregado el job

                insertJob();


            }
        });

        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void inicilizar() {
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        dateInput.setText(mydate);


        coord = getLocationJob();
    }

    private CoordGPS getLocationJob() { // TODO obtener desde el locationManager
        CoordGPS co = new CoordGPS();
        co.latitud = 0.0;
        co.longitud = 0.0;

        return co;
    }

    private void insertJob() {



        // Reset errors.
        nameJobInput.setError(null);
        descriptionInput.setError(null);
        dateInput.setError(null);

        // Store values at the
        String name = nameJobInput.getText().toString();
        String desc = descriptionInput.getText().toString();
        String date = dateInput.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            nameJobInput.setError(getString(R.string.error_field_required));
            focusView = nameJobInput;
            cancel = true;
        }

        // Check for a valid description
        if (TextUtils.isEmpty(desc)) {
            descriptionInput.setError(getString(R.string.error_field_required));
            focusView = descriptionInput;
            cancel = true;
        }

        // Check for a valid date
        if (TextUtils.isEmpty(date)) {
            dateInput.setError(getString(R.string.error_field_required));
            focusView = dateInput;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //aca agregar todo lo relacionado a guardar los datos en la base de datos y volver al activiti anterior actualizando lo "Nuevo"

            Toast.makeText(this, "GUARDADO CORRECTAMENTE!!!", Toast.LENGTH_SHORT)
                    .show();

            Job job = new Job();
            job.name = nameJobInput.getText().toString();
            job.descrip = descriptionInput.getText().toString();
            job.date = dateInput.getText().toString();
            job.id_photo = 1;
            job.coord = coord;


            //TODO GUARDAR EN LA BD. se puede hacer como un thread
            DataBaseHandler db = new DataBaseHandler(this);
            db.addJob(job);
            job.id = db.getIdJob(job);
            Conteiner.jobs.add(job);

            Toast.makeText(this, "tamanio de jobs!!!" +Conteiner.jobs.size(), Toast.LENGTH_SHORT)
                    .show();


            finish();

        }



    }
}
