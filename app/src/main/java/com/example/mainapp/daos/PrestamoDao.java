package com.example.mainapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mainapp.entitys.PrestamoConNombres;
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

    @Update
    void actualizarPrestamo(PrestamoEntity prestamo);

    @Query("SELECT p.*, a.nombreArticulo as nombreArticulo, per.nombrePersona as nombrePersona " +
            "FROM tblPrestamo p " +
            "INNER JOIN tblArticulos a ON p.idArticulo = a.idArticulo " +
            "INNER JOIN tblPersonas per ON p.idPersona = per.idPersona " +
            "WHERE p.devuelto = :estado")
    List<PrestamoConNombres> obtenerPrestamosPorEstado(boolean estado);
}
