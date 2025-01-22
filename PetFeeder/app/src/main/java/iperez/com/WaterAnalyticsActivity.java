package iperez.com;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class WaterAnalyticsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_wateranalytics);

        // Primera gráfica: Comida consumida por hora
        BarChart graph1 = new BarChart(this);
        ((FrameLayout) findViewById(R.id.graphwater1_container)).addView(graph1);

        ArrayList<BarEntry> data1 = new ArrayList<>();
        data1.add(new BarEntry(0, 50)); // Añadir datos en la gráfica.
        data1.add(new BarEntry(1, 70));
        data1.add(new BarEntry(2, 70));
        data1.add(new BarEntry(3, 70));
        data1.add(new BarEntry(4, 70));
        data1.add(new BarEntry(5, 70));
        data1.add(new BarEntry(6, 70));
        BarDataSet dataSet1 = new BarDataSet(data1, "Liters per hour");
        dataSet1.setColor(ContextCompat.getColor(this, R.color.blue));
        graph1.setData(new BarData(dataSet1));

        // Se personaliza el eje X con las iniciales de los días
        graph1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[] {"L", "M", "X", "J", "V", "S", "D"}));
        graph1.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        // Se refesca la gráfica para que se muestre correctamente
        graph1.invalidate();  // Esto redibuja la gráfica


        // Segunda gráfica: Veces activado por día
        BarChart graph2 = new BarChart(this);
        ((FrameLayout) findViewById(R.id.graphwater2_container)).addView(graph2);

        ArrayList<BarEntry> data2 = new ArrayList<>();
        data2.add(new BarEntry(0, 5)); // // Añadir datos en la segunda gráfica.
        data2.add(new BarEntry(1, 3));
        data2.add(new BarEntry(2, 70));
        data2.add(new BarEntry(3, 15));
        data2.add(new BarEntry(4, 54));
        data2.add(new BarEntry(5, 23));
        data2.add(new BarEntry(6, 12));
        BarDataSet dataSet2 = new BarDataSet(data2, "Activations per day");
        dataSet2.setColor(ContextCompat.getColor(this, R.color.blue));
        graph2.setData(new BarData(dataSet2));

        // Personalizar el eje X con las iniciales de los días
        graph2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[] {"L", "M", "X", "J", "V", "S", "D"}));
        graph2.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        // Refrescar la gráfica para que se muestre correctamente
        graph2.invalidate();  // Esto redibuja la gráfica

    }
}