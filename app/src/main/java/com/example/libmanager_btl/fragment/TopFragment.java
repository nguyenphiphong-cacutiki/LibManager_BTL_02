package com.example.libmanager_btl.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.adapter.TopAdapter;
import com.example.libmanager_btl.dao.PhieuMuonDAO;
import com.example.libmanager_btl.model.Top;

import java.util.ArrayList;

public class TopFragment extends Fragment {
    ListView lv;
    ArrayList<Top> list;
    TopAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top,container,false);
        lv = v.findViewById(R.id.lvTop);
        PhieuMuonDAO phieuMuonDAO =new PhieuMuonDAO(getActivity());
        list = (ArrayList<Top>) phieuMuonDAO.getTop(); // goi ham gettop trong phieumuondao do vao list
        adapter= new TopAdapter(getActivity(),this,list);
        lv.setAdapter(adapter);
        return v;
    }
}