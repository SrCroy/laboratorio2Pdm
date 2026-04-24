package com.example.mainapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.mainapp.dataBase.AppDataBase;
import com.example.mainapp.entitys.CategoriasEntitys;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private CategoriasEntitys categoriasEntitys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cargarCategorias();

        this.bottomNavigationView = findViewById(R.id.menuButton);

        if(savedInstanceState == null){
            cambiarFragmnto(new CategoriasFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setCheckable(true);
                if (menuItem.getItemId() == R.id.categorias){
                    cambiarFragmnto(new CategoriasFragment());
                    return true;
                }else if (menuItem.getItemId() == R.id.personas){
                    cambiarFragmnto(new PersonasFragment());
                    return true;
                }else if (menuItem.getItemId() == R.id.articulos){
                    cambiarFragmnto(new ArticulosFragment());
                    return true;
                }else if (menuItem.getItemId() == R.id.prestamos){
                    cambiarFragmnto(new PrestamoFragment());
                    return true;
                }
                return false;
            }
        });

        /*categoriasEntitys = new CategoriasEntitys(1,"Cargadores");

        AppDataBase appDataBase = AppDataBase.getInstance(getApplicationContext());

        appDataBase.dataBaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                appDataBase.categoriasDao().insertarCategoria(categoriasEntitys);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Dato ingresado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
    }

    private void cambiarFragmnto(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerMain, fragment)
                .commit();
    }

    private void cargarCategorias() {
        AppDataBase db = AppDataBase.getInstance(this);
        List<CategoriasEntitys> lista = db.categoriasDao().obtenerCategorias();

        if (lista.isEmpty()) {
            db.categoriasDao().insertarCategoria(new CategoriasEntitys(0, "Electrónica"));
            db.categoriasDao().insertarCategoria(new CategoriasEntitys(0, "Herramientas"));
            db.categoriasDao().insertarCategoria(new CategoriasEntitys(0, "Mobiliario"));
            db.categoriasDao().insertarCategoria(new CategoriasEntitys(0, "Papelería"));
            db.categoriasDao().insertarCategoria(new CategoriasEntitys(0, "Libros"));
        }
    }
}