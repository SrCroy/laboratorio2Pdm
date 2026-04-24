package com.example.mainapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapp.R;
import com.example.mainapp.entitys.CategoriasEntitys;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriasVH> {

    private List<CategoriasEntitys> listaCategorias;
    private Listener listener;

    // Constructor
    public CategoriasAdapter(List<CategoriasEntitys> listaCategorias, Listener listener) {
        this.listaCategorias = listaCategorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriasVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_categorias, parent, false);
        return new CategoriasVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriasVH holder, int position) {
        CategoriasEntitys categoria = listaCategorias.get(position);

        holder.txtNombre.setText(categoria.nommbreCategoria);
        holder.txtId.setText("ID Categoría: " + categoria.idCategoria);

        // Eventos de botones
        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) listener.onEditar(categoria);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onEliminar(categoria);
        });
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public class CategoriasVH extends RecyclerView.ViewHolder {
        TextView txtNombre, txtId;
        ImageButton btnEditar, btnEliminar;

        public CategoriasVH(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreCatItem);
            txtId = itemView.findViewById(R.id.txtIdCatItem);
            btnEditar = itemView.findViewById(R.id.btnEditarCat);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCat);
        }
    }

    // Interfaz para manejar clics desde el Fragment
    public interface Listener {
        void onEditar(CategoriasEntitys categoria);
        void onEliminar(CategoriasEntitys categoria);
    }
}