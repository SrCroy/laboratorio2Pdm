package com.example.mainapp.dataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mainapp.daos.ArticuloDao;
import com.example.mainapp.daos.CategoriasDao;
import com.example.mainapp.daos.PersonasDao;
import com.example.mainapp.daos.PrestamoDao;
import com.example.mainapp.entitys.ArticulosEntity;
import com.example.mainapp.entitys.CategoriasEntitys;
import com.example.mainapp.entitys.PersonasEntity;
import com.example.mainapp.entitys.PrestamoEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                CategoriasEntitys.class,
                ArticulosEntity.class,
                PersonasEntity.class,
                PrestamoEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CategoriasDao categoriasDao();
    public abstract ArticuloDao articuloDao();
    public abstract PersonasDao personasDao();
    public abstract PrestamoDao prestamoDao();

    private static volatile AppDataBase INSTANCE;

    public static ExecutorService dataBaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDataBase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (AppDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "dbLaboratorio"
                            )
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
