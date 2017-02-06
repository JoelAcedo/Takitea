package idi.acedo.joel.ventadeentradas.asientos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import idi.acedo.joel.ventadeentradas.R;

/**
 * Created by joela on 06/06/2016.
 */
public class AsientoAdapter extends RecyclerView.Adapter<AsientoAdapter.AsientoHolder>{

    private ArrayList<Asiento> mDataset;
    private Context context;
    private ArrayList<Asiento> asientosSeleccionados;

    public AsientoAdapter(ArrayList<Asiento> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;

        asientosSeleccionados = new ArrayList<Asiento>();
    }

    public class AsientoHolder extends RecyclerView.ViewHolder{
        TextView info_butaca;

        public AsientoHolder(View itemView) {
            super(itemView);
            info_butaca = (TextView) itemView.findViewById(R.id.asiento_text);
        }
    }

    @Override
    public AsientoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asiento, parent, false);

        AsientoHolder asientoHolder = new AsientoHolder(view);
        return asientoHolder;
    }

    @Override
    public void onBindViewHolder(final AsientoHolder holder, final int position) {
        holder.info_butaca.setText(mDataset.get(position).getIdString());

        holder.info_butaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int estado = mDataset.get(position).getEstado();
                if (estado == Asiento.ASIENTO_SELEC) {
                    holder.info_butaca.setBackgroundColor(context.getResources().getColor(R.color.asiento_disponible));
                    mDataset.get(position).setEstado(Asiento.ASIENTO_LIBRE);
                    asientosSeleccionados.remove(asientosSeleccionados.indexOf(mDataset.get(position)));
                }
                else if (estado == Asiento.ASIENTO_LIBRE) {
                    holder.info_butaca.setBackgroundColor(context.getResources().getColor(R.color.asiento_selec));
                    mDataset.get(position).setEstado(Asiento.ASIENTO_SELEC);
                    asientosSeleccionados.add(mDataset.get(position));
                }
            }
        });

        int colorTmp = mDataset.get(position).getEstado();
        if (colorTmp == Asiento.ASIENTO_SELEC)
            holder.info_butaca.setBackgroundColor(context.getResources().getColor(R.color.asiento_selec));
        else if (colorTmp == Asiento.ASIENTO_OCUPADO)
            holder.info_butaca.setBackgroundColor(context.getResources().getColor(R.color.asiento_ocupado));
        else
            holder.info_butaca.setBackgroundColor(context.getResources().getColor(R.color.asiento_disponible));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public int getSizeAsientosSeleccionados() {
        return asientosSeleccionados.size();
    }

    public ArrayList<Asiento> asientosSeleccionados() {
        return asientosSeleccionados;
    }
}
