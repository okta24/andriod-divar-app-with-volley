package com.shahruie.www.divar.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shahruie.www.divar.Ok_No_Dialog;
import com.shahruie.www.divar.Adv;
import com.shahruie.www.divar.My_Adv_List;
import com.shahruie.www.divar.My_adv_dialog;
import com.shahruie.www.divar.R;
import com.shahruie.www.divar.helper.About;
import com.shahruie.www.divar.helper.DatabaseAccess;

import java.util.ArrayList;
import java.util.List;


public class FourFragment extends Fragment {
    public static boolean log_out_flag;
    Button btn_city;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public static List<Adv> adv_list =new ArrayList<Adv>();
     Button btn_exit;
    Button btn_my_adv;
    public FourFragment() {
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
        View view= inflater.inflate(R.layout.fragment_four, container, false);

        pref = getActivity().getApplicationContext().getSharedPreferences("setting",getActivity().MODE_PRIVATE);
        editor = pref.edit();
        btn_my_adv=(Button)view.findViewById(R.id.btn_myadv);
        btn_city=(Button)view.findViewById(R.id.btn_city);
        Button btn_about=(Button)view.findViewById(R.id.btn_about);
        btn_exit=(Button)view.findViewById(R.id.btn_exit);


        //===========================
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.getBoolean("Is_login",false)) {
                    log_out_flag=false;
                    final Ok_No_Dialog log_out=new Ok_No_Dialog(getActivity(),"خروج از حساب کاربری","ایا قصد خروج از حساب کاربری خود را دارید؟");
                    log_out.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    log_out.getWindow().getAttributes().windowAnimations=R.style.PauseDialogAnimation2;
                    log_out.setContentView(R.layout.ok_no_dialog);
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    WindowManager.LayoutParams lp = log_out.getWindow().getAttributes();
                    lp.dimAmount = 0.7f;
                    lp.width=LinearLayout.LayoutParams.WRAP_CONTENT;
                    lp.height=LinearLayout.LayoutParams.WRAP_CONTENT;
                    lp.gravity=Gravity.CENTER;
                    log_out.getWindow().setAttributes(lp);
                    log_out.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    log_out.show();
                    log_out.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(log_out_flag){
                                editor.putBoolean("Is_login", false);
                                editor.commit();
                                btn_exit.setText("ورود");
                                btn_my_adv.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "شما از حساب کاربری خارج شدید!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }else{
                    My_adv_dialog my_adv=new My_adv_dialog(getActivity());
                    my_adv.show();
                    my_adv.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(pref.getBoolean("Is_login",false)) {
                                set_login_title();
                                btn_my_adv.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "شما وارد حساب کاربری خود شدید", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        //===========================
        set_title();
        ///========================\\
        btn_my_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),My_Adv_List.class);
                startActivity(intent);
            }

        });
        //============================
        btn_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_city_list();

            }
        });
        //============================
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Ok_No_Dialog about=new Ok_No_Dialog(getActivity(),"درباره ما","");
                about.requestWindowFeature(Window.FEATURE_NO_TITLE);
                about.getWindow().getAttributes().windowAnimations=R.style.PauseDialogAnimation2;
                about.setContentView(R.layout.ok_no_dialog);
                TextView btn_ok=(TextView)about.findViewById(R.id.btn_ok);
                btn_ok.setVisibility(View.GONE);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                WindowManager.LayoutParams lp = about.getWindow().getAttributes();
                lp.dimAmount = 0.7f;
                lp.width=LinearLayout.LayoutParams.WRAP_CONTENT;
                lp.height=LinearLayout.LayoutParams.WRAP_CONTENT;
                lp.gravity=Gravity.CENTER;
                about.getWindow().setAttributes(lp);
                about.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                about.show();*/
                About about=new About(getActivity());
                about.show();

            }
        });
        return  view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onResume() {
        if(pref.getBoolean("Is_login",false)){
            btn_my_adv.setVisibility(View.VISIBLE);
            set_login_title();
        }else {
            btn_my_adv.setVisibility(View.GONE);
            btn_exit.setText("ورود");

        }
        super.onResume();
    }

    private void set_login_title() {
        String str=pref.getString("Login_by","");
        if(str.equals("email"))
            btn_exit.setText("خروج"+": "+pref.getString("email",""));
        else
            btn_exit.setText("خروج"+": "+pref.getString("phone",""));
    }

    private   void  show_city_list(){
        final Dialog city_dialog = new Dialog(getActivity());
        city_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        city_dialog.setContentView(R.layout.city_list_dialog);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
        databaseAccess.open();
        List<String> citys = databaseAccess.get_city_name();
        databaseAccess.close();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.city_list_row,R.id.city_name, citys);
        ListView city_list=(ListView)city_dialog.findViewById(R.id.city_list);
        city_list.setAdapter(adapter);

        city_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences pref;
                SharedPreferences.Editor editor;
                pref = getActivity().getApplicationContext().getSharedPreferences("setting", getActivity().MODE_PRIVATE);
                editor = pref.edit();
                editor.putInt("city_id",++position);
                editor.commit();
                Toast.makeText(getActivity(),
                        "city is changeed successfully!"+"",Toast.LENGTH_LONG).show();
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
    private void set_title() {

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("setting", getActivity().MODE_PRIVATE);
        int c_id = pref.getInt("city_id", 0);
        if(c_id!=0) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
            databaseAccess.open();
            String city_title = databaseAccess.get_city_name_by_id(c_id);
            databaseAccess.close();

            btn_city.setText("شهر:" + city_title);
        }else btn_city.setText("انتخاب شهر");
    }

    public static void ok() {
        log_out_flag=true;
    }
}
