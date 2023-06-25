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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.LoaiSachAdapter;
import com.example.libmanager_btl.adapter.LoaiSachSpinnerAdapter;
import com.example.libmanager_btl.dao.LoaiSachDAO;
import com.example.libmanager_btl.model.LoaiSach;
import com.example.libmanager_btl.model.Sach;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class LoaiSachFragment extends Fragment {
    private RecyclerView rcvLoaiSach;
    private FloatingActionButton fab;
    private LoaiSachAdapter adapter;
    private LoaiSachDAO loaiSachDb;
    public static final int MODE_INSERT = 0;
    public static final int MODE_UPDATE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_loai_sach, container, false);
        mappingAndInitializeVariable(v);
        updateRecycleView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddOrInsert(getContext(), MODE_INSERT, null);
            }
        });





        return v;
    }
    private void mappingAndInitializeVariable(View v){
        rcvLoaiSach = v.findViewById(R.id.rcvLoaiSach);
        fab  = v.findViewById(R.id.fab);
        adapter = new LoaiSachAdapter(getContext(), this);
        loaiSachDb = new LoaiSachDAO(getContext());
        // set up rcv manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvLoaiSach.setLayoutManager(linearLayoutManager);
    }
    private void updateRecycleView(){
        adapter.setData(getData());
        rcvLoaiSach.setAdapter(adapter);
    }

    private List<LoaiSach> getData() {
        return loaiSachDb.getAll();
    }
    public void deleteItem(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có muốn xóa không?");
        builder.setCancelable(false);
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loaiSachDb.delete(id);
                        updateRecycleView();
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }
    public void dialogAddOrInsert(Context context, int type, LoaiSach itemLoaiSach){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_loai_sach);

        //mapping
        EditText edMaLoaiSach = dialog.findViewById(R.id.edMaLoaiSach);
        EditText edTenLoaiSach = dialog.findViewById(R.id.edTenLoaiSach);
        Button btDontSave = dialog.findViewById(R.id.btDontSaveLoaiSach);
        Button btSave = dialog.findViewById(R.id.btSaveLoaiSach);

        //
        edMaLoaiSach.setEnabled(false);
        if(type == MODE_UPDATE){
            edMaLoaiSach.setText(String.valueOf(itemLoaiSach.getMaLoai()));
            edTenLoaiSach.setText(itemLoaiSach.getTenLoai());
        }
        btDontSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenLoaiSach = edTenLoaiSach.getText().toString().trim();
                if(tenLoaiSach.isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                }else{
                    LoaiSach itemLoaiSach = new LoaiSach();
                    itemLoaiSach.setTenLoai(tenLoaiSach);

                    if(type == MODE_INSERT){
                        if(loaiSachDb.insert(itemLoaiSach)>0){
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }else if(type == MODE_UPDATE){
                        itemLoaiSach.setMaLoai(Integer.parseInt(edMaLoaiSach.getText().toString().trim()));
                        if(loaiSachDb.update(itemLoaiSach) > 0){
                            Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateRecycleView();
                    dialog.dismiss();
                }



            }
        });
        dialog.show();
    }
}