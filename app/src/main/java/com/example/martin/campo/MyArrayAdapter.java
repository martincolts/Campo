package com.example.martin.campo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Legui on 30/12/2015.
 */
public class MyArrayAdapter extends ArrayAdapter <Job> {

    private int layoutResourceId;
    private Context context;


    public MyArrayAdapter(Context context, int layoutResourceId, List<Job> trabajos) {
        super(context, layoutResourceId , trabajos);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;


        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);


        holder = new Holder();
        holder.trabajo = Conteiner.jobs.get(position);
        holder.nombre = (TextView) row.findViewById(R.id.textViewNombre);
        row.setTag(holder);
        setupItem(holder);

        return row;
    }

    private void setupItem(Holder holder) {
        if(holder.trabajo.isImported){
            holder.nombre.setTextColor(Color.parseColor("#FFBA2020"));
        };

        String resultado = holder.trabajo.name + " \n"+ holder.trabajo.date;
        holder.nombre.setText(resultado);

    }

    public static class Holder { // es auxiliar
        Job trabajo;
        TextView nombre;
    }

    @Override
    public int getCount() {
        //System.out.println("El tamanio de la lista es: "+ Inventario.productos.size());
        return Conteiner.jobs.size();
    }

    @Override
    public Job getItem(int position) {
        // System.out.println("Lo que devuelve el getItem: " + Inventario.productos.get(position).nombre +"....Inventario.productos en la posicion: "+position);
        return Conteiner.jobs.get(position);
    }



    /*
    private void setNameTextChangeListener(final AtomPaymentHolder holder) {
        holder.name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.atomPayment.setName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void setValueTextListeners(final AtomPaymentHolder holder) {
        holder.value.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    holder.atomPayment.setValue(Double.parseDouble(s.toString()));
                }catch (NumberFormatException e) {
                    Log.e(LOG_TAG, "error reading double value: " + s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }*/
}
