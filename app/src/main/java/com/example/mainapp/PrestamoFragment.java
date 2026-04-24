package com.example.mainapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import com.example.mainapp.adapters.PrestamosAdapter;
import com.example.mainapp.dataBase.AppDataBase;
import com.example.mainapp.entitys.ArticulosEntity;
import com.example.mainapp.entitys.PersonasEntity;
import com.example.mainapp.entitys.PrestamoConNombres;
import com.example.mainapp.entitys.PrestamoEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrestamoFragment extends Fragment {

    private RecyclerView rvPrestamos;
    private PrestamosAdapter adapter;
    private List<PrestamoConNombres> listaPrestamos = new ArrayList<>();
    private FloatingActionButton fabAgregar;
    private AppDataBase db;
    private TabLayout tabFiltro;
    private boolean verHistorial = false;

    public PrestamoFragment() {
        // Required empty public constructor
    }

    public static PrestamoFragment newInstance(String param1, String param2) {
        PrestamoFragment fragment = new PrestamoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prestamo, container, false);

        db = AppDataBase.getInstance(getContext());
        rvPrestamos = view.findViewById(R.id.rvPrestamos);
        fabAgregar = view.findViewById(R.id.fabAgregarPrestamo);
        tabFiltro = view.findViewById(R.id.tabFiltroPrestamos);

        actualizarLista();

        adapter = new PrestamosAdapter(listaPrestamos, new PrestamosAdapter.Listener() {
            @Override
            public void onDevolver(PrestamoConNombres prestamo) {
                finalizarPrestamo(prestamo);
            }
        });

        rvPrestamos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPrestamos.setAdapter(adapter);

        tabFiltro.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                verHistorial = (tab.getPosition() == 1);
                actualizarLista();

                if (verHistorial) fabAgregar.hide();
                else fabAgregar.show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirFormulario();
            }
        });

        return view;
    }

    private void abrirFormulario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_prestamos, null);
        builder.setView(dialogView);

        Spinner spArticulos = dialogView.findViewById(R.id.spArticuloPrestamo);
        Spinner spPersonas = dialogView.findViewById(R.id.spPersonaPrestamo);
        EditText etFechaDev = dialogView.findViewById(R.id.etFechaDevolucion);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardarPrestamo);

        etFechaDev.setFocusable(false);
        etFechaDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int anio = calendar.get(java.util.Calendar.YEAR);
                int mes = calendar.get(java.util.Calendar.MONTH);
                int dia = calendar.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new android.app.DatePickerDialog(getContext(),
                        (view, year, month, dayOfMonth) -> {
                            // Formateamos la fecha seleccionada
                            String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (month + 1), year);
                            etFechaDev.setText(fechaSeleccionada);
                        }, anio, mes, dia);
                datePicker.show();
            }
        });

        List<ArticulosEntity> disponibles = db.articuloDao().obtenerDisponible();
        List<PersonasEntity> personas = db.personasDao().obtenerPersonas();

        List<String> nombresArt = new ArrayList<>();
        for (ArticulosEntity a : disponibles) nombresArt.add(a.nombreArticulo);

        List<String> nombresPer = new ArrayList<>();
        for (PersonasEntity p : personas) nombresPer.add(p.nombrePersona);

        spArticulos.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresArt));
        spPersonas.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresPer));

        AlertDialog dialog = builder.create();

        btnGuardar.setOnClickListener(v -> {
            if (disponibles.isEmpty() || personas.isEmpty()) {
                Toast.makeText(getContext(), "Datos insuficientes", Toast.LENGTH_SHORT).show();
                return;
            }

            String fechaD = etFechaDev.getText().toString();
            if (fechaD.isEmpty()) {
                Toast.makeText(getContext(), "Seleccione una fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            ArticulosEntity art = disponibles.get(spArticulos.getSelectedItemPosition());
            PersonasEntity per = personas.get(spPersonas.getSelectedItemPosition());

            PrestamoEntity nuevo = new PrestamoEntity();
            nuevo.idArticulo = art.idArticulo;
            nuevo.idPersona = per.idPersona;
            nuevo.fechaPrestamo = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            nuevo.fechaDevolucionEstimada = fechaD;
            nuevo.devuelto = false;

            db.prestamoDao().insertarPrestamo(nuevo);

            art.estadoArticulo = true;
            db.articuloDao().editarCategoria(art);

            actualizarLista();
            dialog.dismiss();
            Toast.makeText(getContext(), "Préstamo registrado", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void finalizarPrestamo(PrestamoConNombres p) {
        p.devuelto = true;
        db.prestamoDao().actualizarPrestamo(p);

        ArticulosEntity art = db.articuloDao().obtenerPorId(p.idArticulo);
        if (art != null) {
            art.estadoArticulo = false;
            db.articuloDao().editarCategoria(art);
        }

        actualizarLista();
        Toast.makeText(getContext(), "Artículo devuelto con éxito", Toast.LENGTH_SHORT).show();
    }

    private void actualizarLista() {
        listaPrestamos.clear();
        listaPrestamos.addAll(db.prestamoDao().obtenerPrestamosPorEstado(verHistorial));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}