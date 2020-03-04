package com.example.getmybus.Recyclerview_bus_details;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getmybus.MainActivity;
import com.example.getmybus.MapsActivity;
import com.example.getmybus.R;
import java.util.ArrayList;
public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
    ArrayList<Busdata> data = new ArrayList<>();
    Context context;
    public BusAdapter(Context context,ArrayList<Busdata> datas) {
        this.data = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_details_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.src_dest.setText(data.get(position).getSrc_dest());
        holder.bus_num.setText(data.get(position).getBus_num());
        holder.exp_time.setText(data.get(position).getExp_time());
        holder.location.setText(data.get(position).getLocation());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class).putExtra("id",data.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView src_dest,bus_num,exp_time,location;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            src_dest = itemView.findViewById(R.id.src_dest);
            bus_num = itemView.findViewById(R.id.bus_num);
            exp_time = itemView.findViewById(R.id.exp_time);
            location = itemView.findViewById(R.id.location);
            linearLayout = itemView.findViewById(R.id.bus_item);
        }
    }
}
