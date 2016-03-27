package com.example.martin.campo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupActionBar();

        infoo = (TextView) findViewById(R.id.info);
        photoV = (GridView) findViewById(R.id.photoView);
        nam  = (TextView) findViewById(R.id.name);

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

