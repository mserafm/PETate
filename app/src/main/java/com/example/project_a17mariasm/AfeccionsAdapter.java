package com.example.project_a17mariasm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AfeccionsAdapter extends RecyclerView.Adapter<AfeccionsAdapter.AfeccionsHolder> {

    private List<Afeccions> afeccions = new ArrayList<>();

    @NonNull
    @Override
    public AfeccionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.afeccion_item, parent, false);
        return new AfeccionsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AfeccionsHolder holder, int position) {
        Afeccions currentAfeccion = afeccions.get(position);
        holder.txt_tipoAfeccion.setText(currentAfeccion.getTipo());
    }

    @Override
    public int getItemCount() {
        return afeccions.size();
    }

    public void setAfeccions(List<Afeccions> afeccions){
        this.afeccions = afeccions;
        notifyDataSetChanged();
    }

    public Afeccions getAfeccionAt(int position){
        return afeccions.get(position);
    }



    class AfeccionsHolder extends RecyclerView.ViewHolder{
        private TextView txt_tipoAfeccion;

        public AfeccionsHolder(@NonNull View itemView) {
            super(itemView);
            txt_tipoAfeccion= itemView.findViewById(R.id.tv_afeccions_item);
        }
    }
}