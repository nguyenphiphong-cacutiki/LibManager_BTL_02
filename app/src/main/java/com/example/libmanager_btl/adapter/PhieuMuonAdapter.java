package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.dao.ThanhVienDAO;
import com.example.libmanager_btl.fragment.PhieuMuonFragment;
import com.example.libmanager_btl.model.Mode;
import com.example.libmanager_btl.model.PhieuMuon;
import com.example.libmanager_btl.model.Sach;
import com.example.libmanager_btl.model.ThanhVien;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PhieuMuonAdapter extends RecyclerView.Adapter<PhieuMuonAdapter.SpendingHolder> {

    private Context context;
    private PhieuMuonFragment fragment;
    private List<PhieuMuon> list;
    private SachDAO sachDAO;
    private ThanhVienDAO thanhVienDAO;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public PhieuMuonAdapter(Context context, PhieuMuonFragment fragment) {
        this.context = context;
        this.fragment = fragment;

        sachDAO = new SachDAO(context);
        thanhVienDAO = new ThanhVienDAO(context);
    }

    public void setData(List<PhieuMuon> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void deleteItemOfList(int pos){
        if(pos >= 0  && pos < list.size() - 1){
            list.remove(pos);
        }
    }

    @NonNull
    @Override
    public SpendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phieu_muon, parent, false);
        return new SpendingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingHolder holder, int position) {
        if (list != null) {
            PhieuMuon phieuMuon = list.get(position);

            // set data to view
            holder.tvMaPM.setText("Mã phiếu: " + phieuMuon.getMaPM());
            Sach sach = sachDAO.getId(phieuMuon.getMaSach() + "");
            holder.tvTenSach.setText("Tên sách: " + sach.getTenSach());
            ThanhVien thanhVien = thanhVienDAO.getWithID(phieuMuon.getMaTV() + "");
            holder.tvTenTV.setText("Thành viên: " + thanhVien.getHoTen());
            holder.tvTienThue.setText("Tiền thuê: " + phieuMuon.getTienThue());
            holder.tvNgay.setText(String.format("Ngày: " + sdf.format(phieuMuon.getNgay())));
            holder.tvSoLuong.setText("Số lượng: " + phieuMuon.getSoLuong());




            if (phieuMuon.getTraSach() == PhieuMuon.DA_TRA) {
                holder.tvTraSach.setTextColor(Color.GREEN);
                holder.tvTraSach.setText("Đã trả sách");
            } else {
                holder.tvTraSach.setTextColor(Color.RED);
                holder.tvTraSach.setText("Chưa trả sách");
                if(PhieuMuon.isQuaHan(phieuMuon)){
                    holder.tvTraSach.setText("Đã quá hạn");
                }
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    fragment.insertAndUpdate(context, Mode.MODE_UPDATE, phieuMuon);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public class SpendingHolder extends RecyclerView.ViewHolder {
        private TextView tvMaPM, tvTenTV, tvTienThue, tvTenSach, tvNgay, tvTraSach, tvSoLuong;
        private ImageView imgDelete;

        public SpendingHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPM = itemView.findViewById(R.id.tvMaPM);
            tvTenTV = itemView.findViewById(R.id.tvTenTV);
            tvTienThue = itemView.findViewById(R.id.tvTienThue);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvNgay = itemView.findViewById(R.id.tvNgayMuon);
            tvTraSach = itemView.findViewById(R.id.tvTraSach);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            imgDelete = itemView.findViewById(R.id.imgDeletePM);
        }
    }
}
