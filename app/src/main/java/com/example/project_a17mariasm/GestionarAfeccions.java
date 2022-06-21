package com.example.project_a17mariasm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GestionarAfeccions extends AppCompatActivity {
    private EditText editText;
    private RecyclerView recyclerView;
    private Button button;
    private Context myContext;
    private MascotasViewModel mascotasViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_afeccions);

        setTitle(R.string.titulo_afeccions);

        myContext = this;
        editText = findViewById(R.id.et_afeccion_xestion);
        recyclerView = findViewById(R.id.rv_afeccions_xestion);
        button = findViewById(R.id.btn_engadir_afeccion_xestion);

        recyclerView.setLayoutManager(new LinearLayoutManager(myContext));
        recyclerView.setHasFixedSize(true);
        AfeccionsAdapter adapter = new AfeccionsAdapter();
        recyclerView.setAdapter(adapter);

        mascotasViewModel = new ViewModelProvider(this).get(MascotasViewModel.class);

        try {

            mascotasViewModel.getAllAfeccions().observe(this, new Observer<List<Afeccions>>() {
                @Override
                public void onChanged(List<Afeccions> afeccions) {
                    adapter.setAfeccions(afeccions);
                }
            });

            new ItemTouchHelper((new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    try {
                        int ls = mascotasViewModel.getMascotaForAfeccions(adapter.getAfeccionAt(viewHolder.getAdapterPosition())).size();
                        if (ls > 0) {

                            String aux = ls > 1 ? " " + getString(R.string.mascotas) : " " + getString(R.string.mascota);

                            Toast.makeText(myContext, getString(R.string.non_borrar) + " " + String.valueOf(ls) + aux, Toast.LENGTH_SHORT).show();
                            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(GestionarAfeccions.this);

                            builder.setTitle(getString(R.string.borrar_datos));
                            builder.setMessage(getString(R.string.pregunta_borrar) + " " + adapter.getAfeccionAt(viewHolder.getAdapterPosition()).getTipo() + "?");

                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mascotasViewModel.deleteAfeccions(adapter.getAfeccionAt(viewHolder.getAdapterPosition()));
                                    Toast.makeText(GestionarAfeccions.this, R.string.d_deleted, Toast.LENGTH_SHORT).show();
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

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            })).attachToRecyclerView(recyclerView);


        } catch (Exception e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(editText.getText().toString().trim())) {

                    ArrayList<Afeccions> arrAf = null;
                    try {
                        arrAf = new ArrayList<>(mascotasViewModel.getAllAfeccionsArray());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    boolean hacer = true;

                    for (Afeccions afeccions : arrAf) {
                        if (editText.getText().toString().trim().toLowerCase().equals(afeccions.getTipo().toLowerCase())) {
                            hacer = false;
                            break;
                        }
                    }

                    if (hacer) {

                        int id = arrAf.get(arrAf.size() - 1).getId() + 1;

                        mascotasViewModel.insertAfeccions(new Afeccions(id, editText.getText().toString().trim()));

                        editText.setText("");

                    } else {
                        Toast.makeText(myContext, getString(R.string.afeccion_existente), Toast.LENGTH_SHORT).show();
                    }



                } else {
                    Toast.makeText(myContext, getString(R.string.inserte_afeccion), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}