package com.example.mainapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapp.R;
import com.example.mainapp.entitys.PrestamoConNombres;

import java.util.List;

public class PrestamosAdapter extends RecyclerView.Adapter<PrestamosAdapter.PrestamoVH> {

    private List<PrestamoConNombres> listaPrestamos;
    private Listener listener;

    public PrestamosAdapter(List<PrestamoConNombres> listaPrestamos, Listener listener) {
        this.listaPrestamos = listaPrestamos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PrestamosAdapter.PrestamoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_prestamos, parent, false);
        return new PrestamoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestamosAdapter.PrestamoVH holder, int position) {
        PrestamoConNombres prestamo = listaPrestamos.get(position);

        holder.txtPersona.setText("Persona: " + prestamo.nombrePersona);
        holder.txtArticulo.setText("Artículo: " + prestamo.nombreArticulo);
        holder.txtFechaP.setText("Préstamo: " + prestamo.fechaPrestamo);

        if (prestamo.devuelto) {
            holder.btnDevolver.setVisibility(View.GONE);
            holder.txtFechaD.setText("Estado: ENTREGADO");
            holder.txtFechaD.setTextColor(android.graphics.Color.parseColor("#388E3C"));
            holder.txtFechaD.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {

            holder.btnDevolver.setVisibility(View.VISIBLE);
            holder.txtFechaD.setText("Devolución: " + prestamo.fechaDevolucionEstimada);
            holder.txtFechaD.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
            holder.txtFechaD.setTypeface(null, android.graphics.Typeface.ITALIC);
        }

        holder.btnDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onDevolver(prestamo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPrestamos.size();
    }

    public class PrestamoVH extends RecyclerView.ViewHolder {
        TextView txtPersona, txtArticulo, txtFechaP, txtFechaD;
        Button btnDevolver;

        public PrestamoVH(@NonNull View itemView) {
            super(itemView);
            txtPersona = itemView.findViewById(R.id.txtPersonaPrestamo);
            txtArticulo = itemView.findViewById(R.id.txtArticuloPrestamo);
            txtFechaP = itemView.findViewById(R.id.txtFechaP);
            txtFechaD = itemView.findViewById(R.id.txtFechaD);
            btnDevolver = itemView.findViewById(R.id.btnDevolverPrestamo);
        }
    }

    public interface Listener {
        void onDevolver(PrestamoConNombres prestamo);
    }
}