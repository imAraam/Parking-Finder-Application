package com.example.freeparkingfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

//Settings tab


public class SettingsTab extends AppCompatActivity {

    int radius;
    int defaultZoom;

    PrefManagerSettings prefManagerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_tab);

        prefManagerSettings = new PrefManagerSettings(this);



        Spinner spinnerRadius = (Spinner) findViewById(R.id.spinnerRadius);
        Spinner spinnerZoom = (Spinner) findViewById(R.id.spinnerZoom);




        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.radiusValues, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerRadius.setAdapter(adapter);




        SharedPreferences prefsRadius = getPreferences(0);
        spinnerRadius.setSelection(prefsRadius.getInt("radius", 0)); /*reloads the last
        chosen radius from previous app session (if any. Otherwise default remains)*/



        ArrayAdapter adapterZoom = ArrayAdapter.createFromResource(this,
                R.array.zoomValues, R.layout.spinner_item);

        adapterZoom.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerZoom.setAdapter(adapterZoom);

        SharedPreferences prefsZoom = getPreferences(0);
        spinnerZoom.setSelection(prefsZoom.getInt("zoom", 0));


        spinnerRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                SharedPreferences.Editor editor = getPreferences(0).edit();


                String item = parent.getItemAtPosition(pos).toString(); //get selected spinner item
                radius = Integer.parseInt(item); //parse to int and store in radius
                prefManagerSettings.setRadius("radius", radius); //save to SharedPreferences to load in main

                int selectedPositionRadius = spinnerRadius.getSelectedItemPosition();
                editor.putInt("radius", selectedPositionRadius); //save to different instance to load in settings tab upon activity reload
                editor.apply();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerZoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                SharedPreferences.Editor editor = getPreferences(0).edit();

                String item = parent.getItemAtPosition(pos).toString();
                defaultZoom = Integer.parseInt(item);
                prefManagerSettings.setZoom("zoom", defaultZoom);

                int selectedPositionZoom = spinnerZoom.getSelectedItemPosition();
                editor.putInt("zoom", selectedPositionZoom);
                editor.apply();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Button mapButton = findViewById(R.id.mapButton);
        //access map tab once map button is clicked
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapTab();
            }
        });

        Button favouritesButton = findViewById(R.id.favouritesButton);
        favouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavouritesTab();
            }
        });

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistoryTab();
            }
        });
    }

    public void openMapTab() { //function used to open the map tab
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openFavouritesTab() {
        Intent intent = new Intent(this, FavouritesTab.class);
        startActivity(intent);
    }

    public void openHistoryTab() {
        Intent intent = new Intent(this, HistoryTab.class);
        startActivity(intent);
    }
}
