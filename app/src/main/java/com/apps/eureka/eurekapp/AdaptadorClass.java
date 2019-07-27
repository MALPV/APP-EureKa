package com.apps.eureka.eurekapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 * Clase que funciona como puente entre los datos del servicio y las vistas contenidas en el recicler view
 */
public class AdaptadorClass
        extends RecyclerView.Adapter<AdaptadorClass.miVistaContenedor> implements View.OnClickListener {

    ArrayList<User> listServicio;
    private View.OnClickListener listener;

    public AdaptadorClass(ArrayList<User> listServicio){

        this.listServicio = listServicio;
    }

    @NonNull
    @Override
    public miVistaContenedor onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_servi, viewGroup,false);
        view.setOnClickListener(this);
        return new miVistaContenedor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull miVistaContenedor miVistaContenedor, int i) {
        String nomComplete = listServicio.get(i).getNombre()+" "+listServicio.get(i).getApellidos();
        miVistaContenedor.nComplet.setText(nomComplete);
        miVistaContenedor.ser.setText(listServicio.get(i).getEspecialidad());
        miVistaContenedor.des.setText(listServicio.get(i).getDescripServicio());
        miVistaContenedor.pre.setText(listServicio.get(i).getPrecio());

    }

    @Override
    public int getItemCount() {
        return listServicio.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    class miVistaContenedor extends RecyclerView.ViewHolder {

        TextView nComplet, ser, des, pre;

        public miVistaContenedor(@NonNull View itemView) {
            super(itemView);
            nComplet = itemView.findViewById(R.id.textTitulo);
            des = itemView.findViewById(R.id.textDescripcion);
            ser = itemView.findViewById(R.id.textServicio);
            pre = itemView.findViewById(R.id.textPrecio);
        }
    }
}
