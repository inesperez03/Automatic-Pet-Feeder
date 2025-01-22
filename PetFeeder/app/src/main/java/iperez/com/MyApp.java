package iperez.com;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

public class MyApp extends Application {
    private boolean autofill_state = false;
    private String[] profile_data;
    private boolean connectbt = false;
    public static Double total_agua = 0.0;
    public static Double total_comida = 0.0;

    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    // Para el modo automático
    public boolean getAutofillState() {
        return autofill_state;
    }
    public void setAutofillState(boolean autofill_state) {
        this.autofill_state = autofill_state;
    }

    // Para los datos de perfil
    public String[] getProfileData() {
        return profile_data;
    }
    public void setProfileData(String[] profile_data) {
        this.profile_data = profile_data;
    }

    // Para el indicador de conexión Bluetooth
    public boolean getConnect() {
        return connectbt;
    }
    public void setConnect(boolean connectbt) {
        this.connectbt = connectbt;
    }

    //Para la comida total
    public Double getFood() {
        return total_comida;
    }
    public void setFood(Double total_comida) {
        this.total_comida = total_comida;
    }

    //Para la bebida total
    public Double getWater() {
        return total_agua;
    }
    public void setWater(Double total_agua) {
        this.total_agua = total_agua;
    }

    // Métodos para gestionar el Bluetooth Socket
    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }
    public void setBluetoothSocket(BluetoothSocket socket) {
        this.bluetoothSocket = socket;
        try {
            if (socket != null && socket.isConnected()) {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                connectbt = true;
            }
        } catch (Exception e) {
            Log.e("BluetoothError", "Error al configurar streams: ", e);
            connectbt = false;
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void closeBluetoothConnection() {
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                bluetoothSocket = null;
                inputStream = null;
                outputStream = null;
                connectbt = false;
            }
        } catch (Exception e) {
            Log.e("BluetoothError", "Error al cerrar conexión: ", e);
        }
    }
}
