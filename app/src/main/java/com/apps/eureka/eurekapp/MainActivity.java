package com.apps.eureka.eurekapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Clase Activity que valida la autentificación del usuario y conexión con la firebase
 */
public class MainActivity extends AppCompatActivity {

    //region Inicialización
    //Objetos View
    private EditText etUser, etPass;
    private Button btnLoguear;
    private TextView tvNewuser;
    private ProgressDialog progresoDialog;

    //Declaración de obj Auth(Autentificación)
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializando el obj Auth
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //region Referencias
        etUser = (EditText) findViewById(R.id.etUserL);
        etPass = (EditText)findViewById(R.id.etPassL);
        tvNewuser = (TextView) findViewById(R.id.tvRegistrar);
        btnLoguear = (Button) findViewById(R.id.btnLogin);
        //endregion

        progresoDialog = new ProgressDialog(this);


        //Lleva al Activity de registro
        tvNewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , TipoUserActivity.class);
                startActivity(i);
            }
        });

        btnLoguear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUsuario();
            }
        });

    }

    public void loginUsuario(){
        //region Obtención de datos
        final String correo = etUser.getText().toString().trim();
        final String contra = etPass.getText().toString().trim();
        //endregion

        if (TextUtils.isEmpty(correo)){//(correo.equals(""))
            etUser.setError("Debe ingresar un correo");
            //Toast.makeText(this, "Debe ingresar un correo",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contra)) {
            etPass.setError("Debe ingresar una contraseña");
            //Toast.makeText(this, "Debe ingresar una contraseña",Toast.LENGTH_SHORT).show();
            return;
        }

        //Mensaje de carga
        progresoDialog.setMessage("Iniciando sesión, cargando perfil...");
        progresoDialog.show();

        //region Autentificación y logueo de usuario
        myAuth.signInWithEmailAndPassword(correo, contra).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = myAuth.getCurrentUser().getUid();
                    mDatabase.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(MainActivity.this, buscarServicioActivity.class);
                                //String id = myAuth.getUid();
                                //intent.putExtra(profileActivity.id, id);
                                startActivity(intent);
                                finish();
                            }else{
                                startActivity(new Intent(MainActivity.this, buscarServicioActivity.class));
                                finish();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "No se logró iniciar sesión, verifique los datos...",
                            Toast.LENGTH_SHORT).show();
                }
                //cerramos el mensaje de progreso
                progresoDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (myAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, buscarServicioActivity.class));
            finish();
        }
    }
}
