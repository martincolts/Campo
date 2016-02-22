package com.example.martin.campo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Martin on 21/02/2016.
 */
public class PopUpAdd extends Activity{

    private EditText nameJobInput ;
    private EditText descriptionInput ;
    private EditText dateInput ;
    private Button insert ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up_add);

        DisplayMetrics met = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(met);
        getWindow().setLayout((int)(met.widthPixels*.8),(int)(met.heightPixels*.8));

        nameJobInput = (EditText) findViewById(R.id.name_job_input);
        descriptionInput = (EditText) findViewById(R.id.description_input);
        dateInput = (EditText) findViewById(R.id.date_input);

        insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backData = new Intent();
                backData.putExtra("name", nameJobInput.getText().toString());
                backData.putExtra("description", descriptionInput.getText().toString());
                backData.putExtra("date", dateInput.getText().toString());
                setResult(RESULT_OK, backData);
                getFragmentManager().popBackStack();
            }
        });
    }
}
