package com.example.libmanager_btl.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.ThanhVienAdapter;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.dao.ThanhVienDAO;
import com.example.libmanager_btl.model.ThanhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;


public class ThanhVienFragment extends Fragment {
    private RecyclerView rcvThanhVien;
    private ThanhVienDAO thanhVienDb;
    private ThanhVienAdapter adapter;
    private FloatingActionButton fab;
    private final int MODE_UPDATE = 1;
    private final int MODE_INSERT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_thanh_vien, container, false);
        mappingAndInitializeVariable(v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvThanhVien.setLayoutManager(linearLayoutManager);
        rcvThanhVien.setAdapter(adapter);
        adapter.setData(getData());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInsertAndUpdate(getContext(), MODE_INSERT,null);
            }
        });

        return v;


    }
    private void mappingAndInitializeVariable(View v){
        rcvThanhVien = v.findViewById(R.id.rcvThanhVien);
        fab = v.findViewById(R.id.fab);
        thanhVienDb = new ThanhVienDAO(getContext());
        adapter = new ThanhVienAdapter(getContext(), this);
    }
    void updateRcv(){
        adapter.setData(getData());
    }
    private List<ThanhVien> getData(){
        return thanhVienDb.getAll();
    }
    public void delete(final String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa thành viên");
        builder.setMessage("Bạn sẽ xóa luôn cả những phiếu mượn của thành viên này?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Đồng ý",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thanhVienDb.delete(id);
                        PhieuMuonDAO phieuMuonDb = new PhieuMuonDAO(getContext());
                        phieuMuonDb.deleteWithMaTV(id);
                        updateRcv();
                        dialog.cancel();
                    }
                }
        );
        builder.setNegativeButton(
                "Hủy",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
//        builder.create();
        builder.show();
    }
    public void dialogInsertAndUpdate(final Context context, final int type, ThanhVien itemUpdate){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_thanh_vien);
        // mapping
        EditText edMaTV = dialog.findViewById(R.id.edAddMaTV);
        TextInputEditText edTenTV = dialog.findViewById(R.id.edAddTenTV);
        TextInputEditText edNamSinh = dialog.findViewById(R.id.edAddNamSinh);
        Button btSave = dialog.findViewById(R.id.btSaveTV);
        Button btCancel = dialog.findViewById(R.id.btDontSaveTV);
        //
        edMaTV.setEnabled(false); // maTV autoincrement
        // check type is insert (0) or update (1)
        if(type == MODE_UPDATE){
            edMaTV.setText(String.valueOf(itemUpdate.getMaTV()));
            edTenTV.setText(itemUpdate.getHoTen());
            edNamSinh.setText(itemUpdate.getNamSinh());
        }
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten = edTenTV.getText().toString().trim();
                String namsinh = edNamSinh.getText().toString().trim();
                if(hoten.isEmpty() || namsinh.isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    ThanhVien thanhVien = new ThanhVien();
                    thanhVien.setHoTen(hoten);
                    thanhVien.setNamSinh(namsinh);

                    if(type == MODE_INSERT){
                        // type = 0 => insert
                        if(thanhVienDb.insert(thanhVien)>0){
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        // type == 1 => update
                        thanhVien.setMaTV(Integer.parseInt(edMaTV.getText().toString().trim()));
                        if(thanhVienDb.update(thanhVien)>0){
                            Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateRcv();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

}