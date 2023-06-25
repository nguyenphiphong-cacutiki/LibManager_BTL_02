package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.fragment.ThanhVienFragment;
import com.example.libmanager_btl.model.Sach;
import com.example.libmanager_btl.model.ThanhVien;

import java.util.ArrayList;

public class SachSpinnerAdapter extends ArrayAdapter<Sach> {
    private Context context;
    private ArrayList<Sach> list;
    private TextView tvTenSach, tvMaSach;

    public SachSpinnerAdapter(@NonNull Context context, ArrayList<Sach> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_sach_spinner, null);
        }
        final Sach item = list.get(position);
        if(item != null){
            tvMaSach = v.findViewById(R.id.tvMaSachSP);
            tvTenSach = v.findViewById(R.id.tvTenSachSP);

            tvMaSach.setText(item.getMaSach()+": ");
            tvTenSach.setText(item.getTenSach());
        }
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_sach_spinner, null);
        }
        final Sach item = list.get(position);
        if(item != null){
            tvMaSach = v.findViewById(R.id.tvMaSachSP);
            tvTenSach = v.findViewById(R.id.tvTenSachSP);

            tvMaSach.setText(item.getMaSach()+": ");
            tvTenSach.setText(item.getTenSach());
        }
        return v;
    }
}
