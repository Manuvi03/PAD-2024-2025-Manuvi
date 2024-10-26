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
    // se cargan los datos de la API
    public List<BookInfo> loadInBackground() {
        try {
            return getBookInfoJson(queryString, printType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // comprueba datos de caché
    public void onStartLoading(){
        forceLoad();
    }

    public List<BookInfo> getBookInfoJson(String queryString, String printType) throws IOException {

        // primero se completa la URL de la conexión
        URL url = completeURL(queryString, printType);

        // y trás ello se realiza la conexión, en un hilo distinto, devolviendo el resultado obtenido
        return connectToURL(url);
    }

    // completa correctamente la URL
    private URL completeURL(String queryString, String printType) throws IOException{
        URL url;

        // se completa correctamente la URL dependiendo de lo seleccionado por el usuario
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

        return url;
    }






    // ESTA FUNCIÓN HAY QUE REALIZARLA EN UN HILO DISTINTO, SEGÚN LAS DIAPOSITIVAS

    // realiza la conexión URL
    private List<BookInfo> connectToURL(URL url) throws IOException{

        // resultado de la búsqueda tras la conexión
        List<BookInfo> result;

        // se obtiene, se configura y se conecta la URL
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.connect();

        try {
            // Lee el contenido del input stream y guárdalo en jsonResponse
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            String jsonResponse = response.toString(); // Convertir el StringBuilder a String
            result = BookInfo.fromJsonResponse(jsonResponse);
        } finally {
            // siempre se debe cerrar la conexión
            urlConnection.disconnect();
        }
        return result;
    }
}
