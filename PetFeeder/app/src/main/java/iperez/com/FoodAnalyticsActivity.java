package iperez.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

public class FoodAnalyticsActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;
    EditText uploadName, uploadCountry, uploadYears, uploadLabSituation;
    Spinner uploadGender;
    String imageURL;
    Uri uri;

    @SuppressLint({"WrongViewCast", "CutPasteId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_foodanalytics);

        // Primera gráfica: Comida consumida por hora
        BarChart graph1 = new BarChart(this);       //Declaración de la gráfica.
        ((FrameLayout) findViewById(R.id.graphfood1_container)).addView(graph1);

        ArrayList<BarEntry> data1 = new ArrayList<>();
        data1.add(new BarEntry(0, 50)); // Añadir datos en la gráfica.
        data1.add(new BarEntry(1, 70));
        data1.add(new BarEntry(2, 70));
        data1.add(new BarEntry(3, 70));
        data1.add(new BarEntry(4, 70));
        data1.add(new BarEntry(5, 70));
        data1.add(new BarEntry(6, 70));
        BarDataSet dataSet1 = new BarDataSet(data1, "Grams per day");
        dataSet1.setColor(ContextCompat.getColor(this, R.color.brown));
        graph1.setData(new BarData(dataSet1));

        // Se personaliza el eje X con las iniciales de los días
        graph1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[] {"L", "M", "X", "J", "V", "S", "D"}));
        graph1.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        // Se refesca la gráfica para que se muestre correctamente
        graph1.invalidate();

        // Segunda gráfica: Veces activado por día
        BarChart graph2 = new BarChart(this);
        ((FrameLayout) findViewById(R.id.graphfood2_container)).addView(graph2);

        ArrayList<BarEntry> data2 = new ArrayList<>();
        data2.add(new BarEntry(0, 5)); // Añadir datos de la segunda gráfica
        data2.add(new BarEntry(1, 3));
        data2.add(new BarEntry(2, 70));
        data2.add(new BarEntry(3, 70));
        data2.add(new BarEntry(4, 70));
        data2.add(new BarEntry(5, 70));
        data2.add(new BarEntry(6, 70));
        BarDataSet dataSet2 = new BarDataSet(data2, "Activations per day");
        dataSet2.setColor(ContextCompat.getColor(this, R.color.brown));
        graph2.setData(new BarData(dataSet2));

        graph2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[] {"L", "M", "X", "J", "V", "S", "D"}));
        graph2.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        graph2.invalidate();  // Esto redibuja la gráfica
    }
}