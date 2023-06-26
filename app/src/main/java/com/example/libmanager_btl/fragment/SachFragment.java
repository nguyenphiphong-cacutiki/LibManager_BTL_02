package com.example.libmanager_btl.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.LoaiSachSpinnerAdapter;
import com.example.libmanager_btl.adapter.SachAdapter;
import com.example.libmanager_btl.dao.LoaiSachDAO;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.model.LoaiSach;
import com.example.libmanager_btl.model.Sach;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class SachFragment extends Fragment {

    private final int MODE_INSERT = 0;
    private final int MODE_UPDATE = 1;

    private RecyclerView rcvSach;
    private FloatingActionButton fab;

    //sach
    private Sach itemSach;
    private SachDAO sachDb;
    private SachAdapter adapter;
    private EditText edSearch;



    // spinner
    LoaiSachSpinnerAdapter spinnerAdapter;
    ArrayList<LoaiSach> listLoaiSach;
    LoaiSachDAO loaiSachDb;

    // save info when select item of spinner loaiSach
    int maLoaiSach, position;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sach, container, false);
        mappingAndInitializeVariable(v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvSach.setLayoutManager(linearLayoutManager);
        rcvSach.setAdapter(adapter);
        adapter.setData(getData());
        //
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddAndUpdate(getContext(), MODE_INSERT, null);
            }
        });
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.setData(searchSach(getData(), s.toString()));
            }
        });

        return v;
    }

    private void mappingAndInitializeVariable(View v) {
        fab = v.findViewById(R.id.fabAddSach);
        edSearch = v.findViewById(R.id.edSearch);
        sachDb = new SachDAO(getContext());

        rcvSach = v.findViewById(R.id.rcvSach);
        adapter = new SachAdapter(getContext(), this);

    }
    private List<Sach> searchSach(List<Sach> list, String s){
        List<Sach> res = new ArrayList<>();
        for( Sach item : list){
            if(item.getTenSach().trim().toLowerCase().contains(s.trim().toLowerCase())){
                res.add(item);
            }
        }
        return res;
    }

    public void updateRcv(){
        adapter.setData(getData());
    }
    public List<Sach> getData(){
        return sachDb.getAll();
    }
    public void delete(final String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa Sách");
        builder.setMessage("Bạn có muốn xóa không?");
        builder.setCancelable(false);
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
        builder.setPositiveButton(
                "yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sachDb.delete(id);
                        updateRcv();
                        dialog.cancel();
                    }
                }
        );
        builder.show();
    }
    public void dialogAddAndUpdate(Context context, int type, Sach itemUpdate){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_sach);
        //mapping
        EditText edMaSach = dialog.findViewById(R.id.edMaSach);
        EditText edTenSach = dialog.findViewById(R.id.edTenSach);
        EditText edGiaThue = dialog.findViewById(R.id.edGiaThue);
        Spinner spinner = dialog.findViewById(R.id.spLoaiSach);
        Button btDontSave = dialog.findViewById(R.id.btDontSaveSach);
        Button btSave = dialog.findViewById(R.id.btSaveSach);
        EditText edSoLuong = dialog.findViewById(R.id.edSoLuong);
        ImageButton btPlusSoLuong = dialog.findViewById(R.id.btPlusSoLuong);
        ImageButton btMinusSoLuong = dialog.findViewById(R.id.btminusSoLuong);
        //
        listLoaiSach = new ArrayList<>();
        loaiSachDb = new LoaiSachDAO(context);
        listLoaiSach = (ArrayList<LoaiSach>) loaiSachDb.getAll();
        Log.d("***","có số loại sách: "+ listLoaiSach.size());
        spinnerAdapter = new LoaiSachSpinnerAdapter(context, listLoaiSach);
        spinner.setAdapter(spinnerAdapter);

        //
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maLoaiSach = listLoaiSach.get(position).getMaLoai();
                //Toast.makeText(context,"Chọn: "+ listLoaiSach.get(position).getTenLoai(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edMaSach.setEnabled(false);
        if(type == MODE_UPDATE){
            edMaSach.setText(String.valueOf(itemUpdate.getMaSach()));
            edTenSach.setText(itemUpdate.getTenSach());
            edGiaThue.setText(String.valueOf(itemUpdate.getGiaThue()));
            edSoLuong.setText(String.valueOf(itemUpdate.getSoLuong()));
            edSoLuong.setEnabled(false);
            btPlusSoLuong.setVisibility(View.VISIBLE);
            btMinusSoLuong.setVisibility(View.VISIBLE);
            for(int i = 0; i < listLoaiSach.size(); i++){
                if(itemUpdate.getMaLoai() == listLoaiSach.get(i).getMaLoai()){
                    position = i;
                }
            }
            spinner.setSelection(position);
            btPlusSoLuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogChangeSoLuong(1, edSoLuong);
                }
            });
            btMinusSoLuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogChangeSoLuong(-1, edSoLuong);

                }
            });
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
                String tenSach = edTenSach.getText().toString().trim();
                String giaThue = edGiaThue.getText().toString().trim();
                String soLuong = edSoLuong.getText().toString().trim();
                if(tenSach.isEmpty() || giaThue.isEmpty() || soLuong.isEmpty()){
                    Toast.makeText(context, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                }else{
                    itemSach = new Sach();
                    itemSach.setTenSach(tenSach);
                    itemSach.setMaLoai(maLoaiSach);
                    itemSach.setGiaThue(Integer.parseInt(giaThue));
                    itemSach.setSoLuong(Integer.parseInt(soLuong));

                    if(type == MODE_INSERT){
                        if(sachDb.insert(itemSach)>0){
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }else if(type == MODE_UPDATE){
                        itemSach.setMaSach(Integer.parseInt(edMaSach.getText().toString().trim()));
                        if(sachDb.update(itemSach) > 0){
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
    private void dialogChangeSoLuong(int type, EditText edSlCu){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_amount_sach);
        EditText edSoLuong = dialog.findViewById(R.id.edSoLuong);
        Button btOk = dialog.findViewById(R.id.btOk);
        Button btCancel = dialog.findViewById(R.id.btCancel);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String strSl = edSoLuong.getText().toString().trim();
               if(strSl.isEmpty()){
                   Toast.makeText(getContext(), "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
               }else{
                   int slCu = Integer.parseInt(edSlCu.getText().toString().trim());
                   int slmoi = Integer.parseInt(strSl);
                   if(type> 0){
                        edSlCu.setText((slCu + slmoi) + "");
                   }else{
                       if(slmoi >= slCu){
                           edSlCu.setText("0");
                       }else{
                           edSlCu.setText((slCu - slmoi) + "");
                       }
                   }
                   dialog.dismiss();
               }
            }
        });
        dialog.show();
    }
}