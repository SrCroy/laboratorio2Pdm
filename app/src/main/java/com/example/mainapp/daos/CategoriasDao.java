package com.example.mainapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mainapp.entitys.CategoriasEntitys;

import java.util.List;

@Dao
public interface CategoriasDao {
    @Insert
    void insertarCategoria(CategoriasEntitys categoriasEntitys);

    @Update
    void updateCategoria(CategoriasEntitys categoriasEntitys);

    @Delete
    void eliminarCategoria(CategoriasEntitys categoriasEntitys);

    @Query("SELECT * FROM tblCategorias")
    List<CategoriasEntitys> obtenerCategorias();
}
