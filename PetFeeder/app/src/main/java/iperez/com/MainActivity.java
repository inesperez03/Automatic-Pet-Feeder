package iperez.com;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private String cant_comida = " ";           //Valores de los datos de sensores a mostrar por pantalla
    private String cant_agua = " ";

    private MyApp app;                          //Nuestra app

    private TextView textActualAmount;
    private TextView textActualWaterAmount;
    private TextView textActualAmountLabel;
    private TextView textDaiObjFood;
    private TextView textDaiObjWater;
    private TextView connectionStatus;

    private Button addWater;
    private Button addFood;

    private BluetoothAdapter bluetoothAdapter;
    private static final String DEVICE_NAME = "JDY-31-SPP";
    private static final UUID UUID_BT = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);    //barra inferior
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();        //Adaptador de Bluetooth del teléfono.

        this.textActualAmount = findViewById(R.id.textActualAmount);
        this.textActualWaterAmount = findViewById(R.id.textActualWaterAmount);
        this.textActualAmountLabel = findViewById(R.id.textActualAmountLabel);
        this.connectionStatus = findViewById(R.id.connectionStatus);
        this.textDaiObjFood = findViewById(R.id.textDaiObjFood);
        this.textDaiObjWater = findViewById(R.id.textDaiObjWater);
        this.addFood = findViewById(R.id.addFood);
        this.addWater = findViewById(R.id.addWater);

        app = (MyApp) getApplication();         //Instancia a la app misma.

        // Intenta conectar solo si el flag connect está activo
        if (app.getConnect()) {
            if (app.getBluetoothSocket() == null || !app.getBluetoothSocket().isConnected()) {      //Si no existe socket ya creado
                connectToBluetoothDevice();
            } else {
                //Envía mensaje de actualización de parámetros hacia el arduino.
                sendDataToBluetoothDevice("c" + "," + app.getWater() + "," + app.getFood() + "," + app.getAutofillState(), null);
                startReadingData();
            }
        } else {
            connectionStatus.setText("Connection NOT Active");
            desconectarBT();
        }

        //Envío de instrucciones manuales del comedero

        addWater.setOnClickListener(view -> sendDataToBluetoothDevice("w", "50ml of water dropped"));

        addFood.setOnClickListener(view -> sendDataToBluetoothDevice("f", "15g of food dropped"));

        //Barra de menú inferior

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_pets:
                    navigateToActivity(PetsActivity.class);
                    return true;
                case R.id.bottom_analytics:
                    navigateToActivity(AnalyticsActivity.class);
                    return true;
                case R.id.bottom_settings:
                    navigateToActivity(SettingsActivity.class);
                    return true;
            }
            return false;
        });

        checkBTPermissions();
    }

    //Función para la conexión bluetooth

    private void connectToBluetoothDevice() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {    //Comprueba si el Bluetooth está activo
            connectionStatus.setText("Bluetooth Disabled");
            return;
        }

        new Thread(() -> {
            int attempts = 0;
            BluetoothDevice device = null;

            while (attempts < 5 && device == null) { // Intenta hasta 5 veces encontrar el dispositivo entre tus vinculados
                for (BluetoothDevice d : bluetoothAdapter.getBondedDevices()) {
                    if (DEVICE_NAME.equals(d.getName())) {
                        device = d;
                        break;
                    }
                }

                if (device == null) {
                    runOnUiThread(() -> connectionStatus.setText("Device not found, retrying..."));
                    attempts++;
                    try {
                        Thread.sleep(2000); // Espera 2 segundos antes de reintentar
                    } catch (InterruptedException e) {
                        Log.e("BluetoothError", "Reintento interrumpido: ", e);
                        return;
                    }
                }
            }

            if (device == null) {
                runOnUiThread(() -> connectionStatus.setText("Device NOT found after retries"));
                return;
            }

            try {
                //Crea el socket de la conexión y se conecta.
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID_BT);
                socket.connect();

                app.setBluetoothSocket(socket);
                runOnUiThread(() -> connectionStatus.setText("Connected"));
                //Envía mensaje de actualización de parámetros hacia el arduino.
                sendDataToBluetoothDevice("c" + "," + app.getWater() + "," + app.getFood() + "," + app.getAutofillState(), null);
                startReadingData(); //Empieza a detectar los mensajes que le llegan.
            } catch (Exception e) {
                Log.e("Error al conectar", String.valueOf(e));
                runOnUiThread(() -> connectionStatus.setText("Connection Error"));
            }
        }).start();
    }

    //Función de envío de mensajes por Bluetooth

    private void sendDataToBluetoothDevice(String data, String successMessage) {
        try {
            BluetoothSocket socket = app.getBluetoothSocket();  //Obtiene el socket desde las variables globales.
            if (socket != null && socket.isConnected()) {
                socket.getOutputStream().write(data.getBytes());    //Escribe en el OutputStream la string a mandar.
                if (successMessage != null) {
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Dispositivo no conectado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("BluetoothError", "Error al enviar datos: ", e);
        }
    }

    //Función de recepción de mensajes por Bluetooth

    private void startReadingData() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            int bytes;
            connectionStatus.setText("Connected");

            while (true) {
                try {
                    InputStream inputStream = app.getBluetoothSocket().getInputStream();            //Guarda el imputstream del socket.
                    if (inputStream != null && (bytes = inputStream.read(buffer)) > 0) {            //Comprueba que no está vacio
                        final String receivedData = new String(buffer, 0, bytes).trim();      //Convierte en string el mensaje recibido.
                        final String[] values = receivedData.split(",");

                        if (values.length == 4) {
                            runOnUiThread(() -> {
                                try {
                                    cant_comida = values[1] + "g";
                                    cant_agua = values[0] + "ml";

                                    //Mostrar gráficamente los datos recibidos por Bluetooth

                                    if (Objects.equals(values[3], "1")){
                                        textDaiObjFood.setText("Daily objective ✔");
                                        textDaiObjFood.setTextColor(ContextCompat.getColor(this, R.color.green));
                                    }
                                    else{
                                        textDaiObjFood.setText("Daily objective X");
                                        textDaiObjFood.setTextColor(ContextCompat.getColor(this, R.color.red));
                                    }
                                    if (Objects.equals(values[2], "1")){
                                        textDaiObjWater.setText("Daily objective ✔");
                                        textDaiObjWater.setTextColor(ContextCompat.getColor(this, R.color.green));
                                    }
                                    else{
                                        textDaiObjWater.setText("Daily objective X");
                                        textDaiObjWater.setTextColor(ContextCompat.getColor(this, R.color.red));
                                    }
                                    textActualAmount.setText(cant_comida.trim());
                                    textActualWaterAmount.setText(cant_agua.trim());
                                } catch (Exception e) {
                                    Log.e("BluetoothError", "Error al procesar datos: ", e);
                                }
                            });
                        }
                        Log.d("BluetoothData", "Datos recibidos: " + receivedData);
                    }
                } catch (Exception e) {
                    Log.e("BluetoothError", "Error al leer datos: ", e);
                    break;
                }
            }
        }).start();
    }

    //Para hacer funcionar el cambio entre actividades.

    private void navigateToActivity(Class<?> targetActivity) {
        startActivity(new Intent(getApplicationContext(), targetActivity));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    //Para desconectar el socket.

    private void desconectarBT() {
        if (!isChangingConfigurations() && app.getBluetoothSocket() != null) {
            app.closeBluetoothConnection();
        }
    }

    //Comprobación básica de permisos de conexión Bluetooth

    public void checkBTPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Para Android 12 (API 31) y superior, necesitamos BLUETOOTH_CONNECT y BLUETOOTH_SCAN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.BLUETOOTH_SCAN}, 1002);
                }
            }
        }
    }
}
