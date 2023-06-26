package com.example.libmanager_btl.fragment;

import static com.example.libmanager_btl.model.Mode.MODE_INSERT;
import static com.example.libmanager_btl.model.Mode.MODE_UPDATE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.example.libmanager_btl.model.PhieuMuon;
import com.example.libmanager_btl.model.Sach;
import com.example.libmanager_btl.model.ThanhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private List<PhieuMuon> phieuMuonList;
    boolean setUpLanDauChoChucNangUpdate = false;
    boolean trangThaiMuonSachCu = false;
    private int slSachMuonCu = 0;
    private int maSachMuonCu = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_phieu_muon, container, false);
        mappingAndInitializeVariable(v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvPhieuMuon.setLayoutManager(linearLayoutManager);
        updateRecyclerView();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delete(phieuMuonList.get(viewHolder.getAdapterPosition()).getMaPM()+"", viewHolder.getAdapterPosition());
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAndUpdate(getContext(), MODE_INSERT, null);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvPhieuMuon);

        return v;
    }

    private void mappingAndInitializeVariable(View v) {
        rcvPhieuMuon = v.findViewById(R.id.rcvPhieuMuon);
        adapter = new PhieuMuonAdapter(getContext(), this);
        phieuMuonDB = new PhieuMuonDAO(getContext());
        fab = v.findViewById(R.id.fab);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        phieuMuonList = new ArrayList<>();
        phieuMuonList = getData();
    }

    private List<PhieuMuon> getData() {
        return phieuMuonDB.getAll();
    }

    private void updateRecyclerView() {
        adapter.setData(getData());
        rcvPhieuMuon.setAdapter(adapter);
    }

    public void delete(String id, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có muốn xóa không?");
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                }
        );
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        phieuMuonDB.delete(id);
                        phieuMuonList.remove(pos);
                        adapter.deleteItemOfList(pos);
                        adapter.notifyItemRemoved(pos);
                        //updateRecyclerView();

                    }
                }
        );
        builder.show();
    }

    public void insertAndUpdate(Context context, int type, PhieuMuon itemUpdate) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_phieu_muon);

        // Mapping variables
        tvMaPhieuMuon = dialog.findViewById(R.id.edMaPhieuMuon);
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
        setUpLanDauChoChucNangUpdate = type == MODE_UPDATE;

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
                if(!setUpLanDauChoChucNangUpdate){
                    if(sachList.get(position).getSoLuong() >= 1){
                        maSach = String.valueOf(sachList.get(position).getMaSach());
                        tvSoLuong.setText("1");
                        slSachMuon = 1;
                        tienThue = sachList.get(position).getGiaThue() * Integer.parseInt(tvSoLuong.getText().toString().trim());
                        tvTienThue.setText("Tiền thuê: " + tienThue);
                        tvGiaThue.setText("Giá thuê: " + sachList.get(position).getGiaThue());
                        slSachCon = sachList.get(position).getSoLuong();
                    }else{
                        tvSoLuong.setText("0");
                        Toast.makeText(context, "Sách này đã hết, vui lòng chọn sách khác!", Toast.LENGTH_SHORT).show();
                    }
                }else
                    setUpLanDauChoChucNangUpdate = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (type == MODE_UPDATE) {
            tvMaPhieuMuon.setText(String.valueOf(itemUpdate.getMaPM()));
            // set data for sp thanh vien
            int positionTV = 0;
            for (int i = 0; i < thanhVienList.size(); i++) {
                if (itemUpdate.getMaTV() == thanhVienList.get(i).getMaTV()) {
                    positionTV = i;
                }
            }
            spThanhVien.setSelection(positionTV);
            // set data for sp sach
            int positionSach = 0;
            for (int i = 0; i < sachList.size(); i++) {
                if (itemUpdate.getMaSach() == sachList.get(i).getMaSach()) {
                    positionSach = i;
                }
            }
            spLoaiSach.setSelection(positionSach);
            //
            tvNgay.setText(sdf.format(itemUpdate.getNgay()));
            tvTienThue.setText("Tiền thuê: " + item.getTienThue());
            if (itemUpdate.getTraSach() == 1) {
                cbTraSach.setChecked(true);
            } else {
                cbTraSach.setChecked(false);
            }
            tvSoLuong.setText(String.valueOf(itemUpdate.getSoLuong()));
            tvGiaThue.setText("Giá thuê: "+ sachList.get(positionSach).getGiaThue());
            // set variable
            slSachMuon = itemUpdate.getSoLuong();
            maSach = String.valueOf(sachList.get(positionSach).getMaSach());
            slSachCon = sachList.get(positionTV).getSoLuong();
            tienThue = sachList.get(positionSach).getGiaThue() * Integer.parseInt(tvSoLuong.getText().toString().trim());
            tvTienThue.setText("Tiền thuê: " + tienThue);
            slSachMuonCu = slSachMuon; // phải dùng biến này vì biến slSachMuon có thể bị thay đổi
            trangThaiMuonSachCu = cbTraSach.isChecked();

            slSachMuonCu = itemUpdate.getSoLuong();
            maSachMuonCu = itemUpdate.getMaSach();
            //tvSoLuong.setText("10000");

            //Log.d("***","Số lượng: "+ String.valueOf(itemUpdate.getSoLuong()));
        }else{
            cbTraSach.setVisibility(View.GONE);
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
                if(Integer.parseInt(tvSoLuong.getText().toString().trim()) > 0){
                    PhieuMuon item = new PhieuMuon();
                    item.setMaSach(Integer.parseInt(maSach));
                    item.setMaTV(Integer.parseInt(String.valueOf(maThanhVien)));
                    item.setTienThue(tienThue);
                    item.setSoLuong(slSachMuon);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                    String mTT = sharedPreferences.getString("user", "");
                    item.setMaTT(mTT);
                    try {
                        item.setNgay(sdf.parse(tvNgay.getText().toString().trim()));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    if (cbTraSach.isChecked())
                        item.setTraSach(PhieuMuon.DA_TRA);
                    else
                        item.setTraSach(PhieuMuon.CHUA_TRA);
                    if (type == MODE_INSERT) {
                        if (phieuMuonDB.insert(item) > 0) {
                            // thay đổi số lượng trong bảng sách
                            SachDAO sachDb = new SachDAO(context);
                            Sach sach = sachDb.getId(item.getMaSach()+"");
                            sach.setSoLuong(sach.getSoLuong() - item.getSoLuong());
                            sachDb.update(sach);

                            Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (type == MODE_UPDATE) {
                        item.setMaPM(Integer.parseInt(tvMaPhieuMuon.getText().toString().trim()));
                        // thay đổi số lượng trong bảng sách
                        SachDAO sachDb = new SachDAO(context);
                        Sach sach = sachDb.getId(item.getMaSach()+"");
                        boolean capNhat = true;
                        if(!trangThaiMuonSachCu && cbTraSach.isChecked()){
                            // từ đang mượn thành trả sách
                            if(slSachMuonCu != item.getSoLuong() || maSachMuonCu != item.getMaSach())
                                //trường hợp này không cho thay đổi thông tin sách và số lượng
                                capNhat = false;
                            sach.setSoLuong(sach.getSoLuong() + item.getSoLuong());
                            sachDb.update(sach);
                        }else if(trangThaiMuonSachCu && !cbTraSach.isChecked()){
                            // trạng thái đã trả thành mượn sách
                            if(maSachMuonCu != item.getMaSach())
                                //trường hợp này không cho thay đổi thông tin sách
                                capNhat = false;
                            if(sach.getSoLuong() >= item.getSoLuong()){
                                //ok mượn được
                                sach.setSoLuong(sach.getSoLuong() - item.getSoLuong());
                                sachDb.update(sach);

                            }else{
                                // không đủ sách để mượn
                                capNhat = false;
                                Toast.makeText(context, "không đủ sách để mượn!", Toast.LENGTH_SHORT).show();

                            }
                        }else if(trangThaiMuonSachCu && cbTraSach.isChecked()){
                            // giữ nguyên thông tin trả sách (đã trả), thay đổi thông tin khác
                            // đã trả rồi thì không thay đổi thông tin trả
                            capNhat = false;


                        }else if(!trangThaiMuonSachCu && !cbTraSach.isChecked()){
                            // giữ nguyên thông tin trả sách (chưa trả), thay đổi thông tin khác
                            if(sach.getMaSach() == maSachMuonCu){
                                // chỉ thay đổi số lượng
                                sach.setSoLuong(sach.getSoLuong() + slSachMuonCu - item.getSoLuong());
                                sachDb.update(sach);
                            }else{
                                // thay đổi sách mượn
                                // trả lại sách mượn trước
                                Sach sachCu = sachDb.getId(maSachMuonCu+"");
                                sachCu.setSoLuong(sachCu.getSoLuong() + slSachMuonCu);
                                sachDb.update(sachCu);
                                // thay đổi số lượng của sách mới
                                sach.setSoLuong(sach.getSoLuong()-item.getSoLuong());
                                sachDb.update(sach);
                            }

                        }
                        if(!capNhat){
                            Toast.makeText(context, "Thay đổi thông tin không hợp lệ!", Toast.LENGTH_SHORT).show();
                        }
                        else if(phieuMuonDB.update(item) > 0) {
                            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    updateRecyclerView();
                }


                dialog.dismiss();
            }
        });
        tvNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c= Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth=c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(getActivity(), 0,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                GregorianCalendar c_ =  new GregorianCalendar(year,month,dayOfMonth);
                                tvNgay.setText(sdf.format(c_.getTime()));
                            }
                        }
                        , mYear, mMonth, mDay);
                d.show();
            }
        });
        dialog.show();
    }

}
