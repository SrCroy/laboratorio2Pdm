package com.example.mainapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapp.R;
import com.example.mainapp.entitys.PersonasEntity;

import java.util.ArrayList;
import java.util.List;

public class PersonasAdapter extends RecyclerView.Adapter<PersonasAdapter.PersonasVH> {
    private List<PersonasEntity> listaPersonas = new ArrayList<>();
    private Listener listener;

    public PersonasAdapter(List<PersonasEntity> listaPersonas, Listener listener) {
        this.listaPersonas = listaPersonas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PersonasAdapter.PersonasVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_personas, parent, false);
        return new PersonasVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonasAdapter.PersonasVH holder, int position) {
        PersonasEntity personas = listaPersonas.get(position);

        // Usando tus variables públicas de la entidad
        holder.txtNombrePersona.setText(personas.nombrePersona);
        holder.txtContacto.setText("Contacto: " + personas.contactoPersona);

        // Acción de Editar
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.editarPersona(personas);
                }
            }
        });

        // Acción de Eliminar
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.eliminarPersona(personas);
                }
            }
        });

        // Clic en toda la tarjeta (opcional)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.seleccionarPersona(personas);
        });
    }

    @Override
    public int getItemCount() {
        return listaPersonas != null ? listaPersonas.size() : 0;
    }

    public class PersonasVH extends RecyclerView.ViewHolder {
        TextView txtNombrePersona, txtContacto;
        ImageButton btnEditar, btnEliminar;
        public PersonasVH(@NonNull View itemView) {
            super(itemView);
            txtNombrePersona = itemView.findViewById(R.id.txtNombrePersona);
            txtContacto = itemView.findViewById(R.id.txtContacto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    public void setPersona(List<PersonasEntity> lista){
        this.listaPersonas = lista;
        notifyDataSetChanged();
    }

    public interface Listener{
        void insertarPersona(PersonasEntity personasEntity);
        void editarPersona(PersonasEntity personasEntity);
        void eliminarPersona(PersonasEntity personasEntity);
        void seleccionarPersona(PersonasEntity personasEntity);
    }
}