package com.example.martin.campo;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by Martin on 21/02/2016.
 */
public class PopUpAdd extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up_add);

        DisplayMetrics met = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(met);
        getWindow().setLayout((int)(met.widthPixels*.8),(int)(met.heightPixels*.8));
    }
}
