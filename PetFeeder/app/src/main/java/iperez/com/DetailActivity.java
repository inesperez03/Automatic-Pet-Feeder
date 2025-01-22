package iperez.com;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DetailActivity extends AppCompatActivity {

    TextView detailName, detailAnimal, detailWeight, detailYears, detailMonths;
    TextView detailFood, detailWater;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailName = findViewById(R.id.detailName);
        detailImage = findViewById(R.id.detailImage);
        detailAnimal = findViewById(R.id.detailAnimal);
        detailWeight = findViewById(R.id.detailWeight);
        detailYears = findViewById(R.id.detailYears);
        detailMonths = findViewById(R.id.detailMonths);
        detailFood = findViewById(R.id.detailFood);
        detailWater = findViewById(R.id.detailWater);

        Bundle bundle = getIntent().getExtras();    //Se introducen los datos de las pets con los extras
        if (bundle != null) {
            detailName.setText(bundle.getString("name"));
            detailAnimal.setText(bundle.getString("animal"));
            detailWeight.setText(bundle.getString("weight")+ " kg");
            detailYears.setText(bundle.getString("years") + " years");
            detailMonths.setText(bundle.getString("months") + " months");
            detailFood.setText(bundle.getString("food") + " g/day");
            detailWater.setText(bundle.getString("water") + " ml/day");
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

    }
}