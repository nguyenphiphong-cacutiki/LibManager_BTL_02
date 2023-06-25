package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.dao.LoaiSachDAO;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.fragment.SachFragment;
import com.example.libmanager_btl.model.LoaiSach;
import com.example.libmanager_btl.model.Sach;

import java.util.ArrayList;
import java.util.List;

public class SachAdapter extends ArrayAdapter<Sach> implements Filterable {
    private Context context;
    private SachDAO dao;
    private SachFragment sachFragment;
    private List<Sach> list;
    private TextView tvMaSach, tvTenSach, tvMaLoaiSach, tvGiaThue, tvSoLuong;
    private ImageView imgDelete;
    private List<Sach> filteredList; // Danh sách mục đã lọc
    private List<Sach> itemList; private List<Sach> originalList; // Danh sách gốc

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            filteredList.clear(); // Xóa danh sách đã lọc trước đó

            if (originalList == null) {
                originalList = new ArrayList<>(list); // Sao chép danh sách gốc
            }

            if (constraint == null || constraint.length() == 0) {
                // Nếu không có ràng buộc tìm kiếm, trả về toàn bộ danh sách gốc
                filteredList.addAll(originalList);
            } else {
                // Lọc danh sách gốc dựa trên ràng buộc tìm kiếm
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Sach sach : originalList) {
                    if (sach.getTenSach().toLowerCase().contains(filterPattern)) {
                        filteredList.add(sach);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List<Sach>) results.values);
            notifyDataSetChanged();
        }
    };

    public SachAdapter(@NonNull Context context, SachFragment sachFragment, List<Sach> list) {
        super(context, 0, list);
        Log.d("***", "Tạo thành công adapter sach");
        this.context = context;
        this.sachFragment = sachFragment;
        this.list = list;
        this.filteredList = new ArrayList<>();
        this.originalList = new ArrayList<>(list);
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
                    sachFragment.xoa(String.valueOf(sach.getMaSach()));
                }
            });
            Log.d("***", "Đã set dữ liệu lên các view sách");
        }
        return v;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Sach getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<Sach> list){
        this.list = list;
        this.originalList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }




}
