package com.example.getmybus.Recyclerview_bus_details;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.getmybus.R;
import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapterBus extends RecyclerView.Adapter<RecyclerviewAdapterBus.ViewHolder> {
    List<Busdata> datas = new ArrayList<>();

    public RecyclerviewAdapterBus(List<Busdata> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_details_item , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.src_dest.setText(datas.get(position).getSrc_dest());
        holder.bus_num.setText(datas.get(position).getBus_num());
        holder.exp_time.setText(datas.get(position).getExp_time());
        holder.location.setText(datas.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView src_dest,bus_num,exp_time,location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            src_dest = itemView.findViewById(R.id.src_dest);
            bus_num = itemView.findViewById(R.id.bus_num);
            exp_time = itemView.findViewById(R.id.exp_time);
            location = itemView.findViewById(R.id.location);

        }
    }
}
