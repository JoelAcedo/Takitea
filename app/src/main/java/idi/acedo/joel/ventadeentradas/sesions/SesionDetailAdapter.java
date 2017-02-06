package idi.acedo.joel.ventadeentradas.sesions;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import idi.acedo.joel.ventadeentradas.Butacas;
import idi.acedo.joel.ventadeentradas.LlistaClients;
import idi.acedo.joel.ventadeentradas.R;

/**
 * Created by joela on 04/06/2016.
 */
public class SesionDetailAdapter extends RecyclerView.Adapter<SesionDetailAdapter.SesionHolder> {

    private static final String LOG_TAG = "SesionDetailAdapter";
    private ArrayList<Sesion> mDataset;
    private Context context;

    public SesionDetailAdapter(ArrayList<Sesion> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    public class SesionHolder extends RecyclerView.ViewHolder{
        TextView dateView;
        TextView dayView;
        TextView timeView;
        Button ventesButton;
        Button buyButton;

        public SesionHolder(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.date_text_view_detail);
            dayView = (TextView) itemView.findViewById(R.id.day_name_text_view_detail);
            timeView = (TextView) itemView.findViewById(R.id.time_text_view_detail);
            ventesButton = (Button) itemView.findViewById(R.id.clients_button_session_detail);
            buyButton = (Button) itemView.findViewById(R.id.delete_button_session_detail);
        }

    }

    @Override
    public SesionHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_buy, parent, false);

        SesionHolder sesionHolder = new SesionHolder(view);
        return sesionHolder;
    }

    @Override
    public void onBindViewHolder(SesionHolder holder, final int position) {
        holder.dateView.setText(mDataset.get(position).getDateFormated());
        holder.dayView.setText(mDataset.get(position).getDayName());
        holder.timeView.setText(mDataset.get(position).getHourString());

        holder.ventesButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = mDataset.get(position).getId();
                Intent intent = new Intent(context, LlistaClients.class);
                intent.putExtra(LlistaClients.ID_SESION_INTENT, id);
                context.startActivity(intent);
            }
        });

        holder.buyButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = mDataset.get(position).getId();
                Intent intent = new Intent(context, Butacas.class);
                intent.putExtra(Butacas.ID_SESION_INTENT, id);
                context.startActivity(intent);
            }
        });

        Date now = Calendar.getInstance().getTime();
        if (mDataset.get(position).getDate().before(now))
            holder.buyButton.setEnabled(false);
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
