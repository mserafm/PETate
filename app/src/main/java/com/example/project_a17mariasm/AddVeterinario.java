package com.example.project_a17mariasm;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddVeterinario extends DialogFragment {

    public static final String ARG_NAME = "argName";
    public static final String ARG_NUMBER = "argNumber";

    private EditText etNomeVet;
    private EditText etNumeroVet;
    private Button btnEngadirVet;
    private Button btnCanclarVet;
    private OnDialogVeterinarioListener mListener;


    public static AddVeterinario newInstance(String nome, String number) {
        AddVeterinario fragment = new AddVeterinario();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, nome);
        args.putString(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_veterinario, container, false);

        etNomeVet = (EditText) v.findViewById(R.id.et_nome_veterinario);
        etNumeroVet = (EditText) v.findViewById(R.id.et_numero_veterinario);
        btnEngadirVet = (Button) v.findViewById(R.id.btn_engadir_veterinario_fragment);
        btnCanclarVet = (Button) v.findViewById(R.id.btn_cancelar_veterinario);


        if (getArguments() != null) {
            etNomeVet.setText(getArguments().getString(ARG_NAME));
            etNumeroVet.setText(getArguments().getString(ARG_NUMBER));
        }

        btnCanclarVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnEngadirVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onVetData(etNomeVet.getText().toString(), etNumeroVet.getText().toString());
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogVeterinarioListener) {
            mListener = (OnDialogVeterinarioListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDialogVeterinarioListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogVeterinarioListener {
        public void onVetData(String nome, String numero);
    }


}



