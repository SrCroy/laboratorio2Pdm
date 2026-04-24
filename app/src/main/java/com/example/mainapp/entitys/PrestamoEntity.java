package com.example.mainapp.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblPrestamo",
        foreignKeys = {
                @ForeignKey(
                        entity = ArticulosEntity.class,
                        parentColumns = "idArticulo",
                        childColumns = "idArticulo",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = PersonasEntity.class,
                        parentColumns = "idPersona",
                        childColumns = "idPersona",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("idArticulo"),
                @Index("idPersona")
        })
public class PrestamoEntity {
    @PrimaryKey(autoGenerate = true)
    public int idPrestamo;

    public int idArticulo;
    public int idPersona;
    public String fechaPrestamo;
    public String fechaDevolucionEstimada;
    public boolean devuelto;
}