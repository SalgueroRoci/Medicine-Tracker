package edu.fullerton.teamobile.med;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Source code: https://stackoverflow.com/questions/17525886/listview-with-add-and-delete-buttons-in-each-row-in-android
 * Created by: adam83
 */

public class MyAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;
    private List<String> stage;


    public MyAdapter( Context context, List<String> list, List<String> redbg) {
        this.list = list;
        this.context = context;
        this.stage = redbg;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }


    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custome_list, null);
        }

        if(stage.get(position) == "false")
        {
            // Set a background color for ListView regular row/item
            view.setBackgroundColor(Color.parseColor("#00000000"));
        }
        else
        {
            // Set the background color for alternate row/item
            view.setBackgroundColor(Color.parseColor("#b5433f"));
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button addBtn = (Button)view.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                notifyDataSetChanged();
                String name = String.valueOf(list.get(position));
                Intent info = new Intent(context, MedInfo.class);
                info.putExtra("medname", name);
                context.startActivity(info);
            }
        });

        return view;
    }
}
