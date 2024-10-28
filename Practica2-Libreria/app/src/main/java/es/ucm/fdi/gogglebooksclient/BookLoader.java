package es.ucm.fdi.gogglebooksclient;

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

    // constantes referentes a la construcción de la URL
    private final String URL_BASE = "https://www.googleapis.com/books/v1/volumes?q=";
    private final String PRINT_TYPE = "&printType=";
    private final String KEY = "&key=";

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
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET"); //No son necesarias, ya están puestas de serie
                urlConnection.setDoInput(true);
                urlConnection.connect(); // Establecer la conexión


                Log.d("URL", String.valueOf(urlConnection.getResponseCode()));

                InputStream is = null;

                try {
                    // Leer el contenido del InputStream
                    is = urlConnection.getInputStream();
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
                    if(is != null)
                    {
                        is.close();
                    }
                    Log.d("URL", "Conexión desconectada");
                }
            } catch (Exception e) {
                // Manejar la excepción
                e.printStackTrace(); // o cualquier otro manejo de excepciones
            }
    }
    /*
    private void getBookInfoJson(String queryString, String printType) throws IOException {

        // primero se completa la URL de la conexión
        URL url = completeURL(queryString, printType);

        // y trás ello se realiza la conexión, en un hilo independiente, obteniendo el resultado
        ConnectionThread connectionThread = new ConnectionThread(url);
        connectionThread.start();
        Log.i("Resultado", result.toString());
    }*/

    // completa correctamente la URL
    private URL completeURL(String queryString, String printType) throws IOException{
        URL url;

        // se completa correctamente la URL dependiendo de lo seleccionado por el usuario
        if(Objects.equals(printType, "Revista")){
            url = new URL(URL_BASE + queryString + PRINT_TYPE + "magazines"+ KEY + "AIzaSyCgcmZj01An4CkRZicIA2EzQrk-bGTL9qU");
        }
        else if(Objects.equals(printType, "Libro")) {
            url = new URL(URL_BASE + queryString + PRINT_TYPE + "books"+ KEY + "AIzaSyCgcmZj01An4CkRZicIA2EzQrk-bGTL9qU");
        }
        else{
            url = new URL(URL_BASE + queryString + KEY + "AIzaSyCgcmZj01An4CkRZicIA2EzQrk-bGTL9qU");
        }
        Log.i("URL", url.toString());

        return url;
    }

    // hilo que realiza la conexión
    class ConnectionThread extends Thread {

        // URL
        private final URL url;

        // constructora
        public ConnectionThread(URL url) {
            this.url = url;
            start();
        }

        // run, realiza la conexión
        @Override
        public void run() {
            try {
                Log.d("Connection Thread", "Thread ejecutando.");

                // se obtiene, se configura y se conecta la conexión a la URL
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                Log.d("URL", "Conexión conectada");

                try {
                    // se lee el contenido del input stream y se guarda en jsonResponse
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    String jsonResponse = response.toString(); // convierte el StringBuilder a String
                    result = BookInfo.fromJsonResponse(jsonResponse);
                } finally {
                    // siempre se debe cerrar la conexión
                    urlConnection.disconnect();
                    Log.d("URL", "Conexión desconectada");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Log.d("Connection Thread", "Thread ha terminado de ejecutar.");
        }
    }
}
