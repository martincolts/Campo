package com.example.martin.campo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class showImage extends AppCompatActivity {

    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        im = (ImageView) findViewById(R.id.imageView5);
        String i = (String) getIntent().getExtras().get("jobUri");
        im.setImageURI( Uri.parse("content://media"+i)) ;
    }
}
