package com.myfoodapp.seminarioproyect.myfoodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myfoodapp.seminarioproyect.myfoodapp.R;
import com.myfoodapp.seminarioproyect.myfoodapp.items.ItemListRes;

import java.util.ArrayList;

public class ListRestAdapter extends BaseAdapter {
    private Context CONTEXT;
    private ArrayList<ItemListRes> LIST;
    public ListRestAdapter(Context context, ArrayList<ItemListRes> list){
       this.CONTEXT = context;
       this.LIST = list;

    }
    @Override
    public int getCount() {
        return this.LIST.size();
    }

    @Override
    public Object getItem(int position) {
        return this.LIST.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflate = (LayoutInflater) this.CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflate.inflate(R.layout.item_buscar_rest, null);

        }

        TextView nameListRest = (TextView)convertView.findViewById(R.id.namelistRest);
        return convertView;
    }
}
