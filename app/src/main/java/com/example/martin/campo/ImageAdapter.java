package com.example.martin.campo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Legui on 24/03/2016.
 */
public class ImageAdapter extends BaseAdapter {
    // Contexto de la aplicación
    private Context mContext;
    public static int mSelected = 0;
    private List<String>  mThumbIds = new ArrayList<String>(); // le paso la lista de las uri de cada imagen.
    private List<Uri> uris = new ArrayList<Uri>();

    public ImageAdapter(Context c, List<String> photo) {
        mContext = c;
        mThumbIds = photo;
        getUri();
    }

    private void getUri() {
        for (String i : mThumbIds) {
            Uri u;
            if (i.contains(".jpg")){
                u =Uri.parse(i) ;
            }else {
                u = Uri.parse("content://media" + i);// Uri.parse("content://media"+path.getPath())
            }
            uris.add(u);
        }
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return uris.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public String getThumbId(int position){return mThumbIds.get(position);}

    public View getView(final int position, View convertView, ViewGroup parent) {
        //ImageView a retornar
        ImageView imageView;

        if (convertView == null) {
            /*
            Crear un nuevo Image View de 160x160
            y con recorte alrededor del centro
             */
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(160,160));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageURI(uris.get(position));//setImageResource(mThumbIds[position]);
        ///////////////////////
        try {
            imageView.setTag(position);
            //Ponemos un borde de color naranja a la imagen en miniatura seleccionada en el GrisView
            if (position == mSelected) {
                imageView.setBackgroundColor(Color.parseColor("#ff6203"));
            } else {
                imageView.setBackgroundColor(Color.TRANSPARENT);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////
        return imageView;
    }


}
