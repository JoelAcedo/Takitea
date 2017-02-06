package idi.acedo.joel.ventadeentradas.ventas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import idi.acedo.joel.ventadeentradas.R;

/**
 * Created by joela on 06/06/2016.
 */
public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaHolder> {

    private ArrayList<Venta> mDataset;

    public VentaAdapter(ArrayList<Venta> mDataset) {
        this.mDataset = mDataset;
    }

    public class VentaHolder extends RecyclerView.ViewHolder {
        TextView email;
        TextView seients;
        TextView name;

        public VentaHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.lista_name);
            email = (TextView) itemView.findViewById(R.id.lista_email);
            seients = (TextView) itemView.findViewById(R.id.lista_seients);
        }
    }

    @Override
    public VentaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_venta_item, parent, false);

        VentaHolder ventaHolder = new VentaHolder(view);
        return ventaHolder;
    }

    @Override
    public void onBindViewHolder(VentaHolder holder, int position) {
        holder.name.setText(mDataset.get(position).getName());
        holder.email.setText(mDataset.get(position).getEmail());
        holder.seients.setText("Seients: " + mDataset.get(position).getAsientosFormated());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
