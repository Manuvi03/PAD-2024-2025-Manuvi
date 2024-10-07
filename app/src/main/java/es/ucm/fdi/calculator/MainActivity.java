package es.ucm.fdi.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Calculator calculator;
    private EditText editTextX;
    private EditText editTextY;
    private EditText currentText;

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
        calculator = new Calculator();
        editTextX = findViewById(R.id.editTextX_view);
        editTextY = findViewById(R.id.editTextY_view);
        currentText = editTextX;
        setNumButtonListeners();
        setSpecialButtonListeners();
    }

    public void addXandY(View view)
    {
        Log.i(TAG,"Llega a la funcion addXandY");
        Intent intent = new Intent(this, CalculatorResultActivity.class);
        intent.putExtra("solucion",calculator.suma(Double.parseDouble(editTextX.getText().toString()),
                Double.parseDouble(editTextY.getText().toString())));
        startActivity(intent);
    }

    private void setNumButtonListeners(){
        int[] buttonIds = {
                R.id.button, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button7,
                R.id.button8, R.id.button9, R.id.button10, R.id.button13
        };
        Log.i(TAG,"Llega a la funcion setNumButtonListeners");
        //para no tener que hacer 9 listeners iguales declaro un listener que coge el texto del botton y lo escribe en el texto actual seleccionado
        //luego se hace un for con los ids de los botones a los que hay que poner el listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                String buttonText = clickedButton.getText().toString();
                currentText.append(buttonText);
            }
        };

        // Attach the listener to each button
        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setSpecialButtonListeners(){
        Log.i(TAG,"Llega a la funcion setSpecialButtonListeners");
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentText == editTextX)
                {
                    currentText = editTextY;
                }
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextX.getText().toString().isEmpty() && !editTextY.getText().toString().isEmpty())
                {
                    addXandY(view);
                }
            }
        });

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentText.getText().toString().contains(".") && !currentText.getText().toString().isEmpty())
                {
                    currentText.append(".");
                }
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextY.setText("");
                editTextX.setText("");
                currentText = editTextX;
            }
        });
    }
}