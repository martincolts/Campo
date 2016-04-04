package com.example.martin.campo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * Muestra el detalle de los jobs, Usando un GridView para mostrar las imagenes en 90*90
 */
public class DetailActivity extends AppCompatActivity  {

    private TextView infoo;
    private GridView photoV;
    private TextView nam;
    private ImageButton bDel;
    private ImageButton bSave;

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
        final Job j = Conteiner.jobs.get(jobId);
        photoV.setAdapter(new ImageAdapter(this, j.photo ));
        nam.setText(j.name);

        StringBuilder sb = new StringBuilder("\n");
        String cadena = sb.append("Fecha: ").append(j.date).append("\n\n Coordenadas: ").append("\n Lat: ").append(String.valueOf(j.coord.latitud)).append("\n Long: ").append(String.valueOf(j.coord.longitud)).append("\n\n Descripcion: ").append(j.descrip).toString();

        infoo.setText(cadena);
        photoV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DetailActivity.this, showImage.class);
                String uri = j.photo.get(position);
                i.putExtra("jobUri", uri);
                startActivity(i);
            }
        });


        bDel.setOnClickListener(new View.OnClickListener() {
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

        if (j.isImported) {
            bDel.setEnabled(false);
            bSave.setEnabled(true);
            bSave.setOnClickListener(new View.OnClickListener() {
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

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }





}

