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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mainapp.adapters.PersonasAdapter;
import com.example.mainapp.dataBase.AppDataBase;
import com.example.mainapp.entitys.PersonasEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class PersonasFragment extends Fragment {
    private RecyclerView rvPersonas;
    private PersonasAdapter personasAdapter;
    private List<PersonasEntity> listaPersonas = new ArrayList<>();
    private FloatingActionButton fagAgregar;
    private PersonasEntity personaExistente;
    private AppDataBase db;

    public PersonasFragment() {
    }

    public static PersonasFragment newInstance(String param1, String param2) {
        PersonasFragment fragment = new PersonasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personas, container, false);

        db = AppDataBase.getInstance(getContext());
        rvPersonas = view.findViewById(R.id.rvPersonas);
        fagAgregar = view.findViewById(R.id.fabAgregarPersona);

        listaPersonas.clear();
        listaPersonas.addAll(db.personasDao().obtenerPersonas());

        personasAdapter = new PersonasAdapter(listaPersonas, new PersonasAdapter.Listener() {
            @Override
            public void insertarPersona(PersonasEntity personasEntity) {
            }

            @Override
            public void editarPersona(PersonasEntity personasEntity) {
                abrirFormulario(personasEntity);
            }

            @Override
            public void eliminarPersona(PersonasEntity personasEntity) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmar eliminación");
                builder.setMessage("¿Desea eliminar a " + personasEntity.nombrePersona + "?");
                builder.setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.personasDao().elimarPersona(personasEntity);
                        listaPersonas.clear();
                        listaPersonas.addAll(db.personasDao().obtenerPersonas());
                        personasAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Persona eliminada con éxito", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }

            @Override
            public void seleccionarPersona(PersonasEntity personasEntity) {
                personaExistente = personasEntity;
            }
        });

        rvPersonas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPersonas.setAdapter(personasAdapter);

        fagAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirFormulario(null);
            }
        });

        return view;
    }

    private void abrirFormulario(@Nullable PersonasEntity personaAEditar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_editar_persona, null);
        builder.setView(dialogView);

        EditText etNombre = dialogView.findViewById(R.id.etNombreEdit);
        EditText etContacto = dialogView.findViewById(R.id.etContactoEdit);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarCambios);

        if (personaAEditar != null) {
            etNombre.setText(personaAEditar.nombrePersona);
            etContacto.setText(personaAEditar.contactoPersona);
            btnGuardar.setText("Actualizar");
        } else {
            btnGuardar.setText("Guardar");
        }

        AlertDialog dialog = builder.create();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = etNombre.getText().toString();
                String con = etContacto.getText().toString();

                if (!nom.isEmpty() && !con.isEmpty()) {
                    if (personaAEditar != null) {
                        personaAEditar.nombrePersona = nom;
                        personaAEditar.contactoPersona = con;
                        db.personasDao().editarPersona(personaAEditar);
                        Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        PersonasEntity nueva = new PersonasEntity();
                        nueva.nombrePersona = nom;
                        nueva.contactoPersona = con;
                        db.personasDao().insertarPersonas(nueva);
                        Toast.makeText(getContext(), "Nueva persona agregada", Toast.LENGTH_SHORT).show();
                    }

                    listaPersonas.clear();
                    listaPersonas.addAll(db.personasDao().obtenerPersonas());
                    personasAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Llene los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}