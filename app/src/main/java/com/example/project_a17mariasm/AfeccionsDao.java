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
public interface AfeccionsDao {

    @Insert(onConflict = REPLACE)
    void insert(Afeccions afeccions);

    @Update
    void update(Afeccions afeccions);

    @Delete
    void delete (Afeccions afeccions);

    @Query("DELETE FROM afeccions_table")
    void deleteAllAfeccions();

    @Query("SELECT * FROM afeccions_table ORDER BY tipo")
    LiveData<List<Afeccions>> getAllAfeccions();

    @Query("SELECT * FROM afeccions_table ORDER BY tipo")
    List<Afeccions> getAllAfeccionsList();

    @Query("SELECT MAX(id) FROM afeccions_table")
    int countAfeccions();

    @Query("SELECT id FROM afeccions_table WHERE tipo=:tipo ")
    int afeccionIdFromTipo(String tipo);

}
