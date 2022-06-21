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
public interface MascotaDao {

    @Insert(onConflict = REPLACE)
    void insert(Mascota mascota);

    @Update
    void update(Mascota mascota);

    @Delete
    void delete(Mascota mascota);

    @Query("SELECT * FROM mascota_table")
    LiveData<List<Mascota>> getAllMascotas();

    @Query("SELECT * FROM mascota_table WHERE idVeterinario=:idVeterinario")
    LiveData<List<Mascota>> findMascotasForVeterinario(final int idVeterinario);

    @Query("SELECT * FROM veterinario_table INNER JOIN mascota_table ON veterinario_table.id = mascota_table.idVeterinario WHERE mascota_table.id=:idMascota")
    Veterinario findVeterinarioForMascota(final int idMascota);

    @Query("SELECT MAX(id) FROM mascota_table")
    int countMascotas();

    @Query("SELECT * FROM mascota_table WHERE mascota_table.id=:idMascota")
    Mascota findMascotaFromId(final int idMascota);


}
