package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.dao.LoaiSachDAO;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.fragment.SachFragment;
import com.example.libmanager_btl.model.LoaiSach;
import com.example.libmanager_btl.model.Mode;
import com.example.libmanager_btl.model.Sach;

import java.util.List;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.Holder> {

    private Context context;
    private SachFragment sachFragment;
    private List<Sach> list;
    private LoaiSachDAO loaiSachDAO;
    private LoaiSach loaiSach;


    public SachAdapter(Context context, SachFragment fragment){
        this.context = context;
        this.sachFragment = fragment;
        this.loaiSachDAO = new LoaiSachDAO(context);
        loaiSach = new LoaiSach();
    }
    public void setData(List<Sach> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sach, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Log.d("***", "bin viewHolder cho Sách");
        if(list != null){
            Sach sach = list.get(position);
            loaiSach = loaiSachDAO.getId(String.valueOf(sach.getMaLoai()));
            //
            holder.tvMaSach.setText("Mã sách: "+sach.getMaSach());
            holder.tvTenSach.setText("Tên sách: "+sach.getTenSach());
            if(loaiSach != null)
            {
                holder.tvMaLoaiSach.setText("Loại sách: "+loaiSach.getTenLoai());

            }
            holder.tvGiaThue.setText("Giá thuê: "+sach.getGiaThue());
            holder.tvSoLuong.setText("Số lượng: "+ sach.getSoLuong());

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhieuMuonDAO phieuMuonDb = new PhieuMuonDAO(context);
                    if(phieuMuonDb.getWithMaSach(sach.getMaSach()+"").size() > 0){
                        Toast.makeText(context, "Không thể xóa sách này khi đang dùng", Toast.LENGTH_SHORT).show();
                    }else
                        sachFragment.delete(String.valueOf(sach.getMaSach()));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    sachFragment.dialogAddAndUpdate(context, Mode.MODE_UPDATE, sach);
                    return false;
                }
            });
            Log.d("***", "Đã set dữ liệu lên các view sách");
        }
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{

        private TextView tvMaSach, tvTenSach, tvMaLoaiSach, tvGiaThue, tvSoLuong;
        private ImageView imgDelete;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvMaSach = itemView.findViewById(R.id.tvMaSach);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvMaLoaiSach = itemView.findViewById(R.id.tvLoaiSach);
            tvGiaThue = itemView.findViewById(R.id.tvGiaThue);
            imgDelete = itemView.findViewById(R.id.imgDeleteLS);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
        }
    }
}
