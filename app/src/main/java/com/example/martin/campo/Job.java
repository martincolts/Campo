package com.example.martin.campo;

import android.location.Location;

import java.util.Date;

/**
 * Created by Legui on 18/03/2016.
 */
public class Job {

    public Job(){
        this.id = 0;
        this.name = "";
        this.coord = null;
        this.descrip = "";
        this.date = "";
        this.id_photo = 0;
    }

    public String getNane() {
        return name;
    }




    public void setName(String nombre) {
        this.name = nombre;

    }



    public int id;
    public CoordGPS coord;
    public String name;
    public String date;
    public String descrip;
    public int id_photo;
    public String[] photo;


    @Override
    public boolean equals(Object o) {

        Job job = (Job) o;

        return id == job.id;

    }


}
