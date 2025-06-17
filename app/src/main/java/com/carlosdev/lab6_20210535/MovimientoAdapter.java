package com.carlosdev.lab6_20210535;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.ViewHolder> {
    private List<MovimientoLimaPass> lista;
    private OnEliminarClickListener listener;

    public interface OnEliminarClickListener {
        void onEliminar(String id);
    }

    public MovimientoAdapter(List<MovimientoLimaPass> lista, OnEliminarClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvIdTarjeta, tvParaderos;
        Button btnEliminar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvIdTarjeta = itemView.findViewById(R.id.tvIdTarjeta);
            tvParaderos = itemView.findViewById(R.id.tvParaderos);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(MovimientoLimaPass mov) {
            tvFecha.setText("Fecha: " + mov.getFecha());
            tvIdTarjeta.setText("Tarjeta: " + mov.getIdTarjeta());
            tvParaderos.setText("De " + mov.getParaderoEntrada() + " a " + mov.getParaderoSalida());

            btnEliminar.setOnClickListener(v -> listener.onEliminar(mov.getId()));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimiento_lima_pass, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
