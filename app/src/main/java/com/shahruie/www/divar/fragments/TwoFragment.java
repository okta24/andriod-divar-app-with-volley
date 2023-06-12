package com.shahruie.www.divar.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.shahruie.www.divar.Adv_by_sub_cat;
import com.shahruie.www.divar.R;
import com.shahruie.www.divar.helper.DatabaseAccess;
import com.shahruie.www.divar.helper.Gride_view_Adapter;

import java.util.List;


public class TwoFragment extends Fragment {


    Integer[] imageId = { R.drawable.ic_home,
            R.drawable.ic_car,R.drawable.ic_device,
            R.drawable.ic_home,R.drawable.ic_setting,
            R.drawable.ic_game,R.drawable.ic_sotial,
            R.drawable.ic_emploe,R.drawable.ic_emploe  };
    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_two, container, false);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
        databaseAccess.open();
        final List<String> categories = databaseAccess.get_parent_category_name();
        databaseAccess.close();

        final GridView gridView = (GridView)view.findViewById(R.id.categiry_list);
        gridView.setAdapter(new Gride_view_Adapter(getActivity(),categories,imageId));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int parent_cat_id=++position;
                String title=categories.get(parent_cat_id);

                Intent intent=new Intent(getActivity(),Adv_by_sub_cat.class);
                intent.putExtra("p_cat_id",parent_cat_id);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
