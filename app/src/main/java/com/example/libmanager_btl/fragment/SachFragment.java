package com.example.libmanager_btl.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.LoaiSachSpinnerAdapter;
import com.example.libmanager_btl.adapter.SachAdapter;
import com.example.libmanager_btl.dao.LoaiSachDAO;
import com.example.libmanager_btl.dao.SachDAO;
import com.example.libmanager_btl.model.LoaiSach;
import com.example.libmanager_btl.model.Sach;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SachFragment extends Fragment {

    private final int MODE_INSERT = 0;
    private final int MODE_UPDATE = 1;

    private ListView listView;
    private FloatingActionButton fab;

    //sach
    private SachAdapter sachAdapter;
    private Sach itemSach;
    private List<Sach> listSach;
    private SachDAO sachDb;
    private SearchView searchView;
    private SachAdapter adapter;

    // spinner
    LoaiSachSpinnerAdapter spinnerAdapter;
    ArrayList<LoaiSach> listLoaiSach;
    LoaiSachDAO loaiSachDb;
    LoaiSach itemLoaiSach;

    int maLoaiSach, position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sach, container, false);
        listView = v.findViewById(R.id.lvSach);
        fab = v.findViewById(R.id.fabAddSach);
        sachDb = new SachDAO(getContext());

        capNhatListView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit(getContext(), MODE_INSERT);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemSach = listSach.get(position);
                dialogEdit(getContext(), MODE_UPDATE);
                return false;
            }
        });

        EditText etTimkiemDH = v.findViewById(R.id.etTimkiemSanPham);

       // searchView.clearFocus();
        etTimkiemDH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (adapter != null) {
//                    adapter.setData(getListSachs(), s.toString());
//                    adapter.notifyDataSetChanged();
//                }
                {adapter.setData(timkiemSach(getListSachs(),s.toString()));}
            //    adapter.setData(timkiemSach(sachDb.filter(s.toString()));
            }
        });

        return v;
    }
    private ArrayList<Sach> timkiemSach(ArrayList<Sach> list, String s){
        ArrayList<Sach> list1 = new ArrayList<>();
        for( Sach item : list){
            if(item.getTenSach().trim().toLowerCase().contains(s.trim().toLowerCase())){
                list1.add(item);
            }
        }
        return list1;
    }

    private ArrayList<Sach> getListSachs() {
        return (ArrayList<Sach>)sachDb.getAll();
    }

    public void capNhatListView() {
        listSach = sachDb.getAll();
        Log.d("***", "Số sách có: " + listSach.size());
        sachAdapter = new SachAdapter(getActivity(), this, listSach);
        adapter = sachAdapter; // Gán sachAdapter cho biến adapter
        listView.setAdapter(sachAdapter);
    }


    public void xoa(final String id) {
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
                        capNhatListView();
                        dialog.cancel();
                    }
                }
        );
        builder.show();
    }

    public void dialogEdit(Context context, int type) {
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
        Log.d("***", "có số loại sách: " + listLoaiSach.size());
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
        if (type == MODE_UPDATE) {
            edMaSach.setText(String.valueOf(itemSach.getMaSach()));
            edTenSach.setText(itemSach.getTenSach());
            edGiaThue.setText(String.valueOf(itemSach.getGiaThue()));
            edSoLuong.setText("" + itemSach.getSoLuong());
            edSoLuong.setEnabled(false);
            btPlusSoLuong.setVisibility(View.VISIBLE);
            btMinusSoLuong.setVisibility(View.VISIBLE);
            for (int i = 0; i < listLoaiSach.size(); i++) {
                if (itemSach.getMaLoai() == listLoaiSach.get(i).getMaLoai()) {
                    position = i;
                }
            }
            spinner.setSelection(position);
            btPlusSoLuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(edSoLuong.getText().toString());
                    quantity++;
                    edSoLuong.setText(String.valueOf(quantity));
                }
            });
            btMinusSoLuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(edSoLuong.getText().toString());
                    if (quantity > 0) {
                        quantity--;
                        edSoLuong.setText(String.valueOf(quantity));
                    }
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
                if (tenSach.isEmpty() || giaThue.isEmpty() || soLuong.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    itemSach = new Sach();
                    itemSach.setTenSach(tenSach);
                    itemSach.setMaLoai(maLoaiSach);
                    itemSach.setGiaThue(Integer.parseInt(giaThue));
                    itemSach.setSoLuong(Integer.parseInt(soLuong));

                    if (type == MODE_INSERT) {
                        if (sachDb.insert(itemSach) > 0) {
                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (type == MODE_UPDATE) {
                        itemSach.setMaSach(Integer.parseInt(edMaSach.getText().toString().trim()));
                        if (sachDb.update(itemSach) > 0) {
                            Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        } else {
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
