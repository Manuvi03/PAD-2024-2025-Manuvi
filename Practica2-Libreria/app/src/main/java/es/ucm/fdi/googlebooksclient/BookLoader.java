package es.ucm.fdi.googlebooksclient;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class BookLoader extends AsyncTaskLoader<List<BookInfo>> {

    // atributos
    private final String queryString;
    private final String printType;
    private List<BookInfo> result = null;

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
            getBookInfoJson(queryString, printType);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // comprueba datos de caché
    public void onStartLoading(){
        forceLoad();
    }

    private void getBookInfoJson(String queryString, String printType) throws IOException {
        URL url = completeURL(queryString, printType); // Completar la URL

        // no hace falta crear un hilo para la conexion ya que loadInBackground se hace en un hilo a parte
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* millisegundos */);
            urlConnection.setConnectTimeout(15000 /* millisegundos */);
            urlConnection.setRequestMethod("GET"); //No son necesarias, ya están puestas de serie
            urlConnection.setDoInput(true);
            urlConnection.connect(); // Establecer la conexión

            //URL en cuestion
            Log.d("URL", String.valueOf(urlConnection.getResponseCode()));

            try (InputStream is = urlConnection.getInputStream()) {
                // Leer el contenido del InputStream
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String jsonResponse = response.toString(); // Convertir StringBuilder a String
                result = BookInfo.fromJsonResponse(jsonResponse);


            } finally {
                urlConnection.disconnect(); // Desconectar la conexión
                Log.d("URL", "Conexión desconectada");
            }
        } catch (Exception e) {
            // Manejar la excepción
            e.printStackTrace(); // o cualquier otro manejo de excepciones
        }
    }

    // completa correctamente la URL
    private URL completeURL(String queryString, String printType) throws IOException{
        // constantes referentes a la construcción de la URL
        String URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=";
        String PRINT_TYPE = "&printType=";
        String KEY = "&key=";
        //Utilizacion de ficheros privados para proteger la clave
        String apiKey = BuildConfig.BOOKS_API_KEY;

        URL url;

        // se completa correctamente la URL dependiendo de lo seleccionado por el usuario
        if(Objects.equals(printType, "Revista")){
            url = new URL(URL_BASE + queryString + PRINT_TYPE + "magazines"+ KEY + apiKey);
        }
        else if(Objects.equals(printType, "Libro")) {
            url = new URL(URL_BASE + queryString + PRINT_TYPE + "books"+ KEY + apiKey);
        }
        else{
            url = new URL(URL_BASE + queryString + KEY + apiKey);
        }
        Log.i("URL", url.toString());

        return url;
    }
}
