package com.example.libmanager_btl.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.dao.PhieuMuonDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DoanhThuFragment extends Fragment {
    Button btnTuNgay,btnDenNgay,btnDoanhThu;
    EditText edTuNgay,edDenNgay;
    TextView tvDoanhThu;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    int mYear,mMonth,mDay;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_doanhthu,container,false);
        edTuNgay=v.findViewById(R.id.edTuNgay);
        edDenNgay=v.findViewById(R.id.edDenNgay);
        btnTuNgay=v.findViewById(R.id.btnTuNgay);
        btnDenNgay=v.findViewById(R.id.btnDenNgay);
        btnDoanhThu=v.findViewById(R.id.btnDoanhThu);
        tvDoanhThu=v.findViewById(R.id.tvDoanhThu);
        //set
        btnTuNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c= Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(getActivity(),0,mDateTuNgay,mYear,mMonth,mDay);
                d.show();
            }
        });

        btnDenNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c= Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth=c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d = new DatePickerDialog(getActivity(),0,mDateDenNgay,mYear,mMonth,mDay);
                d.show();
            }
        });

        btnDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tuNgay = edTuNgay.getText().toString();
                String denNgay = edDenNgay.getText().toString();
                PhieuMuonDAO phieuMuonDAO= new PhieuMuonDAO(getActivity());
                try {
                    tvDoanhThu.setText("Doanh Thu: " + phieuMuonDAO.getDoanhThu(tuNgay,denNgay) +"VND");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        // Inflate the layout for this fragment
        return v;
    }
    DatePickerDialog.OnDateSetListener mDateTuNgay =  new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth=monthOfYear;
            mDay = dayOfMonth;
            GregorianCalendar c=  new GregorianCalendar(mYear,mMonth,mDay);
            edTuNgay.setText(sdf.format(c.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener mDateDenNgay =  new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth=monthOfYear;
            mDay = dayOfMonth;
            GregorianCalendar c=  new GregorianCalendar(mYear,mMonth,mDay);
            edDenNgay.setText(sdf.format(c.getTime()));
        }
    };
}