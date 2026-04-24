package com.example.mainapp.entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblArticulos",
        foreignKeys = @ForeignKey(entity = CategoriasEntitys.class,
            parentColumns = "idCategoria",
            childColumns = "idCategoria",
            onDelete = ForeignKey.CASCADE),
        indices = @Index("idCategoria"))
public class ArticulosEntity {
    @PrimaryKey(autoGenerate = true)
    public int idArticulo;
    public String nombreArticulo;
    public String descripcionArticulo;
    public int idCategoria;
    public boolean estadoArticulo;

    public ArticulosEntity(int idArticulo, String nombreArticulo, String descripcionArticulo, int idCategoria, boolean estadoArticulo) {
        this.idArticulo = idArticulo;
        this.nombreArticulo = nombreArticulo;
        this.descripcionArticulo = descripcionArticulo;
        this.idCategoria = idCategoria;
        this.estadoArticulo = estadoArticulo;
    }

    public ArticulosEntity(){

    }
}
