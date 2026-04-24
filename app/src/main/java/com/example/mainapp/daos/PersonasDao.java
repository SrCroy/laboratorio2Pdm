package com.example.mainapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mainapp.entitys.ArticulosEntity;
import com.example.mainapp.entitys.PersonasEntity;

import java.util.List;

@Dao
public interface PersonasDao {
    @Insert
    void insertarPersonas(PersonasEntity personasEntity);

    @Update
    void editarPersona(PersonasEntity personasEntity);

    @Delete
    void elimarPersona(PersonasEntity personasEntity);

    @Query("SELECT * FROM tblPersonas")
    List<PersonasEntity> obtenerPersonas();

    @Query("SELECT * FROM tblPersonas WHERE idPersona = :id")
    PersonasEntity obtenerPersonaId(int id);
}
