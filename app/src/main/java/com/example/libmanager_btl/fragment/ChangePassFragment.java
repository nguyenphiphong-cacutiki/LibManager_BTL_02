package com.example.libmanager_btl.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.dao.ThuThuDAO;
import com.example.libmanager_btl.model.ThuThu;
import com.google.android.material.textfield.TextInputEditText;


public class ChangePassFragment extends Fragment {



//    public ChangePassFragment() {
//        // Required empty public constructor
//    }
//
//
//    public static ChangePassFragment newInstance(String param1, String param2) {
//        ChangePassFragment fragment = new ChangePassFragment();
//
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        View v = infla
//    }

    TextInputEditText edPassOld, edPassNew, edRePassNew;
    Button btSave;
    ThuThuDAO dao;
    TextView tvdonSave;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_change_pass, container, false);
        mapping(v);
        dao = new ThuThuDAO(getContext());
        tvdonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edPassOld.setText("");
                edPassNew.setText("");
                edRePassNew.setText("");
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("USERNAME", "");
                if(validate() > 0){
                    ThuThu thuThu = dao.getWithID(user);
                    thuThu.setMatKhau(edPassNew.getText().toString().trim());
                    int status = dao.updatePass(thuThu);
                    if(status > 0){
                        Toast.makeText(getContext(), "Thay đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        edPassOld.setText("");
                        edPassNew.setText("");
                        edRePassNew.setText("");
                    }else{
                        Toast.makeText(getContext(), "Thay đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        return v;
    }
    private void mapping(View v){
        edPassOld = v.findViewById(R.id.edPassOld);
        edPassNew = v.findViewById(R.id.edPassNew);
        edRePassNew = v.findViewById(R.id.edRePassNew);
        btSave = v.findViewById(R.id.btnSave);
        tvdonSave = v.findViewById(R.id.btDontSave);
    }
    private int validate(){

        int check = 1;
        String oldPass = edPassOld.getText().toString().trim();
        String newPass = edPassNew.getText().toString().trim();
        String newRePass = edRePassNew.getText().toString().trim();
        if(oldPass.isEmpty() || newPass.isEmpty() || newRePass.isEmpty()){
            Toast.makeText(getContext(), "Bạn chưa nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            check = -1;
        }else{
            // get user pass in sharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
            String getOldPass = sharedPreferences.getString("PASSWORD", "");
            if(!getOldPass.equals(oldPass)){
                Toast.makeText(getContext(), "Nhập sai mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                check = -1;
            }else{
                if(!newPass.equals(newRePass)){
                    Toast.makeText(getContext(), "Nhập mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                    check = -1;
                }
            }
        }

        return check;
    }
}