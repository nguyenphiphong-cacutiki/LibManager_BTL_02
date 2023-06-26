package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.fragment.TopFragment;
import com.example.libmanager_btl.model.Top;

import java.util.ArrayList;
import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.Holder> {
    private Context context;
    TopFragment topFragment;
    List<Top> lists;

    public TopAdapter(Context context, TopFragment fragment){
        this.context = context;
        this.topFragment = fragment;
    }
    public void setData(List<Top> lists){
        this.lists = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(lists != null){
            Top item = lists.get(position);
            holder.tvSach.setText(item.getTenSach());
            holder.tvSL.setText(item.getSoLuong()+"");
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(lists != null) size = lists.size();
        return size;
    }


    public class Holder extends RecyclerView.ViewHolder{
        private TextView tvSach,tvSL;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvSach = itemView.findViewById(R.id.tvSach);
            tvSL = itemView.findViewById(R.id.tvSL);
        }
    }
}
