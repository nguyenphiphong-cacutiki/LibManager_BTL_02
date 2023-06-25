package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.model.ThanhVien;

import java.util.ArrayList;

public class ThanhVienSpinnerAdapter extends ArrayAdapter<ThanhVien> {
    private Context context;
    private ArrayList<ThanhVien> list;
    private TextView tvMaTV, tvTenTV;

    public ThanhVienSpinnerAdapter(@NonNull Context context, ArrayList<ThanhVien> list) {
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
            v = inflater.inflate(R.layout.item_thanh_vien_spinner, null);
        }
        final ThanhVien item = list.get(position);
        if(item != null){
            tvTenTV = v.findViewById(R.id.tvTenTV);
            tvMaTV = v.findViewById(R.id.tvMaTV);

            tvMaTV.setText(item.getMaTV()+": ");
            tvTenTV.setText(item.getHoTen());
        }
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_thanh_vien_spinner, null);
        }
        final ThanhVien item = list.get(position);
        if(item != null){
            tvTenTV = v.findViewById(R.id.tvTenTV);
            tvMaTV = v.findViewById(R.id.tvMaTV);

            tvMaTV.setText(item.getMaTV()+": ");
            tvTenTV.setText(item.getHoTen());
        }
        return v;
    }
}
