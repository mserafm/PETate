package com.example.project_a17mariasm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MascotasViewModel extends AndroidViewModel {

    private MascotasRepository repository;
    private LiveData<List<Veterinario>> allVeterinarios;
    private LiveData<List<Especie>> allEspecies;
    private LiveData<List<Mascota>> allMascotas;
    private LiveData<List<Afeccions>> allAfeccions;
    private LiveData<List<MascotaAfeccions>> allMascotaAfeccions;

    public MascotasViewModel(@NonNull Application application) {
        super(application);
        repository = new MascotasRepository(application);
        allVeterinarios = repository.getAllVeterinarios();
        allEspecies = repository.getAllEspecies();
        allMascotas = repository.getAllMascotas();
        allAfeccions = repository.getAllAfeccions();
        allMascotaAfeccions = repository.getAllMascotaAfeccions();
    }

    //Acciones Mascotas
    public void insetMascota(Mascota mascota) {
        repository.insertMascota(mascota);
    }

    public Mascota getMascota(int idMascota) throws ExecutionException, InterruptedException {
        return repository.getMascota(idMascota);
    }

    public void updateMascota(Mascota mascota) {
        repository.updateMascota(mascota);
    }

    public void deleteMascota(Mascota mascota) {
        repository.deleteMascota(mascota);
    }

    public LiveData<List<Mascota>> getAllMascotas() {
        return allMascotas;
    }

    public LiveData<List<Mascota>> getMascotaForVeterinario(Veterinario veterinario) {
        return repository.getMascotaForVeterinario(veterinario);
    }

    public Veterinario getVeterinarioForMascota(Mascota mascota) throws ExecutionException, InterruptedException {
        return repository.getVeterinarioForMascota(mascota);
    }

    public int countMascotas() {
        return repository.countMascotas();
    }

    //Acciones Especies

    public void insertEspecie(Especie especie) {
        repository.insertEspecie(especie);
    }
    public void deleteEspecie(Especie especie) {
        repository.deleteEspecie(especie);
    }

    public LiveData<List<Especie>> getAllEspecies() {
        return allEspecies;
    }

    public ArrayList<Especie> getAllEspecieList() throws ExecutionException, InterruptedException {
        return repository.getAllEspecieList();
    }

    //Acciones Veterinarios

    public void insertVeterinario(Veterinario veterinario) {
        repository.insertVeterinario(veterinario);
    }

    public void updateVeterinario(Veterinario veterinario) {
        repository.updateVeterinario(veterinario);
    }

    public void deleteVeterinario(Veterinario veterinario) {
        repository.deleteVeterinario(veterinario);
    }

    public void deleteAllVeterinarios() {
        repository.deleteAllVeterinarios();
    }

    public LiveData<List<Veterinario>> getAllVeterinarios() {
        return allVeterinarios;
    }

    public int countVeterinarios() {
        return repository.countVeterinarios();
    }

    public int vetIdFromNomeNumber(String nome, String number) {
        return repository.vetIdFromNomeNumber(nome, number);
    }


    //Acciones Afeccions

    public void insertAfeccions(Afeccions afeccions) {
        repository.insertAfeccions(afeccions);
    }

    public void updateAfeccions(Afeccions afeccions) {
        repository.updateAfeccions(afeccions);
    }

    public void deleteAfeccions(Afeccions afeccions) {
        repository.deleteAfeccions(afeccions);
    }

    public void deleteAllAfeccions() {
        repository.deleteAllAfeccions();
    }

    public LiveData<List<Afeccions>> getAllAfeccions() {
        return allAfeccions;
    }

    public int countAfeccions() {
        return repository.countAfeccions();
    }

    public int afeccionIdFromTipo(String tipo){
        return repository.afeccionIdFromTipo(tipo);
    }

    public List<Afeccions> getAllAfeccionsArray() throws ExecutionException, InterruptedException {
        return repository.getAllAfeccionsList();
    }


    //Accions MascotaAfeccions

    public void insertJoin(MascotaAfeccions mascotaAfeccions) {
        repository.insertMascotaAfeccions(mascotaAfeccions);
    }

    public void updateJoin(MascotaAfeccions mascotaAfeccions) {
        repository.updateMascotaAfeccions(mascotaAfeccions);
    }

    public void deleteJoin(MascotaAfeccions mascotaAfeccions) {
        repository.deleteMascotaAfeccions(mascotaAfeccions);
    }

    public List<Mascota> getMascotaForAfeccions(Afeccions afeccions) throws ExecutionException, InterruptedException {
        return repository.findMascotasForAfeccion(afeccions);
    }

    public LiveData<List<Afeccions>> getAfeccionsForMascotaLive(int mascotaId) throws ExecutionException, InterruptedException {
        return repository.findAfeccionsForMascotaLive(mascotaId);
    }

    public ArrayList<String> getAfeccionsForMascotaArray(Mascota mascota) throws ExecutionException, InterruptedException {
        return repository.findAfeccionsForMascotaArray(mascota);
    }

    public LiveData<List<MascotaAfeccions>> getAllMascotaAfeccions() {
        return allMascotaAfeccions;
    }

    public ArrayList<MascotaAfeccions> getAllMascotaAfeccionsList() throws ExecutionException, InterruptedException {
        return repository.getAlMascotaAfeccionsList();
    }
}
