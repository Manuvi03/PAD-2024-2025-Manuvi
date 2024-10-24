package es.ucm.fdi.gogglebooksclient;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class BookLoader extends AsyncTaskLoader<List<BookInfo>> {

    String queryString;
    String printType;
    String URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=";

    public BookLoader(@NonNull Context context,String queryString, String printType) {
        super(context);
        this.queryString = queryString;
        this.printType = printType;
    }

    @Nullable
    @Override
    public List<BookInfo> loadInBackground() {
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

    public List<BookInfo> getBookInfoJson(String queryString, String printType) throws IOException {

        URL url;
        if(Objects.equals(printType, "Revista")){
            url = new URL(URL_BASE + queryString + "&printType=" + "magazines"+ "&key=" + "AIzaSyCgcmZj01An4CkRZicIA2EzQrk-bGTL9qU");
        }
        else if(Objects.equals(printType, "Libro")) {
            url = new URL(URL_BASE + queryString + "&printType=" + "books"+ "&key=" + "AIzaSyCgcmZj01An4CkRZicIA2EzQrk-bGTL9qU");
        }
        else{
            url = new URL(URL_BASE + queryString +"&key=" + "AIzaSyCgcmZj01An4CkRZicIA2EzQrk-bGTL9qU");
        }
        Log.i("URL", url.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // Lee todo el contenido del input stream y guárdalo en jsonResponse
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        String jsonResponse = response.toString(); // Convertir el StringBuilder a String
        List<BookInfo> result = BookInfo.fromJsonResponse(jsonResponse);
        urlConnection.disconnect();
        return result;
    }
}
