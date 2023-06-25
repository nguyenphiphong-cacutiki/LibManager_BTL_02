package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.fragment.LoaiSachFragment;
import com.example.libmanager_btl.model.LoaiSach;

import java.util.List;

public class LoaiSachAdapter extends RecyclerView.Adapter<LoaiSachAdapter.SpendingHolder>{
    private Context context;
    private List<LoaiSach> list;
    private LoaiSachFragment fragment;

    public LoaiSachAdapter(Context context, LoaiSachFragment fragment){
        this.context = context;
        this.fragment = fragment;
    }
    public void setData(List<LoaiSach> list){
        this.list = list;
    }

    @NonNull
    @Override
    public SpendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_sach, parent, false);
        return new SpendingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingHolder holder, int position) {
        if(list != null){
            LoaiSach loaiSach = list.get(position);
            holder.tvMaLoai.setText(loaiSach.getMaLoai()+"");
            holder.tvTenLoai.setText(loaiSach.getTenLoai());
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.deleteItem(loaiSach.getMaLoai()+"");
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "Long click me? why???", Toast.LENGTH_SHORT).show();
                    fragment.dialogAddOrInsert(context, LoaiSachFragment.MODE_UPDATE, loaiSach);
                    return false;
                }
            });

            
        }
    }
    

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    

    public class SpendingHolder extends RecyclerView.ViewHolder{
        private TextView tvMaLoai, tvTenLoai;
        private ImageView imgDelete;
        public SpendingHolder(@NonNull View itemView) {
            super(itemView);
            tvMaLoai = itemView.findViewById(R.id.tvMaLoaiSach);
            tvTenLoai = itemView.findViewById(R.id.tvTenLoaiSach);
            imgDelete = itemView.findViewById(R.id.imgDeleteLoaiSach);
            

        }
    }
}
