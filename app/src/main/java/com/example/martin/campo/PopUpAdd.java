package com.example.martin.campo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Martin on 21/02/2016.
 */
public class PopUpAdd extends Activity{

    private final int SELECT_PICTURE = 200;

    private EditText nameJobInput ;
    private EditText descriptionInput ;
    private TextView dateInput ;
    private TextView imagesImput;
    private Button insert ;
    private ImageButton addPhotos;
    private ImageView imagen;

    private List<String> temporalImages;
    private List<String> temporalImagesRealUri;

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
        imagesImput = (TextView) findViewById(R.id.textLinkImages);
        imagen = (ImageView) findViewById(R.id.imageView); // TODO esto volarlo


        inicilizar(); // inicializa la hora fecha, y las coordenadas tomadas

        insert = (Button) findViewById(R.id.insert);
        addPhotos = (ImageButton) findViewById(R.id.add_photos);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertJob();
            }
        });

        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Selecciona App de imagen"), SELECT_PICTURE);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK){ // si la respuesta está bien.. entonces...

                    Uri path = data.getData();

                    String realPath;
                    Log.v("path buscando path",path.toString());
                    // SDK < API11
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    Log.v("path photo", path.toString());
                    Log.v("real path photo", realPath);
                    temporalImages.add(path.getPath()); // guarda en la lista adicional las path de las imagenes que se van seleccionando
                    temporalImagesRealUri.add(realPath);
                    imagesImput.append(path.getPath()); // se muestra en pantalla esos path
                    imagen.setImageURI(Uri.parse("content://media"+path.getPath())); // se muestra la ultima imagen
                }
            break;
        }

    }

    private void inicilizar() {
        temporalImages = new ArrayList<String>();
        temporalImagesRealUri = new ArrayList<String>();
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        dateInput.setText(mydate);
        imagesImput.setText("");
    }

    private CoordGPS getLocationJob() { // TODO ver si anda asi sino levantarlo de un servicio para que arranque el popUp y empiece a buscar la location
        CoordGPS co = new CoordGPS();
        Ubicacion ub = new Ubicacion(this);
        co.longitud=ub.getLong();
        co.latitud=ub.getLat();
        return co;
    }

    private void insertJob() {

        // Reset errors.
        nameJobInput.setError(null);
        descriptionInput.setError(null);
        imagesImput.setError(null);

        // Store values at the
        String name = nameJobInput.getText().toString();
        String desc = descriptionInput.getText().toString();

        String photo = imagesImput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a photo in
        if (TextUtils.isEmpty(photo)) {
            imagesImput.setError(getString(R.string.error_photo_required));
            focusView = addPhotos;//imagesImput;
            cancel = true;
        }
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
        if (cancel) {
            focusView.requestFocus();
        } else {

            Job job = new Job();
            job.name = nameJobInput.getText().toString();
            job.descrip = descriptionInput.getText().toString();
            job.date = dateInput.getText().toString();
            job.photo.addAll(temporalImages);// agrega todas las imagenes que habia guardado en la lista temporal.
            job.photosRealUri.addAll(temporalImagesRealUri);
            job.coord = getLocationJob();

            for (int i = 0 ; i < job.photo.size() ; i++ ){
                Log.v("file photo: ", job.photo.get(i).toString());
            }

            for (int i = 0 ; i < job.photosRealUri.size() ; i++ ){
                Log.v("file photoRealUri: ", job.photosRealUri.get(i).toString());
            }

            //TODO GUARDAR EN LA BD. se puede hacer como un thread
            DataBaseHandler db = new DataBaseHandler(this);
            db.addJob(job);

            Conteiner.jobs.add(job);

            Toast.makeText(this, "GUARDADO CORRECTAMENTE!!!", Toast.LENGTH_SHORT)
                    .show();

            finish();
        }
    }

}
