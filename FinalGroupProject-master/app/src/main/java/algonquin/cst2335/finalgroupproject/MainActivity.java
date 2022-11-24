package algonquin.cst2335.finalgroupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button pexelsButton, dictionaryButton, covidButton, co2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pexelsButton = findViewById(R.id.pexels);
        dictionaryButton = findViewById(R.id.dictionaryButton);
        covidButton = findViewById(R.id.covid);
        co2Button = findViewById(R.id.co2);

        // loading the activities from the button
        covidButton.setOnClickListener(click -> {
            startActivity(new Intent(MainActivity.this, CovidActivity.class));
        });

        co2Button.setOnClickListener(click -> {
            startActivity(new Intent(MainActivity.this, Co2Activity.class));
        });
        //loads to owlbot activity
        dictionaryButton.setOnClickListener(click -> {
            startActivity(new Intent(MainActivity.this, OwlbotActivity.class));
        });
    }
}