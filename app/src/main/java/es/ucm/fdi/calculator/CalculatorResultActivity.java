package es.ucm.fdi.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorResultActivity extends AppCompatActivity {
    private TextView solution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        solution = findViewById(R.id.textView_result);
        mostarResultado();
    }

    protected void mostarResultado()
    {
        Intent intent = getIntent();
        //cogemos el valor del intent que es double lo pasamos a string y se lo ponemos al textView
        solution.setText(String.valueOf(intent.getDoubleExtra("solucion",0)));
    }
}