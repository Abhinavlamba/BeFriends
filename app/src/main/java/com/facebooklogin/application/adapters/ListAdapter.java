package com.facebooklogin.application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebooklogin.application.R;
import com.facebooklogin.application.entities.Item;
import com.facebooklogin.application.onItemClick;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Item> {

    private int resourceLayout;
    private Context mContext;
    private onItemClick mCallback;

    public ListAdapter(Context context, int resource, List<Item> items, onItemClick listener) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        this.mCallback = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Item p = getItem(position);
        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.skills);
            RadioButton tt2 = v.findViewById(R.id.radiobutton);
            tt1.setText(p.getSkills());
            if (p.getChecked())
            {
                tt2.setChecked(true);
                mCallback.onClick(Integer.parseInt(p.getId()));
            }
            else
                tt2.setChecked(false);
            tt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (p.getChecked())
                    {
                        tt2.setChecked(false);
                        p.setChecked(false);
                    }
                    else
                    {
                        tt2.setChecked(true);
                        p.setChecked(true);
                    }
                    mCallback.onClick(Integer.parseInt(p.getId()));
                }
            });
        }
        return v;
    }

}
