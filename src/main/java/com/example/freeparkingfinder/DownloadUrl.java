//DownloadUrl class implementation from
//https://www.youtube.com/watch?v=gAIErOevfoA&t=1040s
package com.example.freeparkingfinder;

import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl
{

    public String readUrl (String myurl) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = httpURLConnection = null;

        try {
            URL url = new URL(myurl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        }
        catch (MalformedURLException e)
        {
            Log.i("Downloadurl", "readUrl"+ e.getMessage());
        }
        finally
        {
            httpURLConnection.disconnect();
        }

        Log.i("Downloadurl", "data: "+ data);
        return data;
    }

}
