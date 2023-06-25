package com.example.libmanager_btl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libmanager_btl.dao.ThuThuDAO;

public class SignUpActivity extends AppCompatActivity {
    ThuThuDAO thuThuDAO;
    EditText edittextusername, edittextpassword,edittextNameAccount;
    Button buttonSignup;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        edittextusername = findViewById(R.id.edittextusername);
        edittextpassword = findViewById(R.id.edittextpassword);
        edittextNameAccount =findViewById(R.id.edittextNameAccount);
        buttonSignup = findViewById(R.id.buttonSignup);
        thuThuDAO = new ThuThuDAO(this);
        final boolean[] passwordVisible = {false}; // Khởi tạo trạng thái ban đầu


        edittextpassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {// xảy ra khi người dùng nhấc tay  khỏi màn hình
                    if (event.getRawX() >= (edittextpassword.getRight() - edittextpassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        passwordVisible[0] = !passwordVisible[0]; // Thay đổi trạng thái hiển thị mật khẩu

                        if (passwordVisible[0]) {
                            // Hiển thị mật khẩu
                            edittextpassword.setTransformationMethod(null);
                            edittextpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                        } else {
                            // Ẩn mật khẩu
                            edittextpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            edittextpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
                        }

                        edittextpassword.setSelection(edittextpassword.getText().length()); //Đảm bảo con trỏ văn bản không thay đổi khi thay đổi trạng thái hiển thị mật khẩu.

                        return true;
                    }
                }
                return false;
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edittextusername.getText().toString();
                String nameAccount = edittextNameAccount.getText().toString();
                String password = edittextpassword.getText().toString();
                if (user.isEmpty() || password.isEmpty() || nameAccount.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean insert = thuThuDAO.checkInsert(nameAccount,user, password);
                    if (insert) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
