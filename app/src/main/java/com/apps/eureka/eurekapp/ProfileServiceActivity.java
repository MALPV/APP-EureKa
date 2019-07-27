package com.apps.eureka.eurekapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileServiceActivity extends AppCompatActivity {

    private TextView tvNombre, tvOficio, tvTelefono, tvCorreo, tvPrecio;
    private EditText etDescrip;
    private Button btnLlamar, btnWsp;
    private String nombre, oficio, descripServi, num, correo, precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_service);

        //Inicializamos el navBar y activamos el boton atras
        Toolbar toolbar5 = findViewById(R.id.toolbar5);
        getSupportActionBar();
        setSupportActionBar(toolbar5);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region Referencias
        tvNombre = (TextView) findViewById(R.id.tvNombreServi);
        tvOficio = (TextView) findViewById(R.id.tvCatServi);
        etDescrip = (EditText) findViewById(R.id.etDescripServi);
        tvTelefono = (TextView) findViewById(R.id.tvTelefServi);
        tvCorreo = (TextView) findViewById(R.id.tvCorreoServi);
        tvPrecio = (TextView) findViewById(R.id.tvPrecio);

        btnLlamar = (Button) findViewById(R.id.btnLlamar);
        btnWsp = (Button) findViewById(R.id.btnWsp);
        //endregion

        //Obtenemos los datos del intent desde el Recycler view
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            nombre = bundle.getString("nombre");
            oficio = bundle.getString("oficio");
            descripServi = bundle.getString("descripServi");
            precio = bundle.getString("precio");

            num = bundle.getString("num");
            correo = bundle.getString("correo");

            tvNombre.setText(nombre);
            tvOficio.setText(oficio);
            etDescrip.setText(descripServi);
            tvPrecio.setText(precio);
            tvTelefono.setText(num);
            tvCorreo.setText(correo);
        }

        btnWsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + num);
                Intent msjWsp = new Intent(Intent.ACTION_SENDTO, uri);
                msjWsp.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(msjWsp, ""));
            }
        });

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + num);
                Intent llamada = new Intent(Intent.ACTION_DIAL, number);
                startActivity(llamada);
            }
        });
    }
}
