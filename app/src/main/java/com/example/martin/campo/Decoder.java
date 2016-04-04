package com.example.martin.campo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Legui on 01/04/2016.
 */
public class Decoder extends AsyncTask {


    String _Ubicacion_ZIP;
    String _Destino_Descompresion;
    boolean _Mantener_ZIP;
    Job job;
    private MainActivity myMainActivity;
    Context cotx;

    /**
     * Descomprime un archivo .ZIP
     * @param ctx Contexto de la Aplicación Android
     * @param  Ubicacion Ruta ABSOLUTA de un archivo .zip
     * @param Destino Ruta ABSOLUTA del destino de la descompresión. Finalizar con /
     * @param Mantener Indica si se debe mantener el archivo ZIP despues de descomprimir
     */
    public Decoder(MainActivity ctx, String Ubicacion, String Destino, boolean Mantener)
    {
        _Ubicacion_ZIP = Ubicacion;
        _Destino_Descompresion = Destino;
        _Mantener_ZIP = Mantener;
        myMainActivity =  ctx;

    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        myMainActivity.adaptador.notifyDataSetChanged();

    }

    @Override
    protected Object doInBackground(Object[] params) {
        int size;
        byte[] buffer = new byte[2048];

        new File(_Destino_Descompresion).mkdirs(); //Crea la ruta de descompresion si no existe
       job= new Job(); // Crea el job a agregar a la lista con los datos leidos a medida que se van descomprimiendo

        try {
            try {
                FileInputStream lector_archivo = new FileInputStream(_Ubicacion_ZIP);
                ZipInputStream lector_zip = new ZipInputStream(lector_archivo);
                ZipEntry item_zip = null;

                while ((item_zip = lector_zip.getNextEntry()) != null) {
                    Log.e("Descompresor", "Descomprimiendo " + item_zip.getName());
                    String file = _Destino_Descompresion + item_zip.getName();
                    FileOutputStream outStream = new FileOutputStream(file);

                    BufferedOutputStream bufferOut = new BufferedOutputStream(outStream, buffer.length);

                    while ((size = lector_zip.read(buffer, 0, buffer.length)) != -1) {
                            bufferOut.write(buffer, 0, size);
                    }


                    bufferOut.flush();
                    bufferOut.close();
                    /////////////////////////carga la lista/////////////////////////////
                    if(item_zip.getName().contains(".jpg")){
                        // si el final del item es .jpg-> toda la direccion completa guardarla en la listas -> _Destino_Descompresion + item_zip.getName()
                        job.photo.add(file);
                        Log.e("Lo quegurda en foto: ", file);
                    }else if(item_zip.getName().contains(".txt")){
                        // si es .txt LLamar a una funcion leerInfo(_Destino_Descompresion + item_zip.getName()) que lo abre, lo vaya letendo linea por linea y vaya cargando la lista
                        loadInfo(file);
                    }
                    //////////////////////////////////////////////////////////////////////

                }
                lector_zip.close();
                lector_archivo.close();

                if(!_Mantener_ZIP)
                    new File(_Ubicacion_ZIP).delete();

            } catch (Exception e) {
                Log.e("Descompresor", "Descomprimir", e);
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return null;
    }

    private void loadInfo(String file) {
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            Log.e("abrir txt", "falllooooooooooooo", e);
        }
        BufferedReader bf = new BufferedReader(fr);
        String sCadena;
        List<String> l = new ArrayList<String>();
        try {
            while ((sCadena = bf.readLine())!=null) {
                l.add(sCadena);
            }
            job.name = l.get(0);
            job.date = l.get(1);
            job.descrip = l.get(2);
            job.coord.latitud = Double.parseDouble(l.get(3));
            job.coord.longitud = Double.parseDouble(l.get(4));
            job.isImported = true;
            Conteiner.jobs.add(job);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
