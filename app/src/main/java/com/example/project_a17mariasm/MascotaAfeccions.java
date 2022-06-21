package com.example.project_a17mariasm;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "mascota_afeccions_join", primaryKeys = {"mascotaId", "afeccionId"},
        foreignKeys = {
                @ForeignKey(entity = Mascota.class, parentColumns = "id", childColumns = "mascotaId", onDelete = CASCADE, onUpdate = CASCADE),
                @ForeignKey(entity = Afeccions.class, parentColumns = "id", childColumns = "afeccionId", onDelete = CASCADE, onUpdate = CASCADE)
        })

public class MascotaAfeccions {

    private int mascotaId;

    private int afeccionId;


    public MascotaAfeccions(int mascotaId, int afeccionId) {
        this.mascotaId = mascotaId;
        this.afeccionId = afeccionId;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getAfeccionId() {
        return afeccionId;
    }
}
