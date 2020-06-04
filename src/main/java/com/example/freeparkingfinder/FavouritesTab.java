package com.example.freeparkingfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//favourites tab

public class FavouritesTab extends AppCompatActivity {

    ListView simpleList;
    Map<String, ?> allValues;
    List<String> favListName = new ArrayList<String>();
    List<String> favListPos = new ArrayList<String>();

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_tab);

        simpleList = findViewById(R.id.favouritesList);

        prefManager = new PrefManager(this);

        allValues = prefManager.getFavourites();

        for (Map.Entry<String, ?> entry : allValues.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            favListName.add(entry.getKey());
            favListPos.add(entry.getValue().toString());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, R.layout.activity_listview, R.id.textView, favListName);
        simpleList.setAdapter(arrayAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String selectedItemPos = favListPos.get(position); /*using same position index as
                item name selected, retrieve latitude and longitude of item*/


                Intent intent = new Intent(FavouritesTab.this, MainActivity.class);
                intent.putExtra("FIND_MARKER_NAME", selectedItem);
                intent.putExtra("FIND_MARKER_POS", selectedItemPos);
                startActivity(intent);
            }
        });



        Button clearButton = findViewById(R.id.clearButton);
        //access map tab once map button is clicked
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.clearAll();
                recreate();
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

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsTab();
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

    public void openSettingsTab() {
        Intent intent = new Intent(this, SettingsTab.class);
        startActivity(intent);
    }

    public void openHistoryTab() {
        Intent intent = new Intent(this, HistoryTab.class);
        startActivity(intent);
    }
}
