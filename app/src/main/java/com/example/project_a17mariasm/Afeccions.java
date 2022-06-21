package com.example.project_a17mariasm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "afeccions_table")
public class Afeccions {

    @PrimaryKey
    private int id;

    private String tipo;

    public Afeccions(int id, String tipo) {

        this.id = id;

        this.tipo = tipo;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }
}
