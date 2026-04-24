package com.example.mainapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mainapp.adapters.ArticulosAdapter;
import com.example.mainapp.dataBase.AppDataBase;
import com.example.mainapp.entitys.ArticulosEntity;
import com.example.mainapp.entitys.CategoriasEntitys;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ArticulosFragment extends Fragment {

    private RecyclerView rvArticulos;
    private ArticulosAdapter articulosAdapter;
    private List<ArticulosEntity> listaArticulos = new ArrayList<>();
    private FloatingActionButton fabAgregarArticulo;
    private AppDataBase db;

    public ArticulosFragment() {
        // Required empty public constructor
    }

    public static ArticulosFragment newInstance(String param1, String param2) {
        ArticulosFragment fragment = new ArticulosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articulos, container, false);

        db = AppDataBase.getInstance(getContext());
        rvArticulos = view.findViewById(R.id.rvArticulos);
        fabAgregarArticulo = view.findViewById(R.id.fabAgregarArticulo);

        actualizarLista();

        articulosAdapter = new ArticulosAdapter(listaArticulos, new ArticulosAdapter.Listener() {
            @Override
            public void editarArticulo(ArticulosEntity articulosEntity) {
                abrirFormulario(articulosEntity);
            }

            @Override
            public void eliminarArticulo(ArticulosEntity articulosEntity) {
                if (articulosEntity.estadoArticulo) {
                    Toast.makeText(getContext(), "No se puede eliminar un artículo prestado", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirmar");
                    builder.setMessage("¿Desea eliminar " + articulosEntity.nombreArticulo + "?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.articuloDao().eliminarCategoria(articulosEntity);
                            actualizarLista();
                            Toast.makeText(getContext(), "Artículo eliminado con éxito", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.show();
                }
            }
        });

        rvArticulos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvArticulos.setAdapter(articulosAdapter);

        fabAgregarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirFormulario(null);
            }
        });

        return view;
    }

    private void abrirFormulario(@Nullable ArticulosEntity articuloAEditar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_articulos, null);
        builder.setView(dialogView);

        EditText etNombre = dialogView.findViewById(R.id.etNombreArticulo);
        EditText etDesc = dialogView.findViewById(R.id.etDescripcionArticulo);
        Spinner spCategorias = dialogView.findViewById(R.id.spCategorias);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarArticulo);

        List<CategoriasEntitys> categorias = db.categoriasDao().obtenerCategorias();
        List<String> nombresCategorias = new ArrayList<>();
        for (CategoriasEntitys cat : categorias) {
            nombresCategorias.add(cat.nommbreCategoria);
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresCategorias);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategorias.setAdapter(adapterSpinner);

        if (articuloAEditar != null) {
            etNombre.setText(articuloAEditar.nombreArticulo);
            etDesc.setText(articuloAEditar.descripcionArticulo);
            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).idCategoria == articuloAEditar.idCategoria) {
                    spCategorias.setSelection(i);
                }
            }
            btnGuardar.setText("Actualizar");
        }

        AlertDialog dialog = builder.create();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = etNombre.getText().toString();
                String des = etDesc.getText().toString();

                if (!nom.isEmpty()) {
                    int idCatSelected = categorias.get(spCategorias.getSelectedItemPosition()).idCategoria;

                    if (articuloAEditar != null) {
                        articuloAEditar.nombreArticulo = nom;
                        articuloAEditar.descripcionArticulo = des;
                        articuloAEditar.idCategoria = idCatSelected;
                        db.articuloDao().editarCategoria(articuloAEditar);
                        Toast.makeText(getContext(), "Artículo actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        ArticulosEntity nuevo = new ArticulosEntity();
                        nuevo.nombreArticulo = nom;
                        nuevo.descripcionArticulo = des;
                        nuevo.idCategoria = idCatSelected;
                        nuevo.estadoArticulo = false;
                        db.articuloDao().insertarCategoria(nuevo);
                        Toast.makeText(getContext(), "Artículo agregado con éxito", Toast.LENGTH_SHORT).show();
                    }
                    actualizarLista();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Debe ingresar al menos el nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void actualizarLista() {
        listaArticulos.clear();
        listaArticulos.addAll(db.articuloDao().obtenerCategoria());
        if (articulosAdapter != null) {
            articulosAdapter.notifyDataSetChanged();
        }
    }
}