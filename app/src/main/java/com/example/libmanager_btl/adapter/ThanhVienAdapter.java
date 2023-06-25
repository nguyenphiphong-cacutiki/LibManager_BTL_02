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
import com.example.libmanager_btl.model.ThanhVien;

import java.util.ArrayList;

public class ThanhVienAdapter extends ArrayAdapter<ThanhVien> {

    private Context context;
    ThanhVienFragment thanhVienFragment;
    private ArrayList<ThanhVien> lists;
    private TextView tvMaTV, tvTenTV, tvNamSinh;
    ImageView imgDel;
    public ThanhVienAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ThanhVienAdapter(@NonNull Context context, ThanhVienFragment thanhVienFragment, ArrayList<ThanhVien> lists) {
        super(context, 0, lists);
        this.context = context;
        this.thanhVienFragment = thanhVienFragment;
        this.lists = lists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Log.d("***", "chạy hàm getview của adapter thành viên");


        if(v == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_thanh_vien, null);
        }
        final ThanhVien thanhVien = lists.get(position);
        if(thanhVien != null){
            tvMaTV = v.findViewById(R.id.tvMaTV);
            tvTenTV = v.findViewById(R.id.tvTenTV);
            tvNamSinh = v.findViewById(R.id.tvNamSinh);
            imgDel = v.findViewById(R.id.imgDeleteLS);

            tvMaTV.setText("Mã thành viên: "+ thanhVien.getMaTV());
            tvTenTV.setText("Tên thành viên: "+ thanhVien.getHoTen());
            tvNamSinh.setText("Năm sinh: "+ thanhVien.getNamSinh());


            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thanhVienFragment.xoa(String.valueOf(thanhVien.getMaTV()));
                }
            });

        }

        return v;
    }

}
