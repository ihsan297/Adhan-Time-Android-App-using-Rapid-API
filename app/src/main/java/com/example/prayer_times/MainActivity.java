package com.example.prayer_times;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    Button gotoNext;// for moving to next activity
    String city; // getting city from dropdown

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoNext=findViewById(R.id.btn_gotoNext);
		

        Spinner dropdown = findViewById(R.id.spinner);


//create a list of items for the spinner.
        String[] items = new String[]{"Lahore", "Karachi", "Islamabad","Sahiwal","Sialkot","Peshawar","Gujrat","Faisalabad","Mianwali","Sargodha"};

//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//selected item from dropdown
                city=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gotoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,details.class);
                intent.putExtra("city",city); //passing intentwith selectedcity
                startActivity(intent);


            }
        });

    }
}
