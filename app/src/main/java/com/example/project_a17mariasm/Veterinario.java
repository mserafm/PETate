package com.example.project_a17mariasm;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "veterinario_table")
public class Veterinario {

    @PrimaryKey
    private int id;

    private String nomeVet;

    private String tlf;

    public Veterinario(int id, String nomeVet, String tlf) {
        this.id=id;
        this.nomeVet = nomeVet;
        this.tlf = tlf;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public int getId() {
        return id;
    }

    public String getNomeVet() {
        return nomeVet;
    }

    public String getTlf() {
        return tlf;
    }
}
