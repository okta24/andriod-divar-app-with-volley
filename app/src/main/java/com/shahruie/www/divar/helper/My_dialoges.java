package com.shahruie.www.divar.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import com.shahruie.www.divar.R;

import java.util.List;

/**
 * Created by MR on 08/07/2017.
 */
public class My_dialoges {
    Context context;
    ArrayAdapter<String> adapter;
    static  int city_id=1;
    public My_dialoges(Context context)
    {
        this.context=context;
    }
    public  void  show_city_list(){
        final Dialog city_dialog = new Dialog(context);
        city_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        city_dialog.setContentView(R.layout.city_list_dialog);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        List<String> citys = databaseAccess.get_city_name();
        databaseAccess.close();
        adapter = new ArrayAdapter<String>(context,R.layout.city_list_row,R.id.city_name, citys);
        ListView city_list=(ListView)city_dialog.findViewById(R.id.city_list);
        // ArrayAdapter arrayAdapter;
        // arrayAdapter = new ArrayAdapter(this,R.layout.city_list_row, R.id.city_name, city);
        city_list.setAdapter(adapter);

        city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // MainActivity.city_id=position++;
                city_dialog.dismiss();
            }
        });
        EditText et_city_search = (EditText) city_dialog.findViewById(R.id.et_city_search);
        et_city_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

               adapter.getFilter().filter(s);
            }
        });

        city_dialog.show();
        city_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

    }
}
