package com.example.libmanager_btl.fragment;

import static com.example.libmanager_btl.model.Mode.MODE_INSERT;
import static com.example.libmanager_btl.model.Mode.MODE_UPDATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.PhieuMuonAdapter;
import com.example.libmanager_btl.adapter.SachSpinnerAdapter;
import com.example.libmanager_btl.adapter.ThanhVienSpinnerAdapter;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.dao.ThanhVienDAO;
import com.example.libmanager_btl.model.Mode;
import com.example.libmanager_btl.model.PhieuMuon;
import com.example.libmanager_btl.model.Sach;
import com.example.libmanager_btl.model.ThanhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class PhieuMuonFragment extends Fragment {
    private RecyclerView rcvPhieuMuon;
    private PhieuMuonAdapter adapter;
    private PhieuMuonDAO phieuMuonDB;
    private FloatingActionButton fab;
    private SimpleDateFormat sdf;
    TextView tvMaPhieuMuon, tvNgay, tvSoLuong, tvTienThue, tvGiaThue;
    Spinner spLoaiSach, spThanhVien;
    CheckBox cbTraSach;
    ImageButton imbPlus, imbMinus;
    Button btSave, btDontSave;
    int maThanhVien;
    int tienThue = 0;
    String maSach;
    int slSachMuon = 1;
    int slSachCon;
    PhieuMuon item = new PhieuMuon();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_phieu_muon, container, false);
        mappingAndInitializeVariable(v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvPhieuMuon.setLayoutManager(linearLayoutManager);
        updateRecyclerView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAndDelete(getContext(), MODE_INSERT);
            }
        });

        // Nhấn giữ để đổ dữ liệu lên dialog
        adapter.setOnItemClickListener(new PhieuMuonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                item = getData().get(position);
                insertAndDelete(getContext(), MODE_UPDATE);
            }
        });

        return v;
    }

    private void mappingAndInitializeVariable(View v) {
        rcvPhieuMuon = v.findViewById(R.id.rcvPhieuMuon);
        adapter = new PhieuMuonAdapter(getContext(), this);
        phieuMuonDB = new PhieuMuonDAO(getContext());
        fab = v.findViewById(R.id.fab);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    private List<PhieuMuon> getData() {
        return phieuMuonDB.getAll();
    }

    private void updateRecyclerView() {
        adapter.setData(getData());
        rcvPhieuMuon.setAdapter(adapter);
    }

    public void delete(String id) {

    }

    public void insertAndDelete(Context context, int type) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_phieu_muon);

        // Mapping variables
        tvMaPhieuMuon = dialog.findViewById(R.id.tvMaPM);
        tvNgay = dialog.findViewById(R.id.tvNgay);
        tvSoLuong = dialog.findViewById(R.id.tvSoLuong);
        tvTienThue = dialog.findViewById(R.id.tvTienThue);
        spLoaiSach = dialog.findViewById(R.id.spSach);
        spThanhVien = dialog.findViewById(R.id.spThanhVien);
        cbTraSach = dialog.findViewById(R.id.chkDaTraSach);
        imbPlus = dialog.findViewById(R.id.btPlusSoLuong);
        imbMinus = dialog.findViewById(R.id.btminusSoLuong);
        btSave = dialog.findViewById(R.id.btSave);
        btDontSave = dialog.findViewById(R.id.btDontSave);
        tvGiaThue = dialog.findViewById(R.id.tvGiaThue);

        // Set default values
        tvSoLuong.setText("1");
        tvNgay.setText(sdf.format(new Date()));

        // Spinner "thanh vien"
        ThanhVienDAO thanhVienDAO = new ThanhVienDAO(context);
        ArrayList<ThanhVien> thanhVienList = (ArrayList<ThanhVien>) thanhVienDAO.getAll();
        ThanhVienSpinnerAdapter thanhVienSpinnerAdapter = new ThanhVienSpinnerAdapter(context, thanhVienList);
        spThanhVien.setAdapter(thanhVienSpinnerAdapter);
        spThanhVien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maThanhVien = thanhVienList.get(position).getMaTV();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner "sach"
        SachDAO sachDAO = new SachDAO(context);
        ArrayList<Sach> sachList = (ArrayList<Sach>) sachDAO.getAll();
        SachSpinnerAdapter sachSpinnerAdapter = new SachSpinnerAdapter(context, sachList);
        spLoaiSach.setAdapter(sachSpinnerAdapter);
        spLoaiSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maSach = String.valueOf(sachList.get(position).getMaSach());
                tienThue = sachList.get(position).getGiaThue() * Integer.parseInt(tvSoLuong.getText().toString().trim());
                tvTienThue.setText("Tiền thuê: " + tienThue);
                tvGiaThue.setText("Giá thuê: " + sachList.get(position).getGiaThue());
                slSachCon = sachList.get(position).getSoLuong();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Edit set data lên form
        if (type != 0) {
            tvMaPhieuMuon.setText(String.valueOf(item.getMaPM()));
            int positiontv = 0;
            for (int i = 0; i < thanhVienList.size(); i++) {
                if (item.getMaTV() == thanhVienList.get(i).getMaTV()) {
                    positiontv = i;
                }
            }
            spThanhVien.setSelection(positiontv);
            for (int i = 0; i < sachList.size(); i++) {
                if (item.getMaTV() == sachList.get(i).getMaSach()) {
                    positiontv = i;
                }
            }
            spLoaiSach.setSelection(positiontv);
            tvNgay.setText("Ngày thuê: " + sdf.format(item.getNgay()));
            tvTienThue.setText("Tiền thuê: " + item.getTienThue());
            if (item.getTraSach() == 1) {
                cbTraSach.setChecked(true);
            } else {
                cbTraSach.setChecked(false);
            }
            ;
        }

        // Change amount
        imbPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldAmount = slSachMuon;
                if (slSachMuon + 1 <= sachDAO.getId(maSach).getSoLuong()) {
                    slSachMuon++;
                    tvSoLuong.setText(String.valueOf(slSachMuon));

                    int tienthuecu = tienThue;
                    int tienhthuemoi = tienthuecu / oldAmount * slSachMuon;
                    tienThue = tienhthuemoi;
                    tvTienThue.setText("Tiền thuê: " + tienhthuemoi);
                } else {
                    Toast.makeText(context, "Không thể mượn nhiều hơn số lượng sách hiện tại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imbMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oldAmount = slSachMuon;
                if (slSachMuon > 1) {
                    slSachMuon--;
                    tvSoLuong.setText(String.valueOf(slSachMuon));

                    int tienthuecu = tienThue;
                    int tienhthuemoi = tienthuecu / oldAmount * slSachMuon;
                    tienThue = tienhthuemoi;
                    tvTienThue.setText("Tiền thuê: " + tienhthuemoi);
                }
            }
        });

        btDontSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhieuMuon item = new PhieuMuon();
                item.setMaSach(Integer.parseInt(maSach));
                item.setMaTV(Integer.parseInt(String.valueOf(maThanhVien)));
                item.setNgay(new Date());
                item.setTienThue(tienThue);
                item.setSoLuong(slSachMuon);
                SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                String mTT = sharedPreferences.getString("user", "");
                item.setMaTT(mTT);
                if (cbTraSach.isChecked())
                    item.setTraSach(1);
                else
                    item.setTraSach(0);
                if (type == MODE_INSERT) {
                    if (phieuMuonDB.insert(item) > 0) {
                        Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (type == MODE_UPDATE) {
                    item.setMaPM(Integer.parseInt(tvMaPhieuMuon.getText().toString().trim()));
                    if (phieuMuonDB.update(item) > 0) {
                        Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                updateRecyclerView();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
