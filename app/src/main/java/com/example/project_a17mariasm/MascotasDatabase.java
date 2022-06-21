package com.example.project_a17mariasm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Veterinario.class, Especie.class, Mascota.class, Afeccions.class, MascotaAfeccions.class}, version = 1)
public abstract class MascotasDatabase extends RoomDatabase {

    private static MascotasDatabase instance;
    public abstract VeterinarioDao veterinarioDao();
    public abstract EspecieDao especieDao();
    public abstract MascotaDao mascotaDao();
    public abstract AfeccionsDao afeccionsDao();
    public abstract MascotaAfeccionsDao mascotaAfeccionsDao();

    public static synchronized  MascotasDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MascotasDatabase.class, "mascotas_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBackV)
                    .addCallback(roomCallBackE)
                    .addCallback(roomCallBackM)
                    .addCallback(roomCallBackA)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallBackV= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbVeterinarioAsyncTask(instance).execute();

        }
    };


    private static RoomDatabase.Callback roomCallBackE= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbEspecieAsyncTask(instance).execute();

        }
    };


    private static RoomDatabase.Callback roomCallBackM= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbMascotaAsyncTask(instance).execute();

        }
    };


    private static RoomDatabase.Callback roomCallBackA= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAfeccionsAsyncTask(instance).execute();

        }
    };

/*
    private static RoomDatabase.Callback roomCallBackMA= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAfeccionsMascotaAsyncTask(instance).execute();

        }
    };

*/

    private static class PopulateDbVeterinarioAsyncTask extends AsyncTask<Void, Void, Void>{
        private VeterinarioDao veterinarioDao;

        public PopulateDbVeterinarioAsyncTask(MascotasDatabase db) {
            veterinarioDao = db.veterinarioDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Veterinario petVet = new Veterinario(1, "", "");
            veterinarioDao.insert(petVet);


            return null;
        }
    }


    private static class PopulateDbEspecieAsyncTask extends AsyncTask<Void, Void, Void>{
            private EspecieDao especieDao;

        public PopulateDbEspecieAsyncTask(MascotasDatabase db) {
            especieDao = db.especieDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Especie perro = new Especie("Perro");
            especieDao.insert(perro);
            Especie gato = new Especie("Gato");
            especieDao.insert(gato);
            Especie periquito = new Especie("Periquito");
            especieDao.insert(periquito);


            return null;
        }
    }


    private static class PopulateDbMascotaAsyncTask extends AsyncTask<Void, Void, Void>{
        private MascotaDao mascotaDao;

        public PopulateDbMascotaAsyncTask(MascotasDatabase db) {

            mascotaDao = db.mascotaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Mascota coco = new Mascota(1,"Coco", "2020/03/01", "Gato", true, 1, "nofoto");
            mascotaDao.insert(coco);
            Mascota luna = new Mascota(2,"Luna", "2018/05/23", "Perro", true, 1, "nofoto");
            mascotaDao.insert(luna);
            Mascota pollo =new Mascota(3,"Pollo", "2021/02/15", "Periquito", true, 1, "nofoto");
            mascotaDao.insert(pollo);


            return null;
        }
    }

    private static class PopulateDbAfeccionsAsyncTask extends AsyncTask<Void, Void, Void>{
        private AfeccionsDao afeccionsDao;

        public PopulateDbAfeccionsAsyncTask(MascotasDatabase db) {
            afeccionsDao = db.afeccionsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //Insertar Afeccions

            Afeccions alergia = new Afeccions(1,"Alergia");

            afeccionsDao.insert(alergia);


            return null;
        }
    }

    /*
    private static class PopulateDbAfeccionsMascotaAsyncTask extends AsyncTask<Void, Void, Void>{
        private MascotaAfeccionsDao mascotaAfeccionsDao;

        public PopulateDbAfeccionsMascotaAsyncTask(MascotasDatabase db) {
            mascotaAfeccionsDao = db.mascotaAfeccionsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            //Crear relacion Mascota-Afeccion

            MascotaAfeccions polloAlergia = new MascotaAfeccions(0, 0);

            mascotaAfeccionsDao.insert(polloAlergia);


            return null;
        }
    }
*/

}
