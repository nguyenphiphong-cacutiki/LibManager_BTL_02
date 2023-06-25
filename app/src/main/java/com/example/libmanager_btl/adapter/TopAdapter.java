package com.example.libmanager_btl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.libmanager_btl.R;
import com.example.libmanager_btl.fragment.TopFragment;
import com.example.libmanager_btl.model.Top;

import java.util.ArrayList;

public class TopAdapter extends ArrayAdapter {
    private Context context;
    TopFragment topFragment;
    ArrayList<Top> lists;
    TextView tvSach,tvSL;
    ImageView imgdel;


    public TopAdapter(@NonNull Context context,TopFragment topFragment, ArrayList<Top> lists) {
        super(context, 0,lists);
        this.context=context;
        this.topFragment =topFragment;
        this.lists = lists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v==null)
        {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v= inflater.inflate(R.layout.top_item,null);//set layout topitem
        }
        final  Top item = lists.get(position);
        if(item !=null)
        {
            tvSach = v.findViewById(R.id.tvSach);
            tvSach.setText("Sach: "+item.getTenSach());

            tvSL =v.findViewById(R.id.tvSL);
            tvSL.setText("so luong"+ item.getSoLuong());
        }
        return v;
    }
}
