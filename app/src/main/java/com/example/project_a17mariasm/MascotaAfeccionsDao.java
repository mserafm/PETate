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
public interface MascotaAfeccionsDao {

    @Insert(onConflict = REPLACE)
    void insert(MascotaAfeccions mascotaAfeccions);

    @Update
    void update(MascotaAfeccions mascotaAfeccions);

    @Delete
    void delete(MascotaAfeccions mascotaAfeccions);

    @Query("SELECT * FROM mascota_table INNER JOIN mascota_afeccions_join ON mascota_table.id=mascota_afeccions_join.mascotaId WHERE mascota_afeccions_join.afeccionId=:afeccionId ORDER BY mascota_table.nome")
    List<Mascota> getMascotasForAfeccion(final int afeccionId);

    @Query("SELECT * FROM afeccions_table INNER JOIN mascota_afeccions_join ON afeccions_table.id=mascota_afeccions_join.afeccionId WHERE mascota_afeccions_join.mascotaId=:mascotaId ORDER BY afeccions_table.tipo")
    LiveData<List<Afeccions>> getAfeccionsForMascota(final int mascotaId);

    @Query("SELECT tipo FROM afeccions_table INNER JOIN mascota_afeccions_join ON afeccions_table.id=mascota_afeccions_join.afeccionId WHERE mascota_afeccions_join.mascotaId=:mascotaId ORDER BY afeccions_table.tipo")
    List<String> getAfeccionsForMascotaArray(final int mascotaId);

    @Query("SELECT * FROM mascota_afeccions_join")
    LiveData<List<MascotaAfeccions>> getAllMascotaAfeccions();

     @Query("SELECT * FROM mascota_afeccions_join")
    List<MascotaAfeccions> getAllMascotaAfeccionsList();
}
