package com.example.martin.campo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Muestra el detalle de los jobs, Usando un GridView para mostrar las imagenes en 90*90
 */
public class DetailActivity extends AppCompatActivity {

    private TextView infoo;
    private GridView photoV;
    private TextView nam;
    private ImageButton bDel;
    private ImageButton bSave;
    private ImageButton shareButton;
    private Job j;
    public static final int BUFFER_SIZE = 2048;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupActionBar();

        infoo = (TextView) findViewById(R.id.info);
        photoV = (GridView) findViewById(R.id.photoView);
        nam  = (TextView) findViewById(R.id.name);
        bDel = (ImageButton)findViewById(R.id.delButton);
        bSave = (ImageButton) findViewById(R.id.saveButton);
        final int jobId = (int) getIntent().getExtras().get("jobId");
        j = Conteiner.jobs.get(jobId);
        photoV.setAdapter(new ImageAdapter(this, j.photo));
        nam.setText(j.name);
        nam = (TextView) findViewById(R.id.name);

        if (Conteiner.jobs != null) {
            j = Conteiner.jobs.get(jobId);

            photoV.setAdapter(new ImageAdapter(this, j.photo));

            nam.setText(j.name);

            StringBuilder sb = new StringBuilder("\n");
            String cadena = sb.append("Fecha: ").append(j.date).append("\n\n Coordenadas: ").append("\n Lat: ").append(String.valueOf(j.coord.latitud)).append("\n Long: ").append(String.valueOf(j.coord.longitud)).append("\n\n Descripcion: ").append(j.descrip).toString();
            infoo.setText(cadena);

            shareButton = (ImageButton) findViewById(R.id.shareButton);
        } else {
            Intent returnToMain = new Intent(getBaseContext(), MainActivity.class);
            startActivity(returnToMain);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Set up the {@link ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Detail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.martin.campo/http/host/path")
        );

        AppIndex.AppIndexApi.start(client, viewAction);

        photoV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DetailActivity.this, showImage.class);
                String uri = j.photo.get(position);
                i.putExtra("jobUri", uri);
                startActivity(i);
            }
        });


        bDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
                alert.setTitle("Elimiar");
                alert.setMessage("¿Seguro desea eliminar este trabajo?");
                alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DataBaseHandler db = new DataBaseHandler(DetailActivity.this);
                        db.deleteJob(j);
                        Conteiner.jobs.remove(j);
                        Toast.makeText(DetailActivity.this, "Eliminado Correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                alert.setNegativeButton("Nooo!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Campo_Folder");
                    if(!root.isDirectory())
                        root.mkdirs();
                    File gpxfile = new File(root, "texto.txt");
                    gpxfile.delete();
                    FileWriter writer = new FileWriter(gpxfile, true);
                    writer.append(j.getName() + "\n");
                    writer.append(j.date + "\n");
                    writer.append(j.descrip + "\n");
                    writer.append(j.coord.latitud + "\n");
                    writer.append(j.coord.longitud + "\n");
                    writer.flush();
                    writer.close();
                    Toast.makeText(getBaseContext(), "Se creo el archivo de texto", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("file No se creo ","");
                }



                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                StringBuilder titleBuilder = new StringBuilder();
                StringBuilder title = titleBuilder.append("Campo App, recibiste el Job: ").append(j.name);
                i.putExtra(Intent.EXTRA_SUBJECT, title.toString());
                i.putExtra(Intent.EXTRA_TEXT, "Por favor, para ver los datos recibidos con nuestra aplicacion abra con la misma el .zip adjunto, Muchas gracias");

                String targetFilePath = Environment.getExternalStorageDirectory() + File.separator + "Campo_Folder" + File.separator + "texto.txt";

                Log.v("File path texto: ", targetFilePath);
                Uri attachmentUri = Uri.parse(targetFilePath);

                i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));

                Vector<String> datos = new Vector<String>();

                datos.add(targetFilePath);

                Log.v("file photos cant", (((Integer)j.photosRealUri.size()).toString()));
                for (String pho : j.photosRealUri){
                    datos.add(pho.toString());
                    Log.v("file, foto: ", pho.toString());
                }

                String zipFile = Environment.getExternalStorageDirectory() + File.separator + "Campo_Folder" + File.separator + "Data.zip";
                File zipFileLocate = new File (Environment.getExternalStorageDirectory() + File.separator + "Campo_Folder" + File.separator + "Data.zip");
                if (zipFileLocate.exists()){
                    zipFileLocate.delete();
                }
                Log.v("file, Data.zip",zipFile);
                Uri attachmentUriZip = Uri.parse(zipFile);
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+attachmentUriZip));
                try {
                    zip(zipFile, datos);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
        }});

        if (j.isImported) {
            bDel.setEnabled(false);
            bSave.setEnabled(true);
            bSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
                    alert.setTitle("Guardar");
                    alert.setMessage("¿Seguro desea Guardar este trabajo?");
                    alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            DataBaseHandler db = new DataBaseHandler(DetailActivity.this);
                            db.addJob(j);
                            j.isImported = false;
                            Toast.makeText(DetailActivity.this, "Guardado Correctamente", Toast.LENGTH_SHORT).show();
                            bSave.setEnabled(false);
                            bDel.setEnabled(true);
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            });
        }else bSave.setEnabled(false);
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction3 = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Detail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.martin.campo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction3);
        client.disconnect();
    }

    public void zip (String zipFileName , Vector<String> fileNumberToCompres) throws IOException {
        FileOutputStream f = new FileOutputStream(zipFileName);
        ZipOutputStream zipOutputStream = new ZipOutputStream(f);

        for (String fileName : fileNumberToCompres){
            addToZipFile(fileName , zipOutputStream);
        }

        zipOutputStream.close();
        f.close();
    }

    public void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

}
