package com.example.mainapp.dataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
        version = 2,
        exportSchema = false
)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CategoriasDao categoriasDao();
    public abstract ArticuloDao articuloDao();
    public abstract PersonasDao personasDao();
    public abstract PrestamoDao prestamoDao();

    private static volatile AppDataBase INSTANCE;

    public static ExecutorService dataBaseWriteExecutor = Executors.newFixedThreadPool(4);

    static final Migration MIGRACION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    public static AppDataBase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (AppDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDataBase.class,
                                    "dbLaboratorio"
                            )
                            .addMigrations(MIGRACION_1_2)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}