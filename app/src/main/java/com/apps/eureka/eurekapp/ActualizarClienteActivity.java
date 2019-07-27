package com.apps.eureka.eurekapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ActualizarClienteActivity extends AppCompatActivity {

    //region Inicialización
    private EditText etNom, etApe, etNumContact;
    private Button btnAct;
    private ProgressDialog progresoDialog;
    String nombre, apellido, telef, correo, numDoc, contra, uid;

    //Declaración de obj Auth(Autentificación)
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_cliente);

        //Inicializando el obj Auth
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //region Referencias
        etNom = (EditText) findViewById(R.id.etNombreCliente);
        etApe = (EditText) findViewById(R.id.etApellidosCliente);
        etNumContact = (EditText) findViewById(R.id.etTelefonoCliente);
        btnAct = (Button) findViewById(R.id.btnActualizarCliente);

        //Inicialización del mensaje de progreso
        progresoDialog = new ProgressDialog(this);

        Toolbar toolbarCliente = findViewById(R.id.toolbarCliente);
        getSupportActionBar();
        setSupportActionBar(toolbarCliente);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        final String id = myAuth.getCurrentUser().getUid();
        mDatabase.child("clientes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nombre = dataSnapshot.child("nombre").getValue().toString();
                    apellido = dataSnapshot.child("apellidos").getValue().toString();
                    telef = dataSnapshot.child("numContact").getValue().toString();
                    correo = dataSnapshot.child("correo").getValue().toString();
                    numDoc = dataSnapshot.child("numDoc").getValue().toString();
                    contra = dataSnapshot.child("pass").getValue().toString();
                    uid = dataSnapshot.child("uid").getValue().toString();

                    etNom.setText(nombre);
                    etApe.setText(apellido);
                    etNumContact.setText(telef);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActualizarClienteActivity.this, "Error no se lograron obtener los datos"
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
                //endregion

                if (TextUtils.isEmpty(nombre)){
                    etNom.setError("Debe ingresar un nombre");
                    Toast.makeText(ActualizarClienteActivity.this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
                }
                if (TextUtils.isEmpty(apellido)){
                    etApe.setError("Debe ingresar un apellido");
                    Toast.makeText(ActualizarClienteActivity.this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
                }
                if (TextUtils.isEmpty(telef)){
                    etNumContact.setError("Debe ingresar un numero de contacto");
                    Toast.makeText(ActualizarClienteActivity.this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show();return;
                }

                //Mensaje de carga
                progresoDialog.setMessage("Actualizando datos...");
                progresoDialog.show();

                Map<String, Object> cliente = new HashMap<>();
                cliente.put("nombre", nombre);
                cliente.put("apellidos", apellido);
                cliente.put("numDoc", numDoc);
                cliente.put("numContact", telef);
                cliente.put("correo", correo);
                cliente.put("pass", contra);
                cliente.put("uid", uid);

                mDatabase.child("clientes").child(uid).setValue(cliente);

                //cerramos el mensaje de progreso
                progresoDialog.dismiss();

                Toast.makeText(ActualizarClienteActivity.this, "Se actualizaron los datos",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
