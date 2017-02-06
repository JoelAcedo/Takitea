package idi.acedo.joel.ventadeentradas.listaobras;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import idi.acedo.joel.ventadeentradas.MainActivity;
import idi.acedo.joel.ventadeentradas.ObraDetail;
import idi.acedo.joel.ventadeentradas.R;

public class ObraTeatreAdapter extends RecyclerView.Adapter<ObraTeatreAdapter.ObraTeatreHolder> {

    private static final String LOG_TAG = "ObraTeatreAdapter";
    private ArrayList<ObraTeatre> mDataset;
    private Context context;

    public ObraTeatreAdapter(ArrayList<ObraTeatre> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    public static class ObraTeatreHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;
        TextView dates;
        TextView time;
        Button delete;
        Button more_info;

        public ObraTeatreHolder(View itemView) {
            super(itemView);
            cover   = (ImageView) itemView.findViewById(R.id.cover);
            title   = (TextView) itemView.findViewById(R.id.text_title);
            dates   = (TextView) itemView.findViewById(R.id.text_dates);
            time    = (TextView) itemView.findViewById(R.id.text_time);
            delete  = (Button) itemView.findViewById(R.id.delete_button_obra);
            more_info = (Button) itemView.findViewById(R.id.btn_more_info);
        }

    }


    @Override
    public ObraTeatreHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.obra_cardview, parent, false);

        ObraTeatreHolder obraTeatreHolder = new ObraTeatreHolder(view);
        return obraTeatreHolder;
    }

    @Override
    public void onBindViewHolder(ObraTeatreHolder holder, final int position) {
        holder.title.setText(mDataset.get(position).getTitle());
        holder.dates.setText(mDataset.get(position).getDates());
        holder.time.setText(mDataset.get(position).getTime());
        holder.cover.setImageBitmap(mDataset.get(position).getImage_bm());
        holder.more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mDataset.get(position).getId();
                Intent intent = new Intent(context, ObraDetail.class);
                intent.putExtra(ObraDetail.ID_OBRA_INTENT, id);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Eliminar");
                alertDialog.setMessage("Desitja eliminar aquesta obra");
                alertDialog.setPositiveButton("Acceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ObraTeatre obraTeatre = mDataset.get(position);
                        mDataset.remove(position);
                        if (context instanceof MainActivity)
                            ((MainActivity) context).BorrarObraById(obraTeatre.getId());
                        notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("Cancel·lar", null);
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
