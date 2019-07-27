package com.apps.eureka.eurekapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ActualizarServicioActivity extends AppCompatActivity {

    //region Inicialización
    private EditText etNom, etApe, etNumContact, etDescrip;
    private Spinner spCatServicio;
    private Button btnAct;
    private ProgressDialog progresoDialog;
    String nombre, apellido, telef, correo, numDoc, contra, uid, descrip, categoria;

    //Declaración de obj Auth(Autentificación)
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_servicio);

        //Inicializando el obj Auth
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //region Referencias
        etNom = (EditText) findViewById(R.id.etNombreServicio);
        etApe = (EditText) findViewById(R.id.etApellidosServicio);
        etNumContact = (EditText) findViewById(R.id.etTelefonoServicio);
        etDescrip = (EditText) findViewById(R.id.etDescripcionServicio);
        spCatServicio = (Spinner) findViewById(R.id.spCategoriaServicio);

        btnAct = (Button) findViewById(R.id.btnActualizarServicio);

        //Inicialización del mensaje de progreso
        progresoDialog = new ProgressDialog(this);

        Toolbar toolbarServicio = findViewById(R.id.toolbarServicio);
        getSupportActionBar();
        setSupportActionBar(toolbarServicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        final String id = myAuth.getCurrentUser().getUid();
        mDatabase.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nombre = dataSnapshot.child("nombre").getValue().toString();
                    apellido = dataSnapshot.child("apellidos").getValue().toString();
                    telef = dataSnapshot.child("numContact").getValue().toString();
                    correo = dataSnapshot.child("correo").getValue().toString();
                    categoria = dataSnapshot.child("especialidad").getValue().toString();
                    descrip = dataSnapshot.child("descripServicio").getValue().toString();
                    numDoc = dataSnapshot.child("numDoc").getValue().toString();
                    contra = dataSnapshot.child("pass").getValue().toString();
                    uid = dataSnapshot.child("uid").getValue().toString();

                    //Array para la lista que se mostrará en el spinner
                    String [] categorias = {categoria, "Servicios de Albañería",
                            "Servicios de Asesoria Contable y Tributaria",
                            "Servicios de Carpintería", "Servicios de Electricista", "Servicios de Enfermeria",
                            "Servicios de Gasfitería", "Servicios de Jardinería", "Servicios de Tintorería", "Servicios de Traslado",
                            "Servicios de Zapatería", "Servicios Domesticos Generales", "Servicios Gastronomicos", "Servicios Mecanicos",
                            "Otros (Indicar en la Descripción)"};
                    //Adaptador que generará la lista con los datos
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(ActualizarServicioActivity.this,
                            android.R.layout.simple_spinner_item, categorias);
                    spCatServicio.setAdapter(spinnerAdapter);

                    etNom.setText(nombre);
                    etApe.setText(apellido);
                    etNumContact.setText(telef);
                    etDescrip.setText(descrip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActualizarServicioActivity.this, "Error no se lograron obtener los datos"
                        ,Toast.LENGTH_SHORT).show();
            }
        });

        btnAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region Obtención de datos
                nombre = etNom.getText().toString().trim();
                apellido = etApe.getText().toString().trim();
                telef = etNumContact.getText().toString().trim();
                categoria = spCatServicio.getSelectedItem().toString();
                descrip = etDescrip.getText().toString();
                //endregion

                if (TextUtils.isEmpty(nombre)){
                    etNom.setError("Debe ingresar un nombre");
                    Toast.makeText(ActualizarServicioActivity.this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
                }
                if (TextUtils.isEmpty(apellido)){
                    etApe.setError("Debe ingresar un apellido");
                    Toast.makeText(ActualizarServicioActivity.this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
                }
                if (TextUtils.isEmpty(telef)){
                    etNumContact.setError("Debe ingresar un numero de contacto");
                    Toast.makeText(ActualizarServicioActivity.this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show();return;
                }
                if (TextUtils.isEmpty(descrip)) {
                    etDescrip.setError("Debe ingresar una descripción del servicio");
                    Toast.makeText(ActualizarServicioActivity.this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
                }

                //Mensaje de carga
                progresoDialog.setMessage("Actualizando datos...");
                progresoDialog.show();

                Map<String, Object> servicio = new HashMap<>();
                servicio.put("nombre", nombre);
                servicio.put("apellidos", apellido);
                servicio.put("numDoc", numDoc);
                servicio.put("especialidad", categoria);
                servicio.put("numContact", telef);
                servicio.put("correo", correo);
                servicio.put("pass", contra);
                servicio.put("uid", id);
                servicio.put("descripServicio", descrip);

                mDatabase.child("usuarios").child(uid).setValue(servicio);

                //cerramos el mensaje de progreso
                progresoDialog.dismiss();

                Toast.makeText(ActualizarServicioActivity.this, "Se actualizaron los datos",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

