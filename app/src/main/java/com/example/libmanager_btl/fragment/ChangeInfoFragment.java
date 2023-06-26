package com.example.libmanager_btl.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.dao.ThuThuDAO;
import com.example.libmanager_btl.model.ThuThu;


public class ChangeInfoFragment extends Fragment {
    private EditText edName, edUser, edPass;
    private Button btUpdate;
    private TextView tvCancel;
    private ThuThuDAO thuThuDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_info, container, false);
        mapping(v);
        thuThuDb = new ThuThuDAO(getContext());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        ThuThu old = thuThuDb.getWithID(sharedPreferences.getString("USERNAME", ""));
        edName.setText(old.getHoTen());
        edUser.setText(old.getMaTT());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThuThu old = thuThuDb.getWithID(sharedPreferences.getString("USERNAME", ""));
                edName.setText(old.getHoTen());
                edUser.setText(old.getMaTT());
                edPass.setText("");
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString().trim();
                String user = edUser.getText().toString().trim();
                String pass = edPass.getText().toString().trim();
                if(name.isEmpty() || user.isEmpty() || pass.isEmpty()){
                    Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    if(pass.equals(old.getMatKhau())){
                        ThuThu mNew = new ThuThu();
                        mNew.setMaTT(user);
                        mNew.setHoTen(name);
                        mNew.setMatKhau(pass);
                        if(thuThuDb.updateInfo(old, mNew) > 0){
                            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("USERNAME", mNew.getMaTT());
                            editor.putString("PASSWORD", mNew.getMatKhau());
                            editor.apply();
                            //
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.flContent, new PhieuMuonFragment())
                                    .commit();
                            getActivity().setTitle("Quản lý phiếu mượn");

                        }else{
                            Toast.makeText(getContext(), "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });



        return v;

    }
    private void mapping(View v){
        edName = v.findViewById(R.id.edName);
        edUser = v.findViewById(R.id.edUser);
        edPass = v.findViewById(R.id.edPass);
        btUpdate = v.findViewById(R.id.btUpdate);
        tvCancel = v.findViewById(R.id.tvCancel);
    }
}