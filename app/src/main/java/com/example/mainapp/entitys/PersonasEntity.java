package com.example.mainapp.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblPersonas")
public class PersonasEntity {
    @PrimaryKey(autoGenerate = true)
    public int idPersona;
    public String nombrePersona;
    public String contactoPersona;

    public PersonasEntity(int idPersona, String nombrePersona, String contactoPersona) {
        this.idPersona = idPersona;
        this.nombrePersona = nombrePersona;
        this.contactoPersona = contactoPersona;
    }

    public PersonasEntity(){

    }
}
