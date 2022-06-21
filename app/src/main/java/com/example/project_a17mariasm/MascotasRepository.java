package com.example.project_a17mariasm;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MascotasRepository {

    private MascotaDao mascotaDao;
    private static Mascota mascota;
    private LiveData<List<Mascota>> allMascotas;
    private static LiveData<List<Mascota>> mascotaForVeterinario;

    private EspecieDao especieDao;
    private LiveData<List<Especie>> allEspecies;

    private AfeccionsDao afeccionsDao;
    private LiveData<List<Afeccions>> allAfeccions;

    private VeterinarioDao veterinarioDao;
    private LiveData<List<Veterinario>> allVeterinarios;

    private MascotaAfeccionsDao mascotaAfeccionsDao;
    private LiveData<List<MascotaAfeccions>> allMascotaAfeccions;
    private static LiveData<List<Mascota>> mascotaForAfeccion;
    private static LiveData<List<Afeccions>> afeccionsForMascota;


    public MascotasRepository(Application application){
        MascotasDatabase database= MascotasDatabase.getInstance(application);

        mascotaDao=database.mascotaDao();
        especieDao=database.especieDao();
        afeccionsDao = database.afeccionsDao();
        veterinarioDao = database.veterinarioDao();
        mascotaAfeccionsDao = database.mascotaAfeccionsDao();

        allMascotas=mascotaDao.getAllMascotas();
        allEspecies=especieDao.getAllEspecie();
        allAfeccions=afeccionsDao.getAllAfeccions();
        allVeterinarios=veterinarioDao.getAllVeterinarios();
        allMascotaAfeccions = mascotaAfeccionsDao.getAllMascotaAfeccions();
    }



    //Tabla Mascota

    public void insertMascota(Mascota mascota){
        new InsertMascotaAsyncTask(mascotaDao).execute(mascota);
    }

    public Mascota getMascota(int idMascota) throws ExecutionException, InterruptedException {
        GetMascotaAsyncTask task = new GetMascotaAsyncTask(mascotaDao);
        return task.execute(idMascota).get();
    }

    public void updateMascota(Mascota mascota){
        new UpdateMascotaAsyncTask(mascotaDao).execute(mascota);
    }

    public void deleteMascota(Mascota mascota){
        new DeleteMascotaAsyncTask(mascotaDao).execute(mascota);
    }

    public LiveData<List<Mascota>> getAllMascotas(){
        return allMascotas;
    }

    public LiveData<List<Mascota>> getMascotaForVeterinario(Veterinario veterinario){

        new FindMascotasForVeterinarioAsyncTask(mascotaDao).execute(veterinario);

        return mascotaForVeterinario;
    }

    public Veterinario getVeterinarioForMascota(Mascota mascota) throws ExecutionException, InterruptedException {
        FindVeterinarioForMascotaAsyncTask task =new FindVeterinarioForMascotaAsyncTask(mascotaDao);

        return task.execute(mascota).get();
    }

    public int countMascotas(){

        CountMascotasAsyncTask task=
        new CountMascotasAsyncTask(mascotaDao);
        try {
            return task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }


    private static class CountMascotasAsyncTask extends AsyncTask<Void, Void, Integer> {

        private MascotaDao mascotaDao;

        private CountMascotasAsyncTask(MascotaDao mascotaDao) {
            this.mascotaDao = mascotaDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return mascotaDao.countMascotas();
        }
    }



    private static class InsertMascotaAsyncTask extends AsyncTask<Mascota, Void, Void> {

        private MascotaDao mascotaDao;

        private InsertMascotaAsyncTask(MascotaDao mascotaDao) {
            this.mascotaDao = mascotaDao;
        }

        @Override
        protected Void doInBackground(Mascota... mascotas) {
            mascotaDao.insert(mascotas[0]);
            return null;
        }
    }

    private static class GetMascotaAsyncTask extends AsyncTask<Integer, Void, Mascota> {

        private MascotaDao mascotaDao;

        private GetMascotaAsyncTask(MascotaDao mascotaDao) {
            this.mascotaDao = mascotaDao;
        }

        @Override
        protected Mascota doInBackground(Integer... integers) {
            Mascota mascota = mascotaDao.findMascotaFromId(integers[0]);
            return mascota;
        }
    }

    private static class UpdateMascotaAsyncTask extends AsyncTask<Mascota, Void, Void> {

        private MascotaDao mascotaDao;

        private UpdateMascotaAsyncTask(MascotaDao mascotaDao) {
            this.mascotaDao = mascotaDao;
        }

        @Override
        protected Void doInBackground(Mascota... mascotas) {
            mascotaDao.update(mascotas[0]);
            return null;
        }
    }

    private static class DeleteMascotaAsyncTask extends AsyncTask<Mascota, Void, Void> {

        private MascotaDao mascotaDao;

        private DeleteMascotaAsyncTask(MascotaDao mascotaDao) {
            this.mascotaDao = mascotaDao;
        }

        @Override
        protected Void doInBackground(Mascota... mascotas) {
            mascotaDao.delete(mascotas[0]);
            return null;
        }
    }

    private static class FindMascotasForVeterinarioAsyncTask extends AsyncTask<Veterinario, Void, Void> {

        private MascotaDao mascotaDao;

        private FindMascotasForVeterinarioAsyncTask(MascotaDao mascotaDao) {
            this.mascotaDao = mascotaDao;
        }

        @Override
        protected Void doInBackground(Veterinario... veterinarios) {

            int parametro = veterinarios[0].getId();
            LiveData<List<Mascota>> result = mascotaDao.findMascotasForVeterinario(parametro);
            mascotaForVeterinario = result;

            return null;
        }
    }

    private static class FindVeterinarioForMascotaAsyncTask extends AsyncTask<Mascota, Void, Veterinario> {

        private MascotaDao mascotaDao;

        private FindVeterinarioForMascotaAsyncTask(MascotaDao mascotaDao) {
            this.mascotaDao = mascotaDao;
        }

        @Override
        protected Veterinario doInBackground(Mascota... mascotas) {

            return mascotaDao.findVeterinarioForMascota(mascotas[0].getId());
        }
    }

    //Tabla Especie

    public void insertEspecie(Especie especie){
        new InsertarEspecieAsynTask(especieDao).execute(especie);
    }

    public void deleteEspecie(Especie especie){
        new DeleteEspecieAsyncTask(especieDao).execute(especie);
    }

    public ArrayList<Especie> getAllEspecieList() throws ExecutionException, InterruptedException {
        GetAllEspecieListAsyncTask task = new GetAllEspecieListAsyncTask(especieDao);
        return task.execute().get();
    }

    public LiveData<List<Especie>> getAllEspecies(){
        return allEspecies;
    }


    private static class GetAllEspecieListAsyncTask extends AsyncTask<Void, Void, ArrayList<Especie>> {

        private EspecieDao especieDao;

        private GetAllEspecieListAsyncTask(EspecieDao especieDao) {
            this.especieDao = especieDao;
        }
        @Override
        protected ArrayList<Especie> doInBackground(Void... voids) {

            ArrayList<Especie> result = new ArrayList(especieDao.getAllEspecieList());

            return result;
        }
    }

    private static class InsertarEspecieAsynTask extends AsyncTask<Especie, Void, Void> {

        private EspecieDao especieDao;

        private InsertarEspecieAsynTask(EspecieDao especieDao) {
            this.especieDao = especieDao;
        }

        @Override
        protected Void doInBackground(Especie... especies) {
            especieDao.insert(especies[0]);
            return null;
        }
    }

    private static class DeleteEspecieAsyncTask extends AsyncTask<Especie, Void, Void> {

        private EspecieDao especieDao;

        private DeleteEspecieAsyncTask(EspecieDao especieDao) {
            this.especieDao = especieDao;
        }

        @Override
        protected Void doInBackground(Especie... especies) {
            especieDao.delete(especies[0]);
            return null;
        }
    }



    //Tabla Veterinario

    public void insertVeterinario(Veterinario veterinario){
        new InsertVeterinarioAsyncTask(veterinarioDao).execute(veterinario);
    }

    public void updateVeterinario(Veterinario veterinario){
        new UpdateVeterinarioAsyncTask(veterinarioDao).execute(veterinario);
    }

    public void deleteVeterinario(Veterinario veterinario){
        new DeleteVeterinarioAsyncTask(veterinarioDao).execute(veterinario);
    }

    public LiveData<List<Veterinario>> getAllVeterinarios(){
        return allVeterinarios;
    }

    public void deleteAllVeterinarios(){
        new DeleteAllVeterinarioAsyncTask(veterinarioDao).execute();
    }

    public int countVeterinarios(){
        CountVeterinarioAsyncTask task = new CountVeterinarioAsyncTask(veterinarioDao);
        try {
            return task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int vetIdFromNomeNumber(String nome, String number){

        vetIdFromNomeNumberAsyncTask task = new vetIdFromNomeNumberAsyncTask(veterinarioDao);
        try {
            return task.execute(nome, number).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;

    }

    private static class vetIdFromNomeNumberAsyncTask extends AsyncTask<String, Void, Integer> {

        private VeterinarioDao veterinarioDao;

        private vetIdFromNomeNumberAsyncTask(VeterinarioDao veterinarioDao) {
            this.veterinarioDao = veterinarioDao;
        }

        @Override
        protected Integer doInBackground(String... strings) {

            return veterinarioDao.vetIdFromNomeNumber(strings[0], strings[1]);
        }
    }


    private static class CountVeterinarioAsyncTask extends AsyncTask<Void, Void, Integer> {

        private VeterinarioDao veterinarioDao;

        private CountVeterinarioAsyncTask(VeterinarioDao veterinarioDao) {
            this.veterinarioDao = veterinarioDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            return veterinarioDao.countVeterinarios();
        }
    }

    private static class InsertVeterinarioAsyncTask extends AsyncTask<Veterinario, Void, Void> {

        private VeterinarioDao veterinarioDao;

        private InsertVeterinarioAsyncTask(VeterinarioDao veterinarioDao) {
            this.veterinarioDao = veterinarioDao;
        }

        @Override
        protected Void doInBackground(Veterinario... veterinarios) {
            veterinarioDao.insert(veterinarios[0]);
            return null;
        }
    }

    private static class UpdateVeterinarioAsyncTask extends AsyncTask<Veterinario, Void, Void> {

        private VeterinarioDao veterinarioDao;

        private UpdateVeterinarioAsyncTask(VeterinarioDao veterinarioDao) {
            this.veterinarioDao = veterinarioDao;
        }

        @Override
        protected Void doInBackground(Veterinario... veterinarios) {
            veterinarioDao.update(veterinarios[0]);
            return null;
        }
    }

    private static class DeleteVeterinarioAsyncTask extends AsyncTask<Veterinario, Void, Void> {

        private VeterinarioDao veterinarioDao;

        private DeleteVeterinarioAsyncTask(VeterinarioDao veterinarioDao) {
            this.veterinarioDao = veterinarioDao;
        }

        @Override
        protected Void doInBackground(Veterinario... veterinarios) {
            veterinarioDao.delete(veterinarios[0]);
            return null;
        }
    }

    private static class DeleteAllVeterinarioAsyncTask extends AsyncTask<Void, Void, Void> {

        private VeterinarioDao veterinarioDao;

        private DeleteAllVeterinarioAsyncTask(VeterinarioDao veterinarioDao) {
            this.veterinarioDao = veterinarioDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            veterinarioDao.deleteAllVeterinarios();
            return null;
        }
    }

    //Tabla Afeccions
    public void insertAfeccions(Afeccions afeccions){
        new InsertAfeccionsAsyncTask(afeccionsDao).execute(afeccions);
    }

    public void updateAfeccions(Afeccions afeccions){
        new UpdateAfeccionsAsyncTask(afeccionsDao).execute(afeccions);
    }

    public void deleteAfeccions(Afeccions afeccions){
        new DeleteAfeccionsAsyncTask(afeccionsDao).execute(afeccions);
    }

    public LiveData<List<Afeccions>> getAllAfeccions(){
        return allAfeccions;
    }

    public void deleteAllAfeccions(){
        new DeleteAllAfeccionsAsyncTask(afeccionsDao).execute();
    }

    public List<Afeccions> getAllAfeccionsList() throws ExecutionException, InterruptedException {
        GetAfeccionsArrayAsyncTask task = new GetAfeccionsArrayAsyncTask(afeccionsDao);
        return task.execute().get();
    }



    public int countAfeccions(){
        CountAfeccionsAsyncTask task = new CountAfeccionsAsyncTask(afeccionsDao);
        try {
            return task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public int afeccionIdFromTipo(String tipo){

        afeccionIdFromTipoAsyncTask task = new afeccionIdFromTipoAsyncTask(afeccionsDao);
        try {
            return task.execute(tipo).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;

    }

    private static class afeccionIdFromTipoAsyncTask extends AsyncTask<String, Void, Integer> {

        private AfeccionsDao afeccionsDao;

        private afeccionIdFromTipoAsyncTask(AfeccionsDao afeccionsDao) {
            this.afeccionsDao = afeccionsDao;
        }

        @Override
        protected Integer doInBackground(String... strings) {

            return afeccionsDao.afeccionIdFromTipo(strings[0]);
        }
    }


    private static class CountAfeccionsAsyncTask extends AsyncTask<Void, Void, Integer> {

        private AfeccionsDao afeccionsDao;

        private CountAfeccionsAsyncTask(AfeccionsDao afeccionsDao) {
            this.afeccionsDao = afeccionsDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return afeccionsDao.countAfeccions();
        }
    }


    private static class InsertAfeccionsAsyncTask extends AsyncTask<Afeccions, Void, Void> {

        private AfeccionsDao afeccionsDao;

        private InsertAfeccionsAsyncTask(AfeccionsDao afeccionsDao) {
            this.afeccionsDao = afeccionsDao;
        }

        @Override
        protected Void doInBackground(Afeccions... afeccions) {
            afeccionsDao.insert(afeccions[0]);
            return null;
        }
    }

    private static class UpdateAfeccionsAsyncTask extends AsyncTask<Afeccions, Void, Void> {

        private AfeccionsDao afeccionsDao;

        private UpdateAfeccionsAsyncTask(AfeccionsDao afeccionsDao) {
            this.afeccionsDao = afeccionsDao;
        }

        @Override
        protected Void doInBackground(Afeccions... afeccions) {
            afeccionsDao.update(afeccions[0]);
            return null;
        }
    }

    private static class DeleteAfeccionsAsyncTask extends AsyncTask<Afeccions, Void, Void> {

        private AfeccionsDao afeccionsDao;

        private DeleteAfeccionsAsyncTask(AfeccionsDao afeccionsDao) {
            this.afeccionsDao = afeccionsDao;
        }

        @Override
        protected Void doInBackground(Afeccions... afeccions) {
            afeccionsDao.delete(afeccions[0]);
            return null;
        }
    }

    private static class DeleteAllAfeccionsAsyncTask extends AsyncTask<Void, Void, Void> {

        private AfeccionsDao afeccionsDao;

        private DeleteAllAfeccionsAsyncTask(AfeccionsDao afeccionsDao) {
            this.afeccionsDao = afeccionsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            afeccionsDao.deleteAllAfeccions();
            return null;
        }
    }

    private static class GetAfeccionsArrayAsyncTask extends AsyncTask<Void, Void, ArrayList<Afeccions>> {

        private AfeccionsDao afeccionsDao;

        private GetAfeccionsArrayAsyncTask(AfeccionsDao afeccionsDao) {
            this.afeccionsDao = afeccionsDao;
        }
        @Override
        protected ArrayList<Afeccions> doInBackground(Void... voids) {

            ArrayList<Afeccions> result = new ArrayList(afeccionsDao.getAllAfeccionsList());

            return result;
        }
    }


    //Tabla Join
    public void insertMascotaAfeccions(MascotaAfeccions mascotaAfeccions){
        new InsertMascotaAfeccionsAsyncTask(mascotaAfeccionsDao).execute(mascotaAfeccions);
    }

    public void updateMascotaAfeccions(MascotaAfeccions mascotaAfeccions){
        new UpdateMascotaAfeccionsAsyncTask(mascotaAfeccionsDao).execute(mascotaAfeccions);
    }

    public void deleteMascotaAfeccions(MascotaAfeccions mascotaAfeccions){
        new DeleteMascotaAfeccionsAsyncTask(mascotaAfeccionsDao).execute(mascotaAfeccions);
    }

    public List<Mascota> findMascotasForAfeccion(Afeccions afeccions) throws ExecutionException, InterruptedException {
        FindMascotasForAfeccionAsyncTask task = new FindMascotasForAfeccionAsyncTask(mascotaAfeccionsDao);
        return task.execute(afeccions).get();
    }

    public LiveData<List<Afeccions>> findAfeccionsForMascotaLive(int mascotaId) throws ExecutionException, InterruptedException {
        FindAfeccionsForMascotaLiveAsyncTask task =new FindAfeccionsForMascotaLiveAsyncTask(mascotaAfeccionsDao);
        return task.execute(mascotaId).get();
    }

    public ArrayList<String> findAfeccionsForMascotaArray(Mascota mascota) throws ExecutionException, InterruptedException {
        FindAfeccionsForMascotaArrayAsyncTask task =new FindAfeccionsForMascotaArrayAsyncTask(mascotaAfeccionsDao);
        return task.execute(mascota).get();
    }

    public LiveData<List<MascotaAfeccions>> getAllMascotaAfeccions(){
        return allMascotaAfeccions;
    }

    public ArrayList<MascotaAfeccions> getAlMascotaAfeccionsList() throws ExecutionException, InterruptedException {
        GetAllAfeccionsMascotaList task = new GetAllAfeccionsMascotaList(mascotaAfeccionsDao);
        return task.execute().get();
    }

    private static class GetAllAfeccionsMascotaList extends AsyncTask<Void, Void, ArrayList<MascotaAfeccions>> {

        private MascotaAfeccionsDao mascotaAfeccionsDao;

        private GetAllAfeccionsMascotaList(MascotaAfeccionsDao mascotaAfeccionsDao) {
            this.mascotaAfeccionsDao = mascotaAfeccionsDao;
        }
        @Override
        protected ArrayList<MascotaAfeccions> doInBackground(Void... voids) {

            ArrayList<MascotaAfeccions> result = new ArrayList(mascotaAfeccionsDao.getAllMascotaAfeccionsList());

            return result;
        }
    }

    private static class FindAfeccionsForMascotaArrayAsyncTask extends AsyncTask<Mascota, Void, ArrayList<String>> {

        private MascotaAfeccionsDao mascotaAfeccionsDao;

        private FindAfeccionsForMascotaArrayAsyncTask(MascotaAfeccionsDao mascotaAfeccionsDao) {
            this.mascotaAfeccionsDao = mascotaAfeccionsDao;
        }
        @Override
        protected ArrayList<String> doInBackground(Mascota... mascotas) {

            int parametro = mascotas[0].getId();
            ArrayList<String> result = new ArrayList(mascotaAfeccionsDao.getAfeccionsForMascotaArray(parametro));

            return result;
        }
    }



    private static class InsertMascotaAfeccionsAsyncTask extends AsyncTask<MascotaAfeccions, Void, Void> {

        private MascotaAfeccionsDao mascotaAfeccionsDao;

        private InsertMascotaAfeccionsAsyncTask(MascotaAfeccionsDao mascotaAfeccionsDao) {
            this.mascotaAfeccionsDao = mascotaAfeccionsDao;
        }

        @Override
        protected Void doInBackground(MascotaAfeccions... mascotaAfeccions) {
            mascotaAfeccionsDao.insert(mascotaAfeccions[0]);
            return null;
        }
    }

    private static class UpdateMascotaAfeccionsAsyncTask extends AsyncTask<MascotaAfeccions, Void, Void> {

        private MascotaAfeccionsDao mascotaAfeccionsDao;

        private UpdateMascotaAfeccionsAsyncTask(MascotaAfeccionsDao mascotaAfeccionsDao) {
            this.mascotaAfeccionsDao = mascotaAfeccionsDao;
        }

        @Override
        protected Void doInBackground(MascotaAfeccions... mascotaAfeccions) {
            mascotaAfeccionsDao.update(mascotaAfeccions[0]);
            return null;
        }
    }

    private static class DeleteMascotaAfeccionsAsyncTask extends AsyncTask<MascotaAfeccions, Void, Void> {

        private MascotaAfeccionsDao mascotaAfeccionsDao;

        private DeleteMascotaAfeccionsAsyncTask(MascotaAfeccionsDao mascotaAfeccionsDao) {
            this.mascotaAfeccionsDao = mascotaAfeccionsDao;
        }

        @Override
        protected Void doInBackground(MascotaAfeccions... mascotaAfeccions) {
            mascotaAfeccionsDao.delete(mascotaAfeccions[0]);
            return null;
        }
    }

    private static class FindMascotasForAfeccionAsyncTask extends AsyncTask<Afeccions, Void, List<Mascota>> {

        private MascotaAfeccionsDao mascotaAfeccionsDao;

        private FindMascotasForAfeccionAsyncTask(MascotaAfeccionsDao mascotaAfeccionsDao) {
            this.mascotaAfeccionsDao = mascotaAfeccionsDao;
        }

        @Override
        protected List<Mascota> doInBackground(Afeccions... afeccions) {

            int parametro = afeccions[0].getId();
            List<Mascota> result = mascotaAfeccionsDao.getMascotasForAfeccion(parametro);

            return result;
        }
    }

    private static class FindAfeccionsForMascotaLiveAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Afeccions>>> {

        private MascotaAfeccionsDao mascotaAfeccionsDao;

        private FindAfeccionsForMascotaLiveAsyncTask(MascotaAfeccionsDao mascotaAfeccionsDao) {
            this.mascotaAfeccionsDao = mascotaAfeccionsDao;
        }
        @Override
        protected LiveData<List<Afeccions>> doInBackground(Integer... integers) {

            //int parametro = mascotas[0].getId();
            LiveData<List<Afeccions>> result = mascotaAfeccionsDao.getAfeccionsForMascota(integers[0]);

            return result;
        }
    }

}
