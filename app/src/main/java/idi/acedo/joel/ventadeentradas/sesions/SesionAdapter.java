package idi.acedo.joel.ventadeentradas.sesions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import idi.acedo.joel.ventadeentradas.R;
import idi.acedo.joel.ventadeentradas.listaobras.ObraTeatre;

/**
 * Created by joela on 04/06/2016.
 */
public class SesionAdapter extends RecyclerView.Adapter<SesionAdapter.SesionHolder> {

    private static final String LOG_TAG = "SesionAdapter";
    private ArrayList<Sesion> mDataset;

    public SesionAdapter(ArrayList<Sesion> mDataset) {
        this.mDataset = mDataset;
    }

    public class SesionHolder extends RecyclerView.ViewHolder{
        TextView dateView;
        TextView dayView;
        TextView timeView;
        Button deleteButton;

        public SesionHolder(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.date_text_view);
            dayView = (TextView) itemView.findViewById(R.id.day_name_text_view);
            timeView = (TextView) itemView.findViewById(R.id.time_text_view);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button_session);
        }

    }

    @Override
    public SesionHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_session_item, parent, false);

        SesionHolder sesionHolder = new SesionHolder(view);
        return sesionHolder;
    }

    @Override
    public void onBindViewHolder(SesionHolder holder, final int position) {
        holder.dateView.setText(mDataset.get(position).getDateFormated());
        holder.dayView.setText(mDataset.get(position).getDayName());
        holder.timeView.setText(mDataset.get(position).getHourString());

        holder.deleteButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Sesion sesion = mDataset.get(position);
                mDataset.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItem(Sesion sesion) {
        mDataset.add(sesion);
        this.notifyDataSetChanged();
    }

    public ArrayList<Sesion> getData() {
        return mDataset;
    }
}
