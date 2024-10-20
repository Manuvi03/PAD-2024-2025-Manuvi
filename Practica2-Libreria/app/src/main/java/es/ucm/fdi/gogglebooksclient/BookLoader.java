package es.ucm.fdi.gogglebooksclient;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<String>> {

    String queryString;
    String printType;
    String URL_BASE = "https://www.googleapis.com/books/v1/volumes?";

    public BookLoader(@NonNull Context context,String queryString, String printType) {
        super(context);
        this.queryString = queryString;
        this.printType = printType;
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        try {
            return getBookInfoJson(queryString, printType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStartLoading(){
        forceLoad();
    }

    public List<String> getBookInfoJson(String queryString, String printType) throws IOException {

        URL url = new URL(URL_BASE+queryString);
        //Uri uri = Uri.parse(URL_BASE).buildUpon().appendQueryParameter("inAuthors", queryString).
          //      appendQueryParameter("printType", printType).build();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        List<String> result = new ArrayList<>();
        while((line = reader.readLine())!= null){
            result.add(line);
        }

        return result;

    }







}
