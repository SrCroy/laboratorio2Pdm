package com.example.mainapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mainapp.entitys.PrestamoEntity;

import java.util.List;

@Dao
public interface PrestamoDao {
    @Insert
    void insertarPrestamo(PrestamoEntity prestamoEntity);
    @Update
    void editarPrestamo(PrestamoEntity prestamoEntity);
    @Query("SELECT * FROM tblPrestamo")
    List<PrestamoEntity> obtenerPrestamos();
    @Query("SELECT * FROM tblPrestamo WHERE devuelto = 0")
    List<PrestamoEntity> obtenerActivos();

    @Query("SELECT * FROM tblPrestamo WHERE devuelto = 1")
    List<PrestamoEntity> obtenerHistorial();
}
