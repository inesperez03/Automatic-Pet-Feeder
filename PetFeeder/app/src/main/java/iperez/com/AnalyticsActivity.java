package iperez.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AnalyticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);    //Vinculación con el UI

        Button food = findViewById(R.id.food_analytics);    //Definición de botones
        Button water = findViewById(R.id.water_analytics);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);       // Vincula la barra de navegación inferior del diseño

        bottomNavigationView.setSelectedItemId(R.id.bottom_analytics);

        //Botones para desplazarse a las actividades de analiticas de comida y agua

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveData();
                Intent intent = new Intent(AnalyticsActivity.this, FoodAnalyticsActivity.class);
                startActivity(intent);
            }
        });

        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveData();
                Intent intent = new Intent(AnalyticsActivity.this, WaterAnalyticsActivity.class);
                startActivity(intent);
            }
        });

        //Barra inferior de menú.

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.bottom_pets:
                    startActivity(new Intent(getApplicationContext(), PetsActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.bottom_analytics:
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }
}