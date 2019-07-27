package com.apps.eureka.eurekapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Clase que muestra la lista de los servicios registrados y genera la busqqueda por tipo de servicio
 */
public class buscarServicioActivity extends AppCompatActivity {


    //Declaración e inicialización de obj Auth(Autentificación)
    private FirebaseAuth myAuth= FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference reference = mDatabase.child("usuarios");

    private TextView msjBienvenida;
    private ArrayList<User> list;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_servicio);

        //Inicializando el obj Auth
        myAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchView = (SearchView) findViewById(R.id.searchView);
        msjBienvenida = (TextView) findViewById(R.id.tvMsjBienvenida);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mensajeBienvenida();
    }

    public void mensajeBienvenida(){
        final String id = myAuth.getCurrentUser().getUid();
        mDatabase.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nombre = "¡Hola, "+dataSnapshot.child("nombre").getValue().toString()+" "
                            +dataSnapshot.child("apellidos").getValue().toString()+"!";

                    msjBienvenida.setText(nombre);
                }else {
                    mDatabase.child("clientes").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String nombre = "¡Hola, "+dataSnapshot.child("nombre").getValue().toString()+" "
                                        +dataSnapshot.child("apellidos").getValue().toString()+"!";

                                msjBienvenida.setText(nombre);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (mDatabase != null){

            mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            list.add(ds.getValue(User.class));
                        }
                        AdaptadorClass adaptadorClass = new AdaptadorClass(list);
                        adaptadorClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(buscarServicioActivity.this, ProfileServiceActivity.class);
                                intent.putExtra("nombre", list.get(recyclerView.getChildAdapterPosition(v)).getNombre()+
                                        " "+list.get(recyclerView.getChildAdapterPosition(v)).getApellidos());
                                intent.putExtra("oficio", list.get(recyclerView.getChildAdapterPosition(v)).getEspecialidad());
                                intent.putExtra("descripServi", list.get(recyclerView.getChildAdapterPosition(v)).getDescripServicio());
                                intent.putExtra("precio", list.get(recyclerView.getChildAdapterPosition(v)).getPrecio());
                                intent.putExtra("num", list.get(recyclerView.getChildAdapterPosition(v)).getNumContact());
                                intent.putExtra("correo", list.get(recyclerView.getChildAdapterPosition(v)).getCorreo());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adaptadorClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(buscarServicioActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    buscar(s);
                    return true;
                }
            });
        }
    }

    public void buscar(String str){
        final ArrayList<User> listBusqueda = new ArrayList<>();
        for (User usr : list){
            if (usr.getEspecialidad().toLowerCase().contains(str.toLowerCase())){
                listBusqueda.add(usr);
            }
        }
        AdaptadorClass adaptadorClass = new AdaptadorClass(listBusqueda);
        adaptadorClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(buscarServicioActivity.this, ProfileServiceActivity.class);
                intent.putExtra("nombre", listBusqueda.get(recyclerView.getChildAdapterPosition(v)).getNombre()+
                        " "+listBusqueda.get(recyclerView.getChildAdapterPosition(v)).getApellidos());
                intent.putExtra("oficio", listBusqueda.get(recyclerView.getChildAdapterPosition(v)).getEspecialidad());
                intent.putExtra("descripServi", listBusqueda.get(recyclerView.getChildAdapterPosition(v)).getDescripServicio());
                intent.putExtra("num", listBusqueda.get(recyclerView.getChildAdapterPosition(v)).getNumContact());
                intent.putExtra("correo", listBusqueda.get(recyclerView.getChildAdapterPosition(v)).getCorreo());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adaptadorClass);
    }

    //Metodo que inicializa el menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.format_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1://ver perfil
                startActivity(new Intent(buscarServicioActivity.this, profileActivity.class));
                break;
            case R.id.item2://actualizar perfil
                final String id = myAuth.getCurrentUser().getUid();
                mDatabase.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //Logica
                            startActivity(new Intent(buscarServicioActivity.this, ActualizarServicioActivity.class));
                        }else {
                            mDatabase.child("clientes").child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        //Logica
                                        startActivity(new Intent(buscarServicioActivity.this, ActualizarClienteActivity.class));
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
                break;
            case R.id.item3://cerrar sesión
                myAuth.signOut();
                startActivity(new Intent(buscarServicioActivity.this, MainActivity.class));
                Toast.makeText(buscarServicioActivity.this,"Has cerrado correctamente la sesión", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo que bloquea volver a la pantalla de inicio de sesión
     */
    @Override public void onBackPressed() { moveTaskToBack(true); }
}
