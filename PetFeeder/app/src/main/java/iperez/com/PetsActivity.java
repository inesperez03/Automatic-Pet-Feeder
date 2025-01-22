package iperez.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import iperez.com.databinding.ActivityPetsBinding;

import java.util.ArrayList;
import java.util.List;

public class PetsActivity extends AppCompatActivity {

    Double total_agua = 0.0;
    Double total_comida = 0.0;
    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        fab = findViewById(R.id.fab);                       //Botón flotante para agregar animal
        recyclerView = findViewById(R.id.recyclerView);     //La lista de animales

        app = (MyApp) getApplication();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(PetsActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        //El mensaje de "cargando" para el listado.

        AlertDialog.Builder builder = new AlertDialog.Builder(PetsActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();      //El datalist en el que se irá metiendo los datos de las mascotas

        adapter = new MyAdapter(PetsActivity.this, dataList);       //El adapter sobre el que se plasmará la datalist
        recyclerView.setAdapter(adapter);

        //La barra inferior de menú
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_pets);

        //La creación de la lista con los datos del firebase.

        databaseReference = FirebaseDatabase.getInstance().getReference("Animales");
        dialog.show();

        //Reacciona cada vez que cambien los datos del nodo Animales (o cuando se cargan por primera vez)
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                total_agua = 0.0;
                total_comida = 0.0;

                //Por cada hijo incluido en el nodo Animales
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());

                    //Calcula e introduce en la variable global la suma del alimento necesario en total por todos los animales
                    total_agua = total_agua + Float.parseFloat(dataClass.getDataWater());
                    total_comida = total_comida + Float.parseFloat(dataClass.getDataFood());
                    app.setWater(total_agua);
                    app.setFood(total_comida);
                    dataList.add(dataClass);    //Añade en la datalist del adapter un nuevo animal
                }
                adapter.notifyDataSetChanged(); //Notifica al adapter que tiene que actualizar la lista
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        //Botón de añadir nuevo animal

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetsActivity.this, UploadActivity.class);
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
                    return true;
                case R.id.bottom_analytics:
                    startActivity(new Intent(getApplicationContext(), AnalyticsActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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