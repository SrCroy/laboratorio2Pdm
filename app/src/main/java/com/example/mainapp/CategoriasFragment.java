package com.example.mainapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainapp.adapters.CategoriasAdapter;
import com.example.mainapp.dataBase.AppDataBase;
import com.example.mainapp.entitys.CategoriasEntitys;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoriasFragment extends Fragment implements CategoriasAdapter.Listener {

    private RecyclerView rvCategorias;
    private FloatingActionButton fabAgregar;
    private CategoriasAdapter adapter;
    private List<CategoriasEntitys> listaCategorias = new ArrayList<>();
    private AppDataBase db;

    public CategoriasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        db = AppDataBase.getInstance(getContext());
        rvCategorias = view.findViewById(R.id.rvCategorias);
        fabAgregar = view.findViewById(R.id.fabAgregarCat);

        setupRecyclerView();

        fabAgregar.setOnClickListener(v -> showCategoriaDialog(null));

        cargarCategorias();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new CategoriasAdapter(listaCategorias, this);
        rvCategorias.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategorias.setAdapter(adapter);
    }

    private void cargarCategorias() {
        listaCategorias.clear();
        listaCategorias.addAll(db.categoriasDao().obtenerCategorias());
        adapter.notifyDataSetChanged();
    }

    private void showCategoriaDialog(@Nullable CategoriasEntitys categoria) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_categoria, null);
        
        TextView lblTitulo = view.findViewById(R.id.lblTituloDialog);
        EditText txtNombre = view.findViewById(R.id.txtNombreCatDialog);

        if (categoria != null) {
            lblTitulo.setText("Editar Categoría");
            txtNombre.setText(categoria.nommbreCategoria);
        } else {
            lblTitulo.setText("Nueva Categoría");
        }

        builder.setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = txtNombre.getText().toString().trim();
                    if (nombre.isEmpty()) {
                        Toast.makeText(getContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (categoria == null) {
                        // Insertar
                        CategoriasEntitys nueva = new CategoriasEntitys();
                        nueva.nommbreCategoria = nombre;
                        db.categoriasDao().insertarCategoria(nueva);
                        Toast.makeText(getContext(), "Categoría agregada", Toast.LENGTH_SHORT).show();
                    } else {
                        // Actualizar
                        categoria.nommbreCategoria = nombre;
                        db.categoriasDao().updateCategoria(categoria);
                        Toast.makeText(getContext(), "Categoría actualizada", Toast.LENGTH_SHORT).show();
                    }
                    cargarCategorias();
                })
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    @Override
    public void onEditar(CategoriasEntitys categoria) {
        showCategoriaDialog(categoria);
    }

    @Override
    public void onEliminar(CategoriasEntitys categoria) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Categoría")
                .setMessage("¿Estás seguro de eliminar la categoría '" + categoria.nommbreCategoria + "'?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    db.categoriasDao().eliminarCategoria(categoria);
                    Toast.makeText(getContext(), "Categoría eliminada", Toast.LENGTH_SHORT).show();
                    cargarCategorias();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}