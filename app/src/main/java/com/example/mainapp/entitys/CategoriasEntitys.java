package com.example.mainapp.entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblCategorias")
public class CategoriasEntitys {
    @PrimaryKey(autoGenerate = true)
    public int idCategoria;
    public String nommbreCategoria;

    public CategoriasEntitys(int idCategoria, String nommbreCategoria) {
        this.idCategoria = idCategoria;
        this.nommbreCategoria = nommbreCategoria;
    }

    public CategoriasEntitys() {
    }
}
