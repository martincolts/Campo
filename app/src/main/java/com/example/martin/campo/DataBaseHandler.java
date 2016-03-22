package com.example.martin.campo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Legui on 19/03/2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "trabajosBd";

    // TABLA TRABAJOS
    private static final String TABLE_JOBS = "trabajos";
    private static final String J_ID = "id";
    private static final String J_NAME = "name";
    private static final String J_LAT = "lat";
    private static final String J_LONG = "long";
    private static final String J_DESCRIP  = "descrip";
    private static final String J_DATE  = "date";

    // TABLA FOTOS
    private static final String TABLE_PHOTOS = "fotos";
    private static final String P_ID_JOB  = "id_job";
    private static final String P_LINK = "link";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_JOBS_TABLE = "CREATE TABLE " + TABLE_JOBS + "("
                + J_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + J_NAME + " TEXT, " + J_LAT +" REAL, "+ J_LONG +" REAL, "+ J_DESCRIP+ " TEXT, "
                +J_DATE + " TEXT " + ")";
        db.execSQL(CREATE_JOBS_TABLE);

        String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + "("
                + P_ID_JOB + " TEXT ," + P_LINK + " TEXT, PRIMARY KEY ( "+P_ID_JOB+" , "+P_LINK +" )"
                 + ")";
        db.execSQL(CREATE_PHOTOS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);

// Create tables again
        onCreate(db);

    }

    public// Adding new job
    void addJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(J_NAME, job.name);
        values.put(J_LAT, job.coord.latitud);
        values.put(J_LONG, job.coord.longitud);
        values.put(J_DESCRIP, job.descrip);
        values.put(J_DATE, job.date);

// Inserting Row
        db.insert(TABLE_JOBS, null, values);
        // una vez que lo insertó, ver cual es su id, y con ese id crear la relacion con la tabla photos
        int id = getIdJob(job);
        job.id = id;
        updateJob(job);

        db.close(); // Closing database connection
        addPhotos(job.photo, job.id);



    }

    private void addPhotos(List<String> photo, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (String i:photo) {
            ContentValues val = new ContentValues();
            val.put(P_ID_JOB, id );
            val.put(P_LINK,i);
            db.insert(TABLE_PHOTOS, null, val);
        }
        db.close();



    }

    // Getting productos Count
    public int getJobsCount() {

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor = db1.rawQuery("SELECT * FROM trabajos", null);
        int i = cursor.getCount();
        cursor.close();
        db1.close();
        return i;

    }
    public void getAllJobs() { // devuelve todos los trabajos ordenados por fecha
// Select All Query

        SQLiteDatabase db1 = this.getWritableDatabase();
        Cursor cursor = db1.rawQuery("SELECT * FROM trabajos", null);

        if (cursor.moveToFirst()) {
            do {
                Job job = new Job();

                job.id = cursor.getInt(0);
                job.name = cursor.getString(1);
                job.coord.latitud=cursor.getDouble(2);
                job.coord.longitud= cursor.getDouble(3);
                job.descrip = cursor.getString(4);
                job.date = cursor.getString(5);

                Conteiner.jobs.add(job);
           } while (cursor.moveToNext());
        }
        db1.close();

    }


    // Getting single job
    Job getJob(int id) { // dado el id se devuelve el job TODO hacer que la key sea el nombre
        SQLiteDatabase db = MainActivity.db.getReadableDatabase();

        Cursor cursor = db.query(TABLE_JOBS, new String[]{J_ID,
                        J_NAME, J_LAT, J_LONG, J_DESCRIP,J_DATE}, J_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Job job = new Job();
        job.id = cursor.getInt(0);
        job.name = cursor.getString(1);
        job.coord.latitud= cursor.getFloat(2);
        job.coord.longitud= cursor.getFloat(3);
        job.descrip = cursor.getString(4);
        job.date = cursor.getString(5);
        db.close();
        return job;

    }



    // Updating single job
    public int updateJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(J_NAME, job.name);
        values.put(J_LAT, job.coord.latitud);
        values.put(J_LONG, job.coord.longitud);
        values.put(J_DESCRIP, job.descrip);
        values.put(J_DATE, job.date);

// updating row
        return db.update(TABLE_JOBS, values, J_ID + " = ?",
                new String[] { String.valueOf(job.id)});

    }

    // Deleting single job
    public void deleteJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOBS, J_ID + " = ?",
                new String[]{String.valueOf(job.id)});
        db.close();
    }





    public int getIdJob (Job job){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_JOBS, new String[]{J_ID}, J_NAME + "=?",
                new String[]{job.name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        db.close();

        return cursor.getInt(0);
    }

}



