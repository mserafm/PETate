package com.example.project_a17mariasm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;

public class DialogMascota extends DialogFragment {

    public static final String ARG_ID_MASCOTA ="argIdMascota";
    public static final String ARG_NOME_MASCOTA = "argNomeMascota";
    public static final String ARG_EDAD_MASCOTA = "argEdadMascota";
    public static final String ARG_ESPECIE_MASCOTA = "argEspecieMascota";
    public static final String ARG_CASTRADO_MASCOTA ="argCastradoMascota";
    public static final String ARG_FOTO_MASCOTA = "argFotoMascota";
    public static final String ARG_TIPO_AFECCIONS="argTipoAfeccions";
    public static final String ARG_NOME_VET = "argNomeVet";
    public static final String ARG_NUMERO_VET="argNumeroVet";
    public static final String ARG_ID_VET="argIdVet";

    public static final String EXTRA_ID_MASCOTA ="extraIdMascota";
    public static final String EXTRA_NOME_MASCOTA = "extraNomeMascota";
    public static final String EXTRA_EDAD_MASCOTA = "extraEdadMascota";
    public static final String EXTRA_ESPECIE_MASCOTA = "extraEspecieMascota";
    public static final String EXTRA_CASTRADO_MASCOTA ="extraCastradoMascota";
    public static final String EXTRA_FOTO_MASCOTA ="extraFotoMascota";
    public static final String EXTRA_TIPO_AFECCIONS="extraTipoAfeccions";
    public static final String EXTRA_NOME_VET = "extraNomeVet";
    public static final String EXTRA_NUMERO_VET="extraNumeroVet";
    public static final String EXTRA_ID_VET="extraIdVet";


    public static final int EDIT_MASCOTA_REQUEST=2;

    ImageView iv_foto;
    TextView txt_nome;
    TextView txt_edad;
    TextView txt_especie;
    TextView txt_castrado;
    TextView txt_titulo_af;
    View line_af_view;
    TextView txt_afeccions;
    TextView txt_titulo_vet;
    View line_vet_view;
    TextView txt_vet_nome;
    TextView txt_vet_numero;
    ImageButton btn_chamar;
    Button btn_editar;
    TextView tv_anos;

    public static DialogMascota newInstance(Mascota mascota, Veterinario veterinario, ArrayList<String> tipoAfeccions){
        DialogMascota dialog = new DialogMascota();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_MASCOTA, mascota.getId());
        args.putString(ARG_NOME_MASCOTA, mascota.getNome());
        args.putInt(ARG_EDAD_MASCOTA, mascota.getEdad());
        args.putString(ARG_ESPECIE_MASCOTA, mascota.getEspecie());
        args.putBoolean(ARG_CASTRADO_MASCOTA, mascota.isCastrado());
        args.putString(ARG_FOTO_MASCOTA, mascota.getFoto());
        args.putStringArrayList(ARG_TIPO_AFECCIONS, tipoAfeccions);
        args.putInt(ARG_ID_VET, mascota.getIdVeterinario());
        args.putString(ARG_NOME_VET, veterinario.getNomeVet());
        args.putString(ARG_NUMERO_VET, veterinario.getTlf());
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dialog_mascota, container, false);

        iv_foto =(ImageView) v.findViewById(R.id.iv_mascota_dialog);
        txt_nome = (TextView) v.findViewById(R.id.tv_nome_mascota_dialog);
        txt_edad = (TextView) v.findViewById(R.id.tv_edad_mascota_dialog);
        txt_especie = (TextView) v.findViewById(R.id.tv_especie_mascota_dialog);
        txt_castrado = (TextView) v.findViewById(R.id.tv_castrado_dialog);
        txt_titulo_af = (TextView) v.findViewById(R.id.titulo_afeccions_dialog);
        line_af_view = (View) v.findViewById(R.id.linea_afeccions_dialog);
        txt_afeccions = (TextView) v.findViewById(R.id.tv_afeccions_dialog);
        txt_titulo_vet = (TextView) v.findViewById(R.id.titulo_veterinario_dialog);
        line_vet_view = (View) v.findViewById(R.id.linea_veterinario_dialog);
        txt_vet_nome = (TextView) v.findViewById(R.id.tv_veterinario_nome_dialog);
        txt_vet_numero = (TextView) v.findViewById(R.id.tv_veterinario_tlf_dialog);
        btn_chamar = (ImageButton) v.findViewById(R.id.ib_chamar_veterinario);
        btn_editar=(Button) v.findViewById(R.id.btn_editar_mascota);
        tv_anos = (TextView) v.findViewById(R.id.tv_anos_dialog);

        txt_nome.setText(getArguments().getString(ARG_NOME_MASCOTA));
        txt_edad.setText(String.valueOf(getArguments().getInt(ARG_EDAD_MASCOTA)));
        txt_especie.setText(getArguments().getString(ARG_ESPECIE_MASCOTA));

        if(getArguments().getInt(ARG_EDAD_MASCOTA)==1){
            tv_anos.setText(R.string.ano);
        }else{
            tv_anos.setText(R.string.anos);
        }

        if(!getArguments().getBoolean(ARG_CASTRADO_MASCOTA)){
            txt_castrado.setVisibility(GONE);
        }

        if(!getArguments().getString(ARG_FOTO_MASCOTA).equals("nofoto")){
            iv_foto.setImageURI(Uri.parse(getArguments().getString(ARG_FOTO_MASCOTA)));
        }

        if(getArguments().getInt(ARG_ID_VET)==1){
            txt_titulo_vet.setVisibility(GONE);
            line_vet_view.setVisibility(GONE);
            txt_vet_nome.setVisibility(GONE);
            txt_vet_numero.setVisibility(GONE);
            btn_chamar.setVisibility(GONE);
        }else{
            txt_vet_nome.setText(getArguments().getString(ARG_NOME_VET));
            txt_vet_numero.setText(getArguments().getString(ARG_NUMERO_VET));
        }

        if(getArguments().getStringArrayList(ARG_TIPO_AFECCIONS).size()>0){
            String aux = "";
            for(String af: getArguments().getStringArrayList(ARG_TIPO_AFECCIONS)){
                aux= aux + af +"\n";
            }
            txt_afeccions.setText(aux);
        }else{

            txt_titulo_af.setVisibility(GONE);
            line_af_view.setVisibility(GONE);
            txt_afeccions.setVisibility(GONE);
        }


        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatosMascota.class);
                intent.putExtra(EXTRA_ID_MASCOTA, getArguments().getInt(ARG_ID_MASCOTA));
                intent.putExtra(EXTRA_NOME_MASCOTA, getArguments().getString(ARG_NOME_MASCOTA));
                intent.putExtra(EXTRA_EDAD_MASCOTA, getArguments().getInt(ARG_EDAD_MASCOTA));
                intent.putExtra(EXTRA_ESPECIE_MASCOTA, getArguments().getString(ARG_ESPECIE_MASCOTA));
                intent.putExtra(EXTRA_CASTRADO_MASCOTA, getArguments().getBoolean(ARG_CASTRADO_MASCOTA));
                intent.putExtra(EXTRA_FOTO_MASCOTA, getArguments().getString(ARG_FOTO_MASCOTA));
                intent.putExtra(EXTRA_TIPO_AFECCIONS, getArguments().getStringArrayList(ARG_TIPO_AFECCIONS));
                intent.putExtra(EXTRA_ID_VET, getArguments().getInt(ARG_ID_VET));
                intent.putExtra(EXTRA_NOME_VET, getArguments().getString(ARG_NOME_VET));
                intent.putExtra(EXTRA_NUMERO_VET, getArguments().getString(ARG_NUMERO_VET));

                startActivityForResult(intent, EDIT_MASCOTA_REQUEST);

                dismiss();
            }
        });

        btn_chamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + txt_vet_numero.getText().toString();
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                phoneIntent.setData(Uri.parse(uri));
                startActivity(phoneIntent);
            }
        });

        return v;
    }


}