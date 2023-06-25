package com.example.libmanager_btl;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libmanager_btl.dao.ThuThuDAO;
import com.example.libmanager_btl.database.DbHelper;

public class LoginActivity extends AppCompatActivity {
    EditText edittextusername,passwordEditText;
    Button buttonLogin;
    CheckBox chkRememberPass;
    String strUser, strPass;
    ThuThuDAO thuThuDAO;
    boolean passwordVisible = false; // Khởi tạo trạng thái ban đầu

    TextView TextSignUp;
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("ĐĂNG NHẬP");
        edittextusername = findViewById(R.id.edittextusername);
        passwordEditText = findViewById(R.id.pass);
        TextSignUp=findViewById(R.id.SignUp);
        //dang ky
        TextSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                        startActivities(new Intent[]{intent});
                    }
                },2000);
            }


        });
        //hide pass
        //   passwordEditText = findViewById(R.id.pass);
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {// xảy ra khi người dùng nhấc tay  khỏi màn hình
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        passwordVisible = !passwordVisible; // Thay đổi trạng thái hiển thị mật khẩu

                        if (passwordVisible) {
                            // Hiển thị mật khẩu
                            passwordEditText.setTransformationMethod(null);
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                        } else {
                            // Ẩn mật khẩu
                            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_remove_red_eye_24, 0);
                        }

                        passwordEditText.setSelection(passwordEditText.getText().length()); //Đảm bảo con trỏ văn bản không thay đổi khi thay đổi trạng thái hiển thị mật khẩu.

                        return true;
                    }
                }
                return false;
            }
        });
        //click login
        setTitle("ĐĂNG NHẬP");
        createData();
        thuThuDAO = new ThuThuDAO(this);
        mapping();
//        read user pass in SharedPreferences
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String user = pref.getString("USERNAME", "");
        String pass = pref.getString("PASSWORD", "");
        Boolean rem = pref.getBoolean("REMEMBER", false);

        if(rem){
            edittextusername.setText(user);
            passwordEditText.setText(pass);
            chkRememberPass.setChecked(rem);
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                i.putExtra("user", strUser);
//                startActivity(i);
                checkLogin();
            }
        });
    }
    private void rememberUser(String u, String p, Boolean status){
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(!status){
            editor.putString("USERNAME", u);
            editor.putString("PASSWORD", p);
            editor.putBoolean("REMEMBER", false);
        }else{
            editor.putString("USERNAME", u);
            editor.putString("PASSWORD", p);
            editor.putBoolean("REMEMBER", true);
        }
        editor.apply();
    }
    public void checkLogin(){
        strUser = edittextusername.getText().toString().trim();
        strPass = passwordEditText.getText().toString().trim();
        if(strUser.isEmpty() || strPass.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập thông tin đầy đủ!", Toast.LENGTH_SHORT).show();
        }else{
            if(thuThuDAO.checkLogin(strUser, strPass) > 0){
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                rememberUser(strUser, strPass, chkRememberPass.isChecked());
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("user", strUser);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(this, "Tên đăng nhập và mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void mapping(){
        edittextusername = findViewById(R.id.edittextusername);
        passwordEditText = findViewById(R.id.pass);
        buttonLogin = findViewById(R.id.buttonLogin);
        chkRememberPass = findViewById(R.id.chkRememberPass);
    }
    private void createData(){

        SharedPreferences sharedPreferences = getSharedPreferences("CREATE_DATA", MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean("IS_CREATE_DATA", false);
        if(!status){
            Log.d("***", "chưa có dl, bắt đầu tạo");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IS_CREATE_DATA", true);
            editor.apply();


            // create data

            String sqlInsertThuThu = "INSERT INTO ThuThu(maTT, hoTen, matKhau) VALUES " +
                    "('thuthu01', 'thu thu 01', '123'), " +
                    "('thuthu02', 'thu thu 02', '1234'), " +
                    "('thuthu03', 'thu thu 03', '12345')";
            //  System.out.printf(sqlInsertThuThu);

            String sqlInsertThanhVien = "insert into ThanhVien(hoTen, namSinh) values " +
                    "('Trần Văn Ngọc', '2001'), " +
                    "('Trần Kiều Ân', '2002'), " +
                    "('Kiều Phong', '2003')";

            DbHelper dbHelper = new DbHelper(this);
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(sqlInsertThuThu);
            sqLiteDatabase.execSQL(sqlInsertThanhVien);

        }else{
            Log.d("***", "đã có dl");
        }
    }
}