package com.example.libmanager_btl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libmanager_btl.dao.ThuThuDAO;

public class SignUpActivity extends AppCompatActivity {
    private ThuThuDAO thuThuDb;
    private EditText edName, edPass, edUserName;
    private Button btSignUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edName = findViewById(R.id.edittextusername);
        edPass = findViewById(R.id.edittextpassword);
        edUserName =findViewById(R.id.edittextNameAccount);
        btSignUp = findViewById(R.id.buttonSignup);
        thuThuDb = new ThuThuDAO(this);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edName.getText().toString().trim();
                String name = edUserName.getText().toString().trim();
                String password = edPass.getText().toString().trim();
                if (user.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean insert = thuThuDb.checkInsert(name,user, password);
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
