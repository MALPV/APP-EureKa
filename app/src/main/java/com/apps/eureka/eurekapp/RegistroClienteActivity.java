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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Clase Activity que genera el registro de los usuarios
 */

public class RegistroClienteActivity extends AppCompatActivity {

    //region Inicialización
    private EditText etNom, etApe, etNumDoc, etNumContact, etCorreo, etContra, etContra2;
    private Button btnRegistro;
    private ProgressDialog progresoDialog;

    //Declaración de obj Auth(Autentificación)
    FirebaseAuth myAuth;
    DatabaseReference mDatabase;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        //Inicializando el obj Auth
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //region Referencias
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etContra = (EditText) findViewById(R.id.etContraseña);

        etNom = (EditText) findViewById(R.id.etNombre);
        etApe = (EditText) findViewById(R.id.etApellidos);
        etNumDoc = (EditText) findViewById(R.id.etKey);
        etNumContact = (EditText) findViewById(R.id.etTelefono);
        etContra2 = (EditText) findViewById(R.id.etContraseña2);
        btnRegistro = (Button)findViewById(R.id.btnRegistrar);

        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        getSupportActionBar();
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        //Inicialización del mensaje de progreso
        progresoDialog = new ProgressDialog(this);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroUser();
            }
        });

    }

    //Metodo para limpiar los editText despues de generado el registro
    public void limpiarDatos(){
        //Limpiamos los EditText
        etNom.setText("");
        etApe.setText("");
        etNumDoc.setText("");
        etNumContact.setText("+569 ");
        etCorreo.setText("");
        etContra.setText("");
        etContra2.setText("");
    }

    //Metodo para registrar usuarios
    public void registroUser(){

        //region Obtención de datos
        final String correo = etCorreo.getText().toString().trim();
        final String contra = etContra.getText().toString().trim();
        String contra2 = etContra2.getText().toString().trim();

        final String nom = etNom.getText().toString().trim();
        final String ape = etApe.getText().toString();
        final String numDoc = etNumDoc.getText().toString().trim();
        final String numContact = etNumContact.getText().toString().trim();

        //endregion

        //region Validación
        //Verificamos que no esten vacios y validamos los datos para generar el registro
        if (TextUtils.isEmpty(nom)){
            etNom.setError("Debe ingresar un nombre");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
        }
        if (TextUtils.isEmpty(ape)){
            etApe.setError("Debe ingresar un apellido");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
        }
        if (TextUtils.isEmpty(numDoc)){
            etNumDoc.setError("Debe ingresar un Rut");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
        }
        if (!validarRut(numDoc)){
            etNumDoc.setError("Debe ingresar un Rut valido");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
        }
        if (TextUtils.isEmpty(numContact)){
            etNumContact.setError("Debe ingresar un numero de contacto");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show();return;
        }
        if (TextUtils.isEmpty(correo)){//(correo.equals(""))
            etCorreo.setError("Debe ingresar un correo");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show(); return;
        }
        if (TextUtils.isEmpty(contra)) {
            etContra.setError("Debe ingresar una contraseña");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show();
            return;
        }
        if (contra.length() < 6) {
            etContra.setError("Debe ingresar una contraseña mayor a 6 caracteres");
            Toast.makeText(this, "Revise los datos ingresados",Toast.LENGTH_SHORT).show();
            return;
        }
        if ((contra.equals(contra2))){ }else{
            etContra.setText("");
            etContra2.setText("");
            Toast.makeText(this, "Las contraseñas deben ser iguales",Toast.LENGTH_LONG).show();
            return;
        }
        //endregion

        //Mensaje de carga
        progresoDialog.setMessage("Generando el registro, espere unos segundos...");
        progresoDialog.show();

        //region Creando usuario Auth
        myAuth.createUserWithEmailAndPassword(correo, contra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Con la variable id se obtiene el UID del usuario
                            String id = myAuth.getCurrentUser().getUid();

                            //region Creacion y Verificación ObjetoUsuario
                            Map<String, Object> map = new HashMap<>();
                            map.put("nombre", nom);
                            map.put("apellidos", ape);
                            map.put("numDoc", numDoc);
                            map.put("numContact", numContact);
                            map.put("correo", correo);
                            map.put("pass", contra);
                            map.put("uid", id);

                            mDatabase.child("clientes").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if (task2.isSuccessful()){
                                        //metodo para limpiar los editText
                                        limpiarDatos();
                                        //Verificado el registro, se envia el mensaje al usuario
                                        Toast.makeText(RegistroClienteActivity.this, "¡Se logro registrar correctamente!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //endregion

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistroClienteActivity.this, "No se logro generar el registro...",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //cerramos el mensaje de progreso
                        progresoDialog.dismiss();

                        if (task.isSuccessful()) {
                            Intent i = new Intent(RegistroClienteActivity.this, MainActivity.class);
                            startActivity(i);
                        }

                    }
                });
        //endregion
    }

    public static boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }
}