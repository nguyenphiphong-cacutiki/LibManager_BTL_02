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
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.fragment.ThanhVienFragment;
import com.example.libmanager_btl.model.Mode;
import com.example.libmanager_btl.model.ThanhVien;

import java.util.List;

public class ThanhVienAdapter extends RecyclerView.Adapter<ThanhVienAdapter.Holder> {

    private Context context;
    private ThanhVienFragment thanhVienFragment;
    private List<ThanhVien> lists;
    

    public ThanhVienAdapter(Context context, ThanhVienFragment thanhVienFragment){
        this.context = context;
        this.thanhVienFragment = thanhVienFragment;
    }
    public void setData(List<ThanhVien> lists){
        this.lists = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thanh_vien, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(lists != null){
            ThanhVien item = lists.get(position);
            holder.tvMaTV.setText("Mã thành viên: "+ item.getMaTV());
            holder.tvTenTV.setText("Tên thành viên: "+ item.getHoTen());
            holder.tvNamSinh.setText("Năm sinh: "+ item.getNamSinh());

            holder.imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhieuMuonDAO phieuMuonDb = new PhieuMuonDAO(context);
                    if(phieuMuonDb.getWithMaTVChuaTraSach(item.getMaTV()+"").size()>0)
                        Toast.makeText(context, "Không thể xóa thành viên chưa trả sách", Toast.LENGTH_SHORT).show();
                    else
                        thanhVienFragment.delete(String.valueOf(item.getMaTV()));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    thanhVienFragment.dialogInsertAndUpdate(context, Mode.MODE_UPDATE, item);
                    return false;
                }
            });
        }else{
            Log.d("***", "bin dữ liệu cho "+" list đang null, ko bin");
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(lists != null) size = lists.size();
        Log.d("***", "count được số item: "+size);
        return size;
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView tvMaTV, tvTenTV, tvNamSinh;
        private ImageView imgDel;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvMaTV = itemView.findViewById(R.id.tvMaTV);
            tvTenTV = itemView.findViewById(R.id.tvTenTV);
            tvNamSinh = itemView.findViewById(R.id.tvNamSinh);
            imgDel = itemView.findViewById(R.id.imgDeleteLS);
        }
    }

}
