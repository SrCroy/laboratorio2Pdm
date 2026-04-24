package com.example.mainapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapp.R;
import com.example.mainapp.entitys.ArticulosEntity;

import java.util.List;

public class ArticulosAdapter extends RecyclerView.Adapter<ArticulosAdapter.ArticulosVH> {

    private List<ArticulosEntity> listaArticulos;
    private Listener listener;

    public ArticulosAdapter(List<ArticulosEntity> listaArticulos, Listener listener) {
        this.listaArticulos = listaArticulos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticulosAdapter.ArticulosVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_articulos, parent, false);
        return new ArticulosVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticulosAdapter.ArticulosVH holder, int position) {
        ArticulosEntity articulo = listaArticulos.get(position);

        holder.txtNombreArticulo.setText(articulo.nombreArticulo);

        String estado = articulo.estadoArticulo ? "No Disponible (Prestado)" : "Disponible";
        holder.txtDetalleArticulo.setText(articulo.descripcionArticulo + "\nEstado: " + estado);

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.editarArticulo(articulo);
                }
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.eliminarArticulo(articulo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    public class ArticulosVH extends RecyclerView.ViewHolder {
        TextView txtNombreArticulo, txtDetalleArticulo, txtEstadoArticulo;
        ImageButton btnEditar, btnEliminar;

        public ArticulosVH(@NonNull View itemView) {
            super(itemView);
            txtNombreArticulo = itemView.findViewById(R.id.txtNombreArticulo);
            txtDetalleArticulo = itemView.findViewById(R.id.txtDetalleArticulo);
            txtEstadoArticulo = itemView.findViewById(R.id.txtEstadoArticulo);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    public interface Listener {
        void editarArticulo(ArticulosEntity articulosEntity);
        void eliminarArticulo(ArticulosEntity articulosEntity);
    }
}