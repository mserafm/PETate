package com.example.project_a17mariasm;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface VeterinarioDao {

    @Insert(onConflict = REPLACE)
    void insert(Veterinario veterinario);

    @Update
    void update(Veterinario veterinario);

    @Delete
    void delete(Veterinario veterinario);

    @Query("DELETE FROM veterinario_table")
    void deleteAllVeterinarios();

    @Query("SELECT * FROM veterinario_table ORDER BY nomeVet")
    LiveData<List<Veterinario>> getAllVeterinarios();

    @Query("SELECT MAX(id) FROM veterinario_table")
    int countVeterinarios();

    @Query("SELECT id FROM veterinario_table WHERE nomeVet=:nome AND tlf=:number ")
    int vetIdFromNomeNumber(String nome, String number);

}
