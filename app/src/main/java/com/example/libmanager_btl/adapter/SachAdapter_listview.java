package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.dao.LoaiSachDAO;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.fragment.SachFragment;
import com.example.libmanager_btl.model.LoaiSach;
import com.example.libmanager_btl.model.Sach;

import java.util.List;

public class SachAdapter_listview extends ArrayAdapter<Sach> {
    private Context context;
    private SachDAO dao;
    private SachFragment sachFragment;
    private List<Sach> list;
    private TextView tvMaSach, tvTenSach, tvMaLoaiSach, tvGiaThue, tvSoLuong;
    private ImageView imgDelete;



    public SachAdapter_listview(@NonNull Context context, SachFragment sachFragment, List<Sach> list) {
        super(context, 0, list);
        Log.d("***", "Tạo thành công adapter sach");
        this.context = context;
        this.sachFragment = sachFragment;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Log.d("***", "chạy hàm getView của adapter sách");

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_sach, null);
            Log.d("***", "Đã có view cho item sách");
        }
        final Sach sach = list.get(position);
        if(sach != null){
            LoaiSachDAO loaiSachDAO = new LoaiSachDAO(context);
            LoaiSach loaiSach = loaiSachDAO.getId(String.valueOf(sach.getMaLoai()));
            // mapping
            tvMaSach = v.findViewById(R.id.tvMaSach);
            tvTenSach = v.findViewById(R.id.tvTenSach);
            tvMaLoaiSach = v.findViewById(R.id.tvLoaiSach);
            tvGiaThue = v.findViewById(R.id.tvGiaThue);
            imgDelete = v.findViewById(R.id.imgDeleteLS);
            tvSoLuong = v.findViewById(R.id.tvSoLuong);
            //
            tvMaSach.setText("Mã sách: "+sach.getMaSach());
            tvTenSach.setText("Tên sách: "+sach.getTenSach());
            if(loaiSach != null)
            {
                tvMaLoaiSach.setText("Loại sách: "+loaiSach.getTenLoai());

            }
            tvGiaThue.setText("Giá thuê: "+sach.getGiaThue());
            tvSoLuong.setText("Số lượng: "+ sach.getSoLuong());

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhieuMuonDAO phieuMuonDb = new PhieuMuonDAO(context);
                    if(phieuMuonDb.getWithMaSach(sach.getMaSach()+"").size() > 0){
                        Toast.makeText(context, "Không thể xóa sách này khi đang dùng", Toast.LENGTH_SHORT).show();
                    }else
                        sachFragment.delete(String.valueOf(sach.getMaSach()));
                }
            });
            Log.d("***", "Đã set dữ liệu lên các view sách");
        }
        return v;
    }
}
