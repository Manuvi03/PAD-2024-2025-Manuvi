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

        URL url = new URL(URL_BASE+queryString+"&key=" + "AIzaSyCgcmZj01An4CkRZicIA2EzQrk-bGTL9qU");
        Log.i("URL", url.toString());
        //Uri uri = Uri.parse(URL_BASE).buildUpon().appendQueryParameter("inAuthors", queryString).
          //      appendQueryParameter("printType", printType).build();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        String jsonResponse = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).readLine();
        List<BookInfo> result = BookInfo.fromJsonResponse(jsonResponse);
        urlConnection.disconnect();
        return result;

    }
}
