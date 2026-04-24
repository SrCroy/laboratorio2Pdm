package com.example.mainapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mainapp.entitys.ArticulosEntity;

import java.util.List;

@Dao
public interface ArticuloDao {
    @Insert
    void insertarCategoria(ArticulosEntity articulosEntity);

    @Update
    void editarCategoria(ArticulosEntity articulosEntity);

    @Delete
    void eliminarCategoria(ArticulosEntity articulosEntity);

    @Query("SELECT * FROM tblArticulos")
    List<ArticulosEntity> obtenerCategoria();

    @Query("SELECT * FROM tblArticulos WHERE estadoArticulo = 0")
    List<ArticulosEntity> obtenerDisponible();

    @Query("SELECT * FROM tblArticulos WHERE idArticulo = :id")
    ArticulosEntity obtenerPorId(int id);
}
