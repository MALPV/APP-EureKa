package com.apps.eureka.eurekapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TipoUserActivity extends AppCompatActivity {

    private ImageView busco, ofrezco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_user);

        busco = (ImageView) findViewById(R.id.imgCliente);
        ofrezco = (ImageView) findViewById(R.id.imgServicio);

        busco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TipoUserActivity.this , RegistroClienteActivity.class);
                startActivity(i);
            }
        });

        ofrezco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TipoUserActivity.this , RegistroActivity.class);
                startActivity(i);
            }
        });
    }
}
