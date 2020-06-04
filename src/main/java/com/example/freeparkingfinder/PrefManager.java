package com.example.freeparkingfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class PrefManager
{
    private SharedPreferences spFav;
    private SharedPreferences.Editor editorFav;
    private Context context;



    private static final String PREF_NAME = "fav";

    PrefManager(Context context)
    {
        this.context = context;
        spFav = context.getSharedPreferences(PREF_NAME, 0);
        editorFav = spFav.edit();
    }

    void setFavourites(String name, String position)
    {
        editorFav.putString(name, position);
        Log.d("click", "name and pos  " + name + position);
        editorFav.commit();
    }

    Map<String, ?> getFavourites()
    {
        return spFav.getAll();
    }

    void clearAll() //Does this clear both history and favourites?----
    {
        editorFav.clear();
        editorFav.commit();
    }

}
