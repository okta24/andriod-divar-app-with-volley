package com.shahruie.www.divar.helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shahruie.www.divar.R;

import java.util.List;

/**
 * Created by MR on 08/10/2017.
 */
public class Gride_view_Adapter extends ArrayAdapter<String> {

    private final Activity context;

    private  final List<String>listview_array;
    private final Integer[] imageId;


    public Gride_view_Adapter(Activity context, List<String> listview_array, Integer[] imageId) {
        super(context, R.layout.grid_cell,listview_array);
        this.context = context;
        this.listview_array =listview_array;
        this.imageId = imageId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.grid_cell, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.cell_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.cell_img);
        txtTitle.setText(listview_array.get(position));
        imageView.setImageResource(imageId[position]);
        return rowView;
    }



}
