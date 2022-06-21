package com.example.project_a17mariasm;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.MascotasHolder> {

    private List<Mascota> mascotas = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public MascotasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mascota_item, parent, false);

        return new MascotasHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotasHolder holder, int position) {
        Mascota currentMascota = mascotas.get(position);
        holder.tvNomeMascota.setText(currentMascota.getNome());
        holder.tvEdadMascota.setText(String.valueOf(currentMascota.getEdad()));


        if(!currentMascota.getFoto().equals("nofoto")){
            holder.ivFotoMascota.setImageURI(Uri.parse(currentMascota.getFoto()));
        }else{
            holder.ivFotoMascota.setImageResource(R.drawable.paw_placeholder);
        }

        if(currentMascota.getEdad()==1){
            holder.tvAnos.setText(R.string.ano);
        }else{
            holder.tvAnos.setText(R.string.anos);
        }

    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
        notifyDataSetChanged();
    }

    public Mascota getMascotaAt(int position) {
        return mascotas.get(position);
    }

    class MascotasHolder extends RecyclerView.ViewHolder {
        private TextView tvNomeMascota;
        private TextView tvEdadMascota;
        private ImageView ivFotoMascota;
        private TextView tvAnos;

        public MascotasHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeMascota = itemView.findViewById(R.id.tv_nome_mascota);
            tvEdadMascota = itemView.findViewById(R.id.tv_edad_mascota);
            ivFotoMascota = itemView.findViewById(R.id.iv_mascota);
            tvAnos = itemView.findViewById(R.id.tv_anos);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemCLick(mascotas.get(position));
                    }
                }
            });

        }
    }

    public interface onItemClickListener {
        void onItemCLick(Mascota mascota);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
