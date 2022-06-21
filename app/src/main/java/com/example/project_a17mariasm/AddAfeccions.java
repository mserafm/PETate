package com.example.project_a17mariasm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class AddAfeccions extends DialogFragment {

    private static final String ARG_TIPO = "tipoAfeccion";

    private EditText et_afeccion;
    private Button btn_engadir;
    private Button btn_sair;
    private ArrayList<Afeccions> afList;
    private ArrayList<String> tipoList;
    private ArrayList<String> checkedList;
    private MascotasViewModel mascotasViewModel;
    private ListView listView;
    private onAddAfeccions listener;


    public static AddAfeccions newInstance(int mascotaId) {
        AddAfeccions fragment = new AddAfeccions();
        Bundle args = new Bundle();
        args.putInt(ARG_TIPO, mascotaId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_afeccions, container, false);

        mascotasViewModel = new ViewModelProvider(getActivity()).get(MascotasViewModel.class);
        tipoList = new ArrayList<String>();
        try {
            afList = new ArrayList<Afeccions>(mascotasViewModel.getAllAfeccionsArray());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        et_afeccion = (EditText) v.findViewById(R.id.et_afeccion);
        btn_engadir = (Button) v.findViewById(R.id.btn_engadir_afeccion_fragment);
        btn_sair = (Button) v.findViewById(R.id.btn_cancelar_afeccions_fragment);
        listView = v.findViewById(R.id.lv_afeccions);

        adaptarArrays();


        if (getArguments() != null) {

            try {
                checkedList = new ArrayList<>(mascotasViewModel.getAfeccionsForMascotaArray(mascotasViewModel.getMascota(getArguments().getInt(ARG_TIPO))));
                int ls = afList.size();
                int cs = checkedList.size();
                for (int i = 0; i < ls; i++) {
                    for (int j = 0; j < cs; j++)
                        if (listView.getItemAtPosition(i).equals(checkedList.get(j))) {
                            listView.setItemChecked(i, true);
                        }
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        btn_engadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = "";
                if (!"".equals(et_afeccion.getText().toString().trim())) {
                    aux = et_afeccion.getText().toString().trim();
                    int id = afList.get(afList.size() - 1).getId() + 1;
                    boolean hacer = true;
                    for (String tipo : tipoList) {
                        if (et_afeccion.getText().toString().trim().toLowerCase().equals(tipo.toLowerCase())) {
                            hacer = false;
                        }
                    }
                    if (hacer) {
                        mascotasViewModel.insertAfeccions(new Afeccions(id, aux));
                        SparseBooleanArray checked = listView.getCheckedItemPositions();
                        ArrayList<Boolean> auxArr = new ArrayList<>();
                        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                            auxArr.add(false);
                        }
                        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                            if (checked.get(i)) {
                                auxArr.set(i, true);
                            } else {
                                auxArr.set(i, false);
                            }
                        }
                        afList.add(new Afeccions(id, aux));
                        adaptarArrays();
                        for (int i = 0; i < auxArr.size(); i++) {
                            listView.setItemChecked(i, auxArr.get(i));
                        }
                        listView.setItemChecked(afList.size() - 1, true);
                        et_afeccion.setText("");
                    }else{
                        Toast.makeText(getContext(), getString(R.string.afeccion_existente), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), getString(R.string.inserte_afeccion), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> arrAux = new ArrayList<String>();
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    if (checked.get(i)) {
                        arrAux.add(listView.getItemAtPosition(i).toString());
                    }
                }
                if (arrAux.size() > 0) {
                    listener.onAfData(arrAux);
                }

                SharedPreferences sharedPref = getContext().getSharedPreferences(getString(R.string.preference_preferences_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("existe_array", true);
                editor.apply();

                dismiss();

            }
        });
        return v;

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onAddAfeccions) {
            listener = (onAddAfeccions) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement onAddAfeccions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public void adaptarArrays() {
        tipoList.clear();
        for (Afeccions af : afList) {
            tipoList.add(af.getTipo());
        }
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, tipoList));

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public interface onAddAfeccions {
        public void onAfData(ArrayList<String> tipo);
    }

}