package com.shahruie.www.divar;

import java.util.ArrayList;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class simple_adapter extends ArrayAdapter<String> {
	
	private final Activity context;
	 
	private  final String listview_array[];
	private final Integer[] imageId;
	
	
	public simple_adapter(Activity context, String[] listview_array, Integer[] imageId) {
		super(context, R.layout.customlist,listview_array);
		this.context = context;
		this.listview_array =listview_array;
		this.imageId = imageId;
		}
	
	public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.customlist, null, true);
	TextView txtTitle = (TextView) rowView.findViewById(R.id.tonvan3);
	ImageView imageView = (ImageView) rowView.findViewById(R.id.icon2);
	txtTitle.setText(listview_array[position]);
	imageView.setImageResource(imageId[position]);
	return rowView;
	}

	

}
