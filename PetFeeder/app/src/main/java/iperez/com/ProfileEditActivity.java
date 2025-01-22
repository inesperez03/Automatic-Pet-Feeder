package iperez.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

//Actividad de los ajustes de perfil

public class ProfileEditActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;
    EditText uploadName, uploadCountry, uploadYears, uploadLabSituation;
    Spinner uploadGender;
    String imageURL;
    Uri uri;
    MyApp app;

    @SuppressLint({"WrongViewCast", "CutPasteId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profilesettings);

        app = (MyApp) getApplication();

        uploadImage = findViewById(R.id.uploadProfileImage);
        uploadGender = findViewById(R.id.profile_spinner);
        uploadCountry = findViewById(R.id.uploadCountry);
        uploadName = findViewById(R.id.uploadProfileName);
        uploadYears = findViewById(R.id.uploadProfileYears);
        uploadLabSituation = findViewById(R.id.uploadLabSituation);
        saveButton = findViewById(R.id.saveProfileButton);

        //Creación del desplegable de sexos
        String[] options = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uploadGender.setAdapter(adapter);

        fillProfileData();      //Función de rellenado de datos guardados

        //Actividad en la que se escoge la imagen de perfil
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            uploadImage.setImageURI(uri);   //Se asigna la imagen resultante en la interfaz
                        } else {
                            Toast.makeText(ProfileEditActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //Guardado en la variable global de los datos de perfil
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = uploadName.getText().toString();
                String gender = uploadGender.getSelectedItem().toString();
                String country = uploadCountry.getText().toString();
                String years = uploadYears.getText().toString();
                String labsituation = uploadLabSituation.getText().toString();


                String[] datos = {name, gender, country, years, labsituation};

                app.setProfileData(datos);

                Toast.makeText(ProfileEditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Rellenado de todos los datos desde las variables globales
    public void fillProfileData(){

        String[] data = app.getProfileData();

        if (Objects.nonNull(data)){
            uploadName.setText(data[0]);
            uploadCountry.setText(data[2]);
            uploadYears.setText(data[3]);
            uploadLabSituation.setText(data[4]);

            String gender = data[1];

            if (gender.equals("Male")) {
                uploadGender.setSelection(0);
            } else if (gender.equals("Female")) {
                uploadGender.setSelection(1);
            } else if (gender.equals("Other")) {
                uploadGender.setSelection(2);
            }
        }
    }
}