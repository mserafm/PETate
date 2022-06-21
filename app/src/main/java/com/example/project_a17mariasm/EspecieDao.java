package com.example.project_a17mariasm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EspecieDao {

    @Insert(onConflict = REPLACE)
    void insert(Especie especie);

    @Update
    void update(Especie especie);

    @Delete
    void delete(Especie especie);

    @Query("DELETE FROM especie_table")
    void deleteAllEspecie();

    @Query("SELECT * FROM especie_table ORDER BY nome_especie")
    LiveData<List<Especie>> getAllEspecie();

    @Query("SELECT * FROM especie_table ORDER BY nome_especie")
    List<Especie> getAllEspecieList();

    @Query("SELECT MAX(nome_especie) FROM especie_table")
    int countEspecies();

}
