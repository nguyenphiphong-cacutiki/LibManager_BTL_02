package com.example.libmanager_btl.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.ThanhVienAdapter;
import com.example.libmanager_btl.dao.ThanhVienDAO;
import com.example.libmanager_btl.model.ThanhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class ThanhVienFragment extends Fragment {
    private ListView listView;
    private ArrayList<ThanhVien> list;
    static ThanhVienDAO dao;
    ThanhVienAdapter adapter;
    ThanhVien item;
    private FloatingActionButton fab;
    private final int MODE_UPDATE = 1;
    private final int MODE_INSERT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_thanh_vien, container, false);
        // mapping
        listView = v.findViewById(R.id.lvThanhVien);
        fab = v.findViewById(R.id.fab);
        //

        dao = new ThanhVienDAO(getContext());
        capNhatListView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(getContext(), MODE_INSERT);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item = list.get(position);
                openDialog(getContext(), MODE_UPDATE);
                return false;
            }
        });
        return v;


    }
    void capNhatListView(){
        list = (ArrayList<ThanhVien>) dao.getAll();
        adapter = new ThanhVienAdapter(getContext(), this, list);
        listView.setAdapter(adapter);
    }
    public void xoa(final String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa thành viên");
        builder.setMessage("Bạn có muốn xóa không?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.delete(id);
                        capNhatListView();
                        dialog.cancel();
                    }
                }
        );
        builder.setNegativeButton(
                "No",
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
    protected void openDialog(final Context context, final int type){
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
            edMaTV.setText(String.valueOf(item.getMaTV()));
            edTenTV.setText(item.getHoTen());
            edNamSinh.setText(item.getNamSinh());
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
                        if(dao.insert(thanhVien)>0){
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        // type == 1 => update
                        thanhVien.setMaTV(Integer.parseInt(edMaTV.getText().toString().trim()));
                        if(dao.update(thanhVien)>0){
                            Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Sửa thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    capNhatListView();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

}