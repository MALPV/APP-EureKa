package com.apps.eureka.eurekapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Activity que obtiene los datos del usuario logueado y los muestra en su perfil
 */
public class profileActivity extends AppCompatActivity {

    //region Inicialización
    private TextView tvNombre, tvTelef, tvCorreo;
    private Button btnActualizar, btnCerrar;

    //Declaración de obj Auth(Autentificación)
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Inicializando el obj Auth
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //region Referencias
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvTelef = (TextView) findViewById(R.id.tvTelef);
        tvCorreo = (TextView) findViewById(R.id.tvCorreo);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        btnCerrar = (Button) findViewById(R.id.btnCerrar);

        Toolbar toolbar4 = findViewById(R.id.toolbar4);
        getSupportActionBar();
        setSupportActionBar(toolbar4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        obtenerInfo();

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = myAuth.getCurrentUser().getUid();
                mDatabase.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Logica
                            startActivity(new Intent(profileActivity.this, ActualizarServicioActivity.class));
                        }else {
                            mDatabase.child("clientes").child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        //Logica
                                        startActivity(new Intent(profileActivity.this, ActualizarClienteActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAuth.signOut();
                startActivity(new Intent(profileActivity.this, MainActivity.class));
                Toast.makeText(profileActivity.this,"Has cerrado correctamente la sesión", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void obtenerInfo(){
        final String id = myAuth.getCurrentUser().getUid();
        mDatabase.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nombre = dataSnapshot.child("nombre").getValue().toString()+" "
                            +dataSnapshot.child("apellidos").getValue().toString();
                    String telef = dataSnapshot.child("numContact").getValue().toString();
                    String correo = dataSnapshot.child("correo").getValue().toString();

                    tvNombre.setText(nombre);
                    tvTelef.setText(telef);
                    tvCorreo.setText(correo);
                }else {
                    mDatabase.child("clientes").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String nombre = dataSnapshot.child("nombre").getValue().toString()+" "
                                        +dataSnapshot.child("apellidos").getValue().toString();
                                String telef = dataSnapshot.child("numContact").getValue().toString();
                                String correo = dataSnapshot.child("correo").getValue().toString();

                                tvNombre.setText(nombre);
                                tvTelef.setText(telef);
                                tvCorreo.setText(correo);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(profileActivity.this, "Error al cargar los datos, vuelva a iniciar sesión",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

