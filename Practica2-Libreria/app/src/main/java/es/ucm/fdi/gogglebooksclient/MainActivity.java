package es.ucm.fdi.gogglebooksclient;

import android.content.Context;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.app.LoaderManager;

import android.net.ConnectivityManager;

public class MainActivity extends AppCompatActivity {

    private static final int BOOK_LOADER_ID = 1;
    private String queryString;
    private String printType;
    private EditText editTextTitulo;
    private EditText editTextAutor;
    private RadioButton selectedButton;

    private final BookLoaderCallbacks bookLoaderCallbacks = new BookLoaderCallbacks(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        if (loaderManager.getLoader(BOOK_LOADER_ID) != null) {
            loaderManager.initLoader(BOOK_LOADER_ID, null, bookLoaderCallbacks);
        }

        RadioGroup radiogroup = findViewById(R.id.radioGroup);
        selectedButton = findViewById(R.id.radioButtonLibro);
        queryString = "";

        //mediante el metodo de radiogroup que devuelve el radioButton que se acaba de checkear cambiamos el atributo boton seleccionado
        // a ese boton
        radiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            selectedButton = findViewById(checkedId);
            //Restringimos los editText para meter autor o titulo dependiendo del radioGroup que este seleccionado
            if (selectedButton == findViewById(R.id.radioButtonRevista)) {
                editTextAutor.setVisibility(View.GONE);
                editTextAutor.setText("");
                editTextAutor.setEnabled(false);
            } else {
                editTextAutor.setVisibility(View.VISIBLE);
                editTextAutor.setEnabled(true);
            }
        });

        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextAutor = findViewById(R.id.editTextAutor);
        //listener del boton de busqueda que al realizar el click al boton se llama al metodo searchbooks
        // Listener del botón de búsqueda que, al hacer clic, llama al método searchBooks
        ImageButton busqueda = findViewById(R.id.imageButton);
        busqueda.setOnClickListener(v -> {
            // la búsqueda se reaiza solo si hay conexión a Internet
            if (isConnected()) {
                printType = selectedButton.getText().toString();
                Log.i("printType", printType);

                // Modificamos la queryString dependiendo del botón seleccionado
                if (selectedButton == findViewById(R.id.radioButtonRevista)) {//Revista
                    if (editTextTitulo.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, R.string.error_falta_titulo_revista, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    queryString = "intitle:" + editTextTitulo.getText().toString();
                } else { //Libro o ambas
                    // Comprobamos que los campos no estén vacíos
                    Log.i(this.getClass().getName(), "Entro a libros");
                    if (editTextTitulo.getText().toString().isEmpty() && editTextAutor.getText().toString().isEmpty()) {
                        Log.i(this.getClass().getName(), "Entro a libros");
                        Toast.makeText(MainActivity.this, R.string.error_text, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!editTextTitulo.getText().toString().isEmpty()) {
                        queryString = "intitle:" + editTextTitulo.getText().toString();
                    }
                    if (!editTextAutor.getText().toString().isEmpty()) {
                        if (!queryString.isEmpty()) {
                            queryString += "+inauthor:" + editTextAutor.getText().toString();
                        } else {
                            queryString = "inauthor:" + editTextAutor.getText().toString();

                        }
                    }
                }
                //Llamamos al método searchBooks
                searchBooks(v);
                queryString = "";
            } else {
                Toast.makeText(MainActivity.this, R.string.error_sin_conexion_busqueda, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void searchBooks(View view) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(BookLoaderCallbacks.EXTRA_QUERY, queryString);
        queryBundle.putString(BookLoaderCallbacks.EXTRA_PRINT_TYPE, printType);
        LoaderManager.getInstance(this).restartLoader(BOOK_LOADER_ID,
                queryBundle, bookLoaderCallbacks);
    }

    // se comprueba si hay conexion a Internet para poder realizar la busqueda
    private boolean isConnected() {
        Log.i(this.getClass().getName(), "Se comprueba la conexión a Internet antes de realizar la búsqueda.");
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = connMgr.getActiveNetwork();
        NetworkCapabilities networkCap =
                connMgr.getNetworkCapabilities(network);
        return (networkCap != null);
    }
}
