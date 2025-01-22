package iperez.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

//Actividad principal de los ajustes generales

public class SettingsActivity extends AppCompatActivity {

    Button profilesettings;
    Button connect;
    Button disconnect;
    Switch autorefill;
    MyApp app;
    TextView connection_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profilesettings = findViewById(R.id.btn_profile_settings);
        autorefill = findViewById(R.id.switch_auto_refill);
        connect = findViewById(R.id.btn_connect);
        disconnect = findViewById(R.id.btn_disconnect);
        connection_state = findViewById(R.id.connection_buttons_state);
        app = (MyApp) getApplication();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_settings);

        setConnectStatText();       //Establece el texto del estado actual de la conexión

        //Botón para entrar en los ajustes de perfil
        profilesettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        //Establece el estado del Switch autorefill tomando en cuenta las variables globales
        autorefill.setChecked(app.getAutofillState());

        //Al pulsar el Switch del autorefill
        autorefill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.setAutofillState(autorefill.isChecked());
            }
        });

        //Al pulsar el botón connect
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.setConnect(true);
                setConnectStatText();
            }
        });

        //Al pulsar el botón disconnect
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                app.setConnect(false);
                setConnectStatText();
            }
        });


        //Barra de menú inferior
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
                    startActivity(new Intent(getApplicationContext(), AnalyticsActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;
                case R.id.bottom_settings:
                    return true;
            }
            return false;
        });
    }

    //Establece el texto del estado actual de la conexión
    public void setConnectStatText(){
        if (app.getConnect()) {
            connection_state.setText("Connected");
            connection_state.setTextColor(ContextCompat.getColor(this, R.color.green));
        }else{
            connection_state.setText("Not Connected");
            connection_state.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
    }
}