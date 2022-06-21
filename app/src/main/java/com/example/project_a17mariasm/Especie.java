package com.example.project_a17mariasm;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "especie_table")
public class Especie {

    @PrimaryKey
    @NonNull
    private String nome_especie;

    public Especie(@NonNull String nome_especie) {
        this.nome_especie = nome_especie;
    }

    @NonNull
    public String getNome_especie() {
        return nome_especie;
    }

    public void setNome_especie(@NonNull String nome_especie) {
        this.nome_especie = nome_especie;
    }
}