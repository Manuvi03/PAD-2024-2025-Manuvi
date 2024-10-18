package es.ucm.fdi.gogglebooksclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private static final int BOOK_LOADER_ID = 1;
    private String queryString;
    private String printType;
    private EditText editText;
    private RadioButton selectedButton;

    private BookLoaderCallbacks bookLoaderCallbacks = new BookLoaderCallbacks(this);
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

        //mediante el metodo de radiogroup que devuelve el radioButton que se acaba de checkear cambiamos el atributo boton seleccionado
        // a ese boton
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedButton = findViewById(checkedId);
            }
        });

        editText = findViewById(R.id.editText);
        //listener del boton de busqueda que al realizar el click al boton se llama al metodo searchbooks
        ImageButton busqueda = findViewById(R.id.imageButton);
        busqueda.setOnClickListener(v -> new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = editText.getText().toString();
                    //comprobamos que el usuario haya introducido texto y mandamos un mensaje toast si no lo ha hecho
                    if(text == null && text.isBlank())
                    {
                        CharSequence msg = getResources().getString(R.string.error_text);
                        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();
                    }
                    else {
                        queryString = text;
                        printType = selectedButton.getText().toString();
                        searchBooks(v);
                    }

                }
        });

    }



    public void searchBooks (View view){
        Bundle queryBundle = new Bundle();
        queryBundle.putString(BookLoaderCallbacks.EXTRA_QUERY, queryString);
        queryBundle.putString(BookLoaderCallbacks.EXTRA_PRINT_TYPE, printType);
        LoaderManager.getInstance(this).restartLoader(BOOK_LOADER_ID,
                queryBundle, bookLoaderCallbacks);
    }
}