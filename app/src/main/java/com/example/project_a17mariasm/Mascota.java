package com.example.project_a17mariasm;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "mascota_table", foreignKeys = {
        @ForeignKey(entity = Veterinario.class,
                parentColumns = "id",
                childColumns = "idVeterinario"),
        @ForeignKey(entity = Especie.class,
                parentColumns = "nome_especie",
                childColumns = "especie")})

public class Mascota {

    @PrimaryKey
    private int id;

    private String nome;

    private String fecha;

    private String especie;

    private boolean castrado;

    private int idVeterinario;

    private String foto;

    private int edad;


    public Mascota(int id, String nome, String fecha, String especie, boolean castrado, int idVeterinario, String foto) {
        this.id = id;
        this.nome = nome;
        this.fecha = fecha;
        this.especie = especie;
        this.castrado = castrado;
        this.idVeterinario = idVeterinario;
        this.foto = foto;
        setEdadBien();
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setEdadBien() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date diaActual = new Date();
        try {
            Date d1 = sdf.parse(sdf.format(diaActual));
            Date d2 = sdf.parse(this.fecha);
            long difference_In_Time
                    = d1.getTime() - d2.getTime();
            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            this.edad = (int) difference_In_Years;

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setCastrado(boolean castrado) {
        this.castrado = castrado;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getEdad() {

        return edad;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEspecie() {
        return especie;
    }

    public boolean isCastrado() {
        return castrado;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }
}