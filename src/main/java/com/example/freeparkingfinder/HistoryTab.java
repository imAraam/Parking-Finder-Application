package com.example.freeparkingfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//history tab


public class HistoryTab extends AppCompatActivity {

    ListView simpleList;
    Map<String, ?> allValues;
    List<String> historyListName = new ArrayList<String>();

    PrefManagerHistory prefManagerHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_tab);

        simpleList = findViewById(R.id.historyList);

        prefManagerHistory = new PrefManagerHistory(this);

        allValues = prefManagerHistory.getHistory();

        for (Map.Entry<String, ?> entry : allValues.entrySet()) {
            Log.d("mapValues", "title: " + entry.getKey() + "pos: " + entry.getValue().toString());
            if (entry.getKey() != null)
            {
                historyListName.add(entry.getKey()); //extract marker names from map
            }
        }
        Log.d("mapValues", "title: " + historyListName);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, R.layout.activity_history_listview, R.id.history_textView, historyListName);
        simpleList.setAdapter(arrayAdapter);



        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManagerHistory.clearAll(); //clear history
                recreate(); //refresh activity
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

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsTab();
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

    public void openSettingsTab() {
        Intent intent = new Intent(this, SettingsTab.class);
        startActivity(intent);
    }
}
