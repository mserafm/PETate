package com.example.project_a17mariasm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_MASCOTA_REQUEST = 1;

    private MascotasViewModel mascotasViewModel;
    private TextView tv;

    public static final int CODIGO_MEMORIA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("PETate");

        FloatingActionButton buttonAddMascota = (FloatingActionButton) findViewById(R.id.fbtn_engadir);
        buttonAddMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DatosMascota.class);
                startActivityForResult(intent, ADD_MASCOTA_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_mascota);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        MascotasAdapter adapter = new MascotasAdapter();
        recyclerView.setAdapter(adapter);


        mascotasViewModel = new ViewModelProvider(this).get(MascotasViewModel.class);
        mascotasViewModel.getAllMascotas().observe(this, new Observer<List<Mascota>>() {
            @Override
            public void onChanged(List<Mascota> mascotas) {
                //update Recycler View
                adapter.setMascotas(mascotas);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //mascotasViewModel.deleteMascota(adapter.getMascotaAt(viewHolder.getAdapterPosition()));
                //Toast.makeText(MainActivity.this, R.string.d_deleted, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(getString(R.string.borrar_datos));
                builder.setMessage(getString(R.string.pregunta_borrar) + " " + adapter.getMascotaAt(viewHolder.getAdapterPosition()).getNome() + "?");

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mascotasViewModel.deleteMascota(adapter.getMascotaAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(MainActivity.this, R.string.d_deleted, Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new MascotasAdapter.onItemClickListener() {
            @Override
            public void onItemCLick(Mascota mascota) {
                DialogMascota dialogMascota = null;
                try {
                    dialogMascota = DialogMascota.newInstance(mascota, mascotasViewModel.getVeterinarioForMascota(mascota), mascotasViewModel.getAfeccionsForMascotaArray(mascota));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialogMascota.show(getSupportFragmentManager(), "dialogMascota");
            }
        });

        for (int i = 0; i<adapter.getItemCount(); i++){
            mascotasViewModel.updateMascota(adapter.getMascotaAt(i));
            adapter.notifyItemChanged(i);
        }


        pedirPermiso();
    }


    public void pedirPermiso() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_MEMORIA);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case CODIGO_MEMORIA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    finish();
                }
                return;
            }


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xes_afeccions:
                Intent intent = new Intent(getApplicationContext(), GestionarAfeccions.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



    }


}