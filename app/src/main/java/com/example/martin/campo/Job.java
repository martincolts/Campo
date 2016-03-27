package com.example.martin.campo;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Legui on 18/03/2016.
 */
public class Job {

    public int id;
    public CoordGPS coord;
    public String name;
    public String date;
    public String descrip;
    public List<String> photo;

    public Job(){
        this.id = 0;
        this.name = "";
        this.coord = new CoordGPS();
        this.descrip = "";
        this.date = "";
        this.photo = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;

    }

    @Override
    public boolean equals(Object o) {
        Job job = (Job) o;
        return id == job.id;

    }


}
